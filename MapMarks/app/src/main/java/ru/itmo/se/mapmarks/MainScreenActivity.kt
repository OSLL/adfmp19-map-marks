package ru.itmo.se.mapmarks

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_main_screen.*
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer
import kotlinx.android.synthetic.main.mark_info_sheet_layout.*
import ru.itmo.se.mapmarks.myElementsActivity.MyCategoriesActivity
import ru.itmo.se.mapmarks.myElementsActivity.MyMarksActivity
import ru.itmo.se.mapmarks.prototype.LocationConverter
import kotlin.random.Random
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationListener
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.getSystemService
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.CameraPosition



class MainScreenActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {
    private lateinit var markInfoPopup: MarkInfoPopup
    private val markInfoContainer = DummyMarkInfoContainer.INSTANCE
    private var currentLocation: LatLng? = null
    private lateinit var map: GoogleMap
    private lateinit var locationManager: LocationManager
    private var currentLocationMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        markInfoPopup = MarkInfoPopup()

        setSupportActionBar(mainMenuToolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(android.R.drawable.ic_menu_camera)
        }

        addMarkButtonMain.setOnClickListener(AddMarkButtonOnClickListener(this, 1))
        (mainScreenMap as SupportMapFragment).getMapAsync(this)

        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.mainMyMarksOptionMenu -> startActivity(Intent(this, MyMarksActivity::class.java))
                R.id.mainMyCategoriesOptionMenu -> startActivity(Intent(this, MyCategoriesActivity::class.java))
            }
            drawerLayout.closeDrawers()
            true
        }

        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

        shareButton.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "Share body"
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this@MainScreenActivity, "Метка добавлена", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setPadding(0, 0, 0, 150)
        map.setOnMarkerClickListener(this)
        map.setOnMapClickListener {
            markInfoPopup.hidePopup()
        }

        //TODO show minimal size bounding box which including all marks
        markInfoContainer.allMarks.forEach {
            map.addMarker(it.options.icon(getMarkerIcon(it.category.color))).tag = it
        }

//        getLocation()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val mark = marker.tag as? Mark ?: return true
        markInfoPopup.fillMarkInfo(mark)
        markInfoPopup.showPopup()
        return true
    }

    override fun onLocationChanged(location: Location) {
        currentLocationMarker?.remove()
        val latLng = LatLng(location.latitude, location.longitude)
        currentLocationMarker = map.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))
        val position = CameraPosition.Builder().target(latLng).zoom(17f).bearing(19f).tilt(30f).build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position))
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    fun getLocation() {
        try {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                }
            }
        }
    }

    private fun getMarkerIcon(color: Int): BitmapDescriptor {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }

    private inner class MarkInfoPopup {
        private val layout = mark_info_sheet

        init {
            layout.visibility = View.INVISIBLE
        }

        fun fillMarkInfo(mark: Mark) {
            //TODO replace hardcode everywhere
            markName.setTextColor(mark.category.color)
            markName.text = mark.name
            markCategory.text = mark.category.name
            markInfoDescription.text = mark.description
            markInfoLocation.text = mark.options.position.let { LocationConverter.convert(it.latitude, it.longitude) }
            markInfoPlace.text = "Пенза, РФ"
            markInfoDistance.text = "${Random.nextInt(12000)}км от Вас"
        }

        fun showPopup() {
            layout.animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
            setEnable(mark_info_sheet, true)
            layout.visibility = View.VISIBLE
        }

        fun hidePopup() {
            layout.animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
            setEnable(mark_info_sheet, false)
            layout.visibility = View.INVISIBLE
        }

        private fun setEnable(v: View, enabled: Boolean) {
            if (v is ViewGroup) {
                for (i in 0 until v.childCount) {
                    setEnable(v.getChildAt(i), enabled)
                }
            }
            v.isEnabled = enabled
        }
    }
}


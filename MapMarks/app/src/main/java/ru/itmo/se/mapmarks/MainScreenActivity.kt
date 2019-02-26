package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.location.LocationProvider
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer
import ru.itmo.se.mapmarks.prototype.LocationConverter
import kotlin.random.Random

class MainScreenActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var map: GoogleMap
    private lateinit var mainView: CoordinatorLayout
    private lateinit var markInfoSheetLayout: LinearLayout

    private val markInfoContainer = DummyMarkInfoContainer.INSTANCE

    private var currentLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        markInfoSheetLayout = findViewById(R.id.mark_info_sheet)
        markInfoSheetLayout.visibility = View.INVISIBLE

        mainView = findViewById<CoordinatorLayout>(R.id.main_layout)

        mainView.setOnClickListener { view ->
                Log.d("IOEO#IOEFIORM#IOCMORV", "remmeorevtrv")
                markInfoSheetLayout.visibility = View.INVISIBLE
        }

        val toolbar = findViewById<Toolbar>(R.id.main_menu_toolbar)
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(android.R.drawable.ic_menu_camera)
        }

        val addMarkButton = findViewById<FloatingActionButton>(R.id.add_mark_button_main)
        addMarkButton.setOnClickListener(AddMarkButtonOnClickListener(this, requestCode = 1))

        val mapFragment = supportFragmentManager.findFragmentById(R.id.main_screen_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mDrawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.main_my_marks_option_menu -> startActivity(Intent(this, MyMarksActivity::class.java))
                R.id.main_my_categories_option_menu -> startActivity(Intent(this, MyCategoriesActivity::class.java))
            }
            mDrawerLayout.closeDrawers()
            true
        }

        val shareButton = findViewById<Button>(R.id.mark_info_share_button)
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
                mDrawerLayout.openDrawer(GravityCompat.START)
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

        markInfoContainer.allMarks.forEach {
            map.addMarker(it.options.icon(getMarkerIcon(it.category.color))).tag = it
        }

        // TODO tbd move current location marker, not adding new one
        if (currentLocation == null) {
            currentLocation = LocationProvider.from(this)
        }

        if (currentLocation != null) {
            map.addMarker(MarkerOptions().position(currentLocation!!).icon(getMarkerIcon(Color.YELLOW)))
            map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        } else {
            markInfoContainer.allMarks.first()
                .let { map.moveCamera(CameraUpdateFactory.newLatLng(it.options.position)) }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val mark = marker.tag as? Mark ?: return true

        val markNameView = markInfoSheetLayout.findViewById<TextView>(R.id.mark_info_mark_name)
        val markCategoryNameView = markInfoSheetLayout.findViewById<TextView>(R.id.mark_info_mark_category)
        val markDescriptionView = markInfoSheetLayout.findViewById<TextView>(R.id.mark_info_mark_description)
        val markCoordinatesView = markInfoSheetLayout.findViewById<TextView>(R.id.mark_info_mark_coordinates)
        val markLocationPlaceView = markInfoSheetLayout.findViewById<TextView>(R.id.mark_info_mark_location_place)
        val markDistanceView = markInfoSheetLayout.findViewById<TextView>(R.id.mark_info_mark_distance)

        markNameView.setTextColor(mark.category.color)
        markNameView.text = mark.name
        markCategoryNameView.text = mark.category.name
        markDescriptionView.text = mark.description
        markCoordinatesView.text = mark.options.position.let { LocationConverter.convert(it.latitude, it.longitude) }
        markLocationPlaceView.text = "Пенза, РФ"

        markDistanceView.text = "${Random.nextInt(12000)}км от Вас"

        val slideUp = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)

        @SuppressWarnings("unused")
        val slideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)

        markInfoSheetLayout.animation = slideUp
        markInfoSheetLayout.visibility = View.VISIBLE

        return true
    }

    private fun getMarkerIcon(color: Int): BitmapDescriptor {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }
}


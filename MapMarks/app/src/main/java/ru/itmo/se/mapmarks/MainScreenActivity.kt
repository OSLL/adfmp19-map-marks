package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.AdapterView
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
import ru.itmo.se.mapmarks.data.resources.RequestCodes
import ru.itmo.se.mapmarks.map.MapWithCurrentLocation
import android.widget.ArrayAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import ru.itmo.se.mapmarks.addElementActivity.AddMarkActivity

class MainScreenActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var markInfoPopup: MarkInfoPopup
    private val markInfoContainer = DummyMarkInfoContainer.INSTANCE
    private lateinit var map: MapWithCurrentLocation
    private lateinit var marksAdapter: ArrayAdapter<Mark>

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

        addMarkButtonMain.setOnClickListener(AddMarkButtonOnClickListener(this, RequestCodes.MAIN_ADD_MARK))
//        addMarkButtonMain.setOnClickListener(StartActivityForResultListener(this, AddMarkActivity::class.java, 1))

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
        val searchView = menu.findItem(R.id.mainScreenSearch).actionView as SearchView
        val searchAutoComplete =
            searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        marksAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, markInfoContainer.allMarks as List
        )
        searchAutoComplete.setAdapter(marksAdapter)
        searchAutoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val mark = parent?.getItemAtPosition(position) as Mark
                map.backedMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark.options.position, 14F))
            }
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
        if (data != null && requestCode in arrayOf(1, 2, 3, 4) && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this@MainScreenActivity, "Метка добавлена", Toast.LENGTH_SHORT).show()
            val name = data.getStringExtra("name")
            val newMark = markInfoContainer.getMarkByName(name)
            marksAdapter.remove(newMark)
            marksAdapter.add(newMark)
            map.backedMap.addMarker(newMark.options.icon(getMarkerIcon(newMark.category.color))).tag = newMark
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setPadding(0, 0, 0, 150)
        googleMap.setOnMarkerClickListener(this)
        googleMap.setOnMapClickListener {
            markInfoPopup.hidePopup()
        }

        // TODO show minimal size bounding box which including all marks
        markInfoContainer.allMarks.forEach {
            googleMap.addMarker(it.options.icon(getMarkerIcon(it.category.color))).tag = it
        }

        map = MapWithCurrentLocation(googleMap, this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val mark = marker.tag as? Mark ?: return true
        markInfoPopup.fillMarkInfo(mark)
        markInfoPopup.showPopup()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        map.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
            editButton.setOnClickListener(EditMarkButtonOnClickListener(
                this@MainScreenActivity,
                requestCode = RequestCodes.MAIN_EDIT_MARK,
                markNameToEdit = markName.text.toString())
            )
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


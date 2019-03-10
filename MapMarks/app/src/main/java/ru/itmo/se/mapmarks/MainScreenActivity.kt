package ru.itmo.se.mapmarks

import android.annotation.SuppressLint
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
import ru.itmo.se.mapmarks.location.LocationProvider
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer
import kotlinx.android.synthetic.main.mark_info_sheet_layout.*
import ru.itmo.se.mapmarks.myElementsActivity.MyCategoriesActivity
import ru.itmo.se.mapmarks.myElementsActivity.MyMarksActivity
import ru.itmo.se.mapmarks.prototype.LocationConverter
import kotlin.random.Random
import android.widget.ArrayAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import ru.itmo.se.mapmarks.addElementActivity.AddMarkActivity

class MainScreenActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var markInfoPopup: MarkInfoPopup
    private val markInfoContainer = DummyMarkInfoContainer.INSTANCE
    private var currentLocation: LatLng? = null
    private lateinit var map: GoogleMap
    private lateinit var marksAdapter: ArrayAdapter<Mark>
    private var categoryName: String? = null
    private var markList = markInfoContainer.allMarks.toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        initFilter()
        markInfoPopup = MarkInfoPopup()
        setSupportActionBar(mainMenuToolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(android.R.drawable.ic_menu_camera)
        }

        addMarkButtonMain.setOnClickListener(StartActivityForResultListener(this, AddMarkActivity::class.java, 1))
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
        marksAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, markList)
        searchAutoComplete.setAdapter(marksAdapter)
        searchAutoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val mark = parent?.getItemAtPosition(position) as Mark
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(mark.options.position, 14F))
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
        if (data != null && requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this@MainScreenActivity, "Метка добавлена", Toast.LENGTH_SHORT).show()
            val name = data.getStringExtra("name")
            val newMark = markInfoContainer.getMarkByName(name)
            if (newMark.category.name == categoryName) {
                marksAdapter.add(newMark)
                map.addMarker(newMark.options.icon(getMarkerIcon(newMark.category.color))).tag = newMark
            }
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
        markList.forEach {
            map.addMarker(it.options.icon(getMarkerIcon(it.category.color))).tag = it
        }

        // TODO tbd move current location marker, not adding new one
        if (currentLocation == null) {
            currentLocation = LocationProvider.from(this)
        }


//        //TODO what are doing this code?
//        if (currentLocation != null) {
//            map.addMarker(MarkerOptions().position(currentLocation!!).icon(getMarkerIcon(Color.YELLOW)))
//            map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
//        } else {
//            markList.first()
//                .let { map.moveCamera(CameraUpdateFactory.newLatLng(it.options.position)) }
//        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val mark = marker.tag as? Mark ?: return true
        markInfoPopup.fillMarkInfo(mark)
        markInfoPopup.showPopup()
        return true
    }

    private fun getMarkerIcon(color: Int): BitmapDescriptor {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }

    @SuppressLint("RestrictedApi")
    private fun initFilter() {
        categoryName = intent.getStringExtra("categoryName")
        if (categoryName != null) {
            val category = markInfoContainer.getCategoryByName(categoryName!!)
            markList = markList.filter { it.category.name == categoryName }
            removeCategoryButton.visibility = View.VISIBLE
            setViewColor(removeCategoryButton, category.color)
            removeCategoryButton.setOnClickListener {
                map.clear()
                marksAdapter.clear()
                markList = markInfoContainer.allMarks.toList()
                removeCategoryButton.visibility = View.INVISIBLE

                markList.forEach {
                    map.addMarker(it.options.icon(getMarkerIcon(it.category.color))).tag = it
                }
                marksAdapter.addAll(markList)
            }
        }
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
            layout.visibility = View.VISIBLE
        }

        fun hidePopup() {
            layout.animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
            layout.visibility = View.INVISIBLE
        }
    }
}


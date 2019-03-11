package ru.itmo.se.mapmarks

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import com.google.android.gms.maps.model.LatLng

class MainScreenActivity : AppCompatActivity(), OnMapReadyCallback {

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
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(mark.getBound(), 25, 25, 5)
                map.animateCamera(cameraUpdate)
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
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setPadding(0, 0, 0, 150)
        map.setOnMapClickListener {
            markInfoPopup.hidePopup()
        }

        //TODO show minimal size bounding box which including all marks
        markList.forEach { it.addToMap(map) }

        // TODO tbd move current location marker, not adding new one
        if (currentLocation == null) {
            currentLocation = LocationProvider.from(this)
        }

        map.setOnPolygonClickListener {
            val mark = it.tag as? Mark ?: return@setOnPolygonClickListener
            onMarkClick(mark)
        }
        map.setOnMarkerClickListener {
            val mark = it.tag as? Mark ?: return@setOnMarkerClickListener true
            onMarkClick(mark)
            true
        }
    }

    private fun onMarkClick(mark: Mark) {
        markInfoPopup.fillMarkInfo(mark)
        markInfoPopup.showPopup()
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

                markList.forEach { it.addToMap(map) }
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
            markInfoLocation.text = LocationConverter.convert(mark.getPosition().latitude, mark.getPosition().longitude)
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


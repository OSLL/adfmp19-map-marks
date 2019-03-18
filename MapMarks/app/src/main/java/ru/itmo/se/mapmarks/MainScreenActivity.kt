package ru.itmo.se.mapmarks

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_main_screen.*
import ru.itmo.se.mapmarks.data.mark.Mark
import kotlinx.android.synthetic.main.mark_info_sheet_layout.*
import ru.itmo.se.mapmarks.myElementsActivity.MyCategoriesActivity
import ru.itmo.se.mapmarks.myElementsActivity.MyMarksActivity
import ru.itmo.se.mapmarks.prototype.LocationConverter
import android.view.ViewGroup
import ru.itmo.se.mapmarks.data.resources.RequestCodes
import ru.itmo.se.mapmarks.map.MapWithCurrentLocation
import android.widget.ArrayAdapter
import ru.itmo.se.mapmarks.data.mark.share.ShareableMark
import ru.itmo.se.mapmarks.data.storage.GsonContainerWriter
import ru.itmo.se.mapmarks.data.storage.MarkInfoContainer
import ru.itmo.se.mapmarks.data.storage.SavedMarkInfoContainer

class MainScreenActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var markInfoPopup: MarkInfoPopup
    private lateinit var markInfoContainer: MarkInfoContainer
    private lateinit var map: MapWithCurrentLocation
    private lateinit var marksAdapter: ArrayAdapter<Mark>
    private var categoryName: String? = null
    private lateinit var markList: List<Mark>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        markInfoPopup = MarkInfoPopup()
        setSupportActionBar(mainMenuToolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_slide_menu)
        }

        initContainer()

        addMarkButtonMain.setOnClickListener(AddMarkButtonOnClickListener(this, RequestCodes.MAIN_ADD_MARK))
        (mainScreenMap as SupportMapFragment).getMapAsync(this)
    }

    override fun onPause() {
        Log.d("DEBUG", "On pause")
        super.onPause()
        saveData()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        saveData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu_search, menu)
        val searchView = menu.findItem(R.id.mainScreenSearch).actionView as SearchView
        val searchAutoComplete =
            searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        marksAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, markList.toMutableList())
        searchAutoComplete.setAdapter(marksAdapter)
        searchAutoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val mark = parent?.getItemAtPosition(position) as Mark
                flyToMark(map.backedMap, mark.getBound())
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
            if (categoryName == null || newMark.category.name == categoryName) {
                if (newMark.attach != null) {
                    marksAdapter.remove(newMark.attach as Mark)
                    (newMark.attach as Mark).remove()
                }
                marksAdapter.add(newMark)
                newMark.addToMap(map.backedMap)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setPadding(0, 0, 0, 150)
        googleMap.setOnMapClickListener {
            markInfoPopup.hidePopup()
        }

        //TODO show minimal size bounding box which including all marks
        markList.forEach { it.addToMap(googleMap) }

        googleMap.setOnPolygonClickListener {
            val mark = it.tag as? Mark ?: return@setOnPolygonClickListener
            onMarkClick(mark)
        }
        googleMap.setOnMarkerClickListener {
            val mark = it.tag as? Mark ?: return@setOnMarkerClickListener true
            onMarkClick(mark)
            true
        }

        map = MapWithCurrentLocation(googleMap, this)

        val selectName = intent.getStringExtra("selectMark")
        if (selectName != null) {
            val mark = markInfoContainer.getMarkByName(selectName)
            flyToMark(googleMap, mark.getBound())
        } else {
            map.moveCameraToCurrentPosition()
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            val elementsIntent = when (menuItem.itemId) {
                R.id.mainMyMarksOptionMenu -> Intent(this, MyMarksActivity::class.java)
                else -> Intent(this, MyCategoriesActivity::class.java)
            }
            map.currentLocation?.let {
                elementsIntent.putExtra(
                    "currentLocation",
                    doubleArrayOf(it.latitude, it.longitude)
                )
            }
            startActivity(elementsIntent)
            drawerLayout.closeDrawers()
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
            if (!::markInfoContainer.isInitialized) {
                initContainer()
            }
            val category = markInfoContainer.getCategoryByName(categoryName!!)
            markList = markList.filter { it.category.name == categoryName }
            removeCategoryButton.visibility = View.VISIBLE
            setViewColor(removeCategoryButton, category.color)
            removeCategoryButton.setOnClickListener {
                map.backedMap.clear()
                marksAdapter.clear()
                markList = markInfoContainer.allMarks.toList()
                removeCategoryButton.visibility = View.INVISIBLE
                markList.forEach { it.addToMap(map.backedMap) }
                marksAdapter.addAll(markList)
            }
        }
    }

    private fun initContainer() {
        val testMod = intent.getBooleanExtra("test", false)
        if (testMod) {
            val path = intent.getStringExtra("path")
            SavedMarkInfoContainer.register(applicationContext, path)
        } else {
            SavedMarkInfoContainer.register(
                applicationContext,
                applicationContext.getString(R.string.savedDataFileName)
            )
        }
        markInfoContainer = SavedMarkInfoContainer.INSTANCE
        markList = markInfoContainer.allMarks.toList()
        initFilter()
    }

    private fun saveData() {
        val serializedContainerData = markInfoContainer.write(GsonContainerWriter())
        applicationContext.openFileOutput(resources.getString(R.string.savedDataFileName), Context.MODE_PRIVATE).use {
            it.write(serializedContainerData.toByteArray())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        map.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private inner class MarkInfoPopup {
        private val layout = mark_info_sheet
        private lateinit var selectMark: Mark

        init {
            layout.visibility = View.INVISIBLE
        }

        @SuppressLint("SetTextI18n")
        fun fillMarkInfo(mark: Mark) {
            //TODO replace hardcode everywhere
            markName.setTextColor(mark.category.color)
            markName.text = mark.name
            markCategory.text = mark.category.name
            markInfoDescription.text = mark.description
            markInfoLocation.text = LocationConverter.convert(mark.getPosition().latitude, mark.getPosition().longitude)
            markInfoDistance.text = "${getDistance(map.currentLocation!!, mark.getPosition())} от Вас"
            selectMark = mark

            shareButton.setOnClickListener {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val shareBody = ShareableMark(mark).makrShareBody()
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(sharingIntent, "Поделиться через"));
            }
        }

        fun showPopup() {
            layout.animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
            setEnable(mark_info_sheet, true)
            layout.visibility = View.VISIBLE

            editButton.setOnClickListener(
                EditMarkButtonOnClickListener(
                    this@MainScreenActivity,
                    requestCode = RequestCodes.MAIN_EDIT_MARK,
                    markNameToEdit = markName.text.toString(),
                    markCoordinates = selectMark.getPosition()
                )
            )

            routeButton.setOnClickListener {
                val position = selectMark.getPosition()
                val intent = Intent(
                    android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=${position.latitude},${position.longitude}")
                )
                startActivity(intent)
            }
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
package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import android.widget.Toast
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class MainScreenActivity : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        mDrawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.main_my_marks_option_menu -> startActivity(Intent(this, MyMarksActivity::class.java))
            }
            mDrawerLayout.closeDrawers()
            true
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
}


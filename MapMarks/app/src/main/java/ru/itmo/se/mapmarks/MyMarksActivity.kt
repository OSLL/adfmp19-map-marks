package ru.itmo.se.mapmarks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class MyMarksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_marks)

        val cardsView = findViewById<RecyclerView>(R.id.my_marks_recycler_view)
        cardsView.layoutManager = LinearLayoutManager(this)

        val cardsAdapter = MyMarksViewAdapter(DummyMarkInfoContainer.INSTANCE.allMarks.toList(), this)
        cardsView.adapter = cardsAdapter
    }
}

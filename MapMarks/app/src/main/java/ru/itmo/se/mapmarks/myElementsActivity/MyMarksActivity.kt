package ru.itmo.se.mapmarks.myElementsActivity

import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_my_elements.*
import ru.itmo.se.mapmarks.StartActivityForResultListener
import ru.itmo.se.mapmarks.adapters.MyMarksViewAdapter
import ru.itmo.se.mapmarks.addElementActivity.AddMarkActivity
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class MyMarksActivity : MyElementsActivity<MyMarksViewAdapter.MyMarksViewHolder, Mark>("Метка добавлена") {
    private lateinit var marksList: List<Mark>

    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = intent
        val name = intent.getStringExtra("name")
        if (name == null) {
            marksList = DummyMarkInfoContainer.INSTANCE.allMarks.toList()
        } else {
            marksList = DummyMarkInfoContainer.INSTANCE.allMarks.filter { it.category.name == name }.toList()
        }
        super.onCreate(savedInstanceState)
    }

    override fun addButtonListener() {
        addButton.setOnClickListener {
            StartActivityForResultListener(this, AddMarkActivity::class.java, 1)
        }
    }

    override fun actualAdapter(context: Context) =
        MyMarksViewAdapter(marksList, context)
}


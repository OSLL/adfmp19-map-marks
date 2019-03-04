package ru.itmo.se.mapmarks.myElementsActivity

import android.content.Context
import kotlinx.android.synthetic.main.activity_my_elements.*
import ru.itmo.se.mapmarks.AddMarkButtonOnClickListener
import ru.itmo.se.mapmarks.adapters.MyMarksViewAdapter
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class MyMarksActivity : MyElementsActivity<MyMarksViewAdapter.MyMarksViewHolder, Mark>("Метка добавлена") {
    override fun addButtonListener() {
        addButton.setOnClickListener(AddMarkButtonOnClickListener(this, 1))
    }

    override fun actualAdapter(context: Context) =
        MyMarksViewAdapter(DummyMarkInfoContainer.INSTANCE.allMarks.toList(), context)
}


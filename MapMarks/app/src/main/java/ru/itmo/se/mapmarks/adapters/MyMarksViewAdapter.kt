package ru.itmo.se.mapmarks.adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.itmo.se.mapmarks.R
import ru.itmo.se.mapmarks.data.mark.Mark
import kotlin.random.Random

class MyMarksViewAdapter(allElements: List<Mark>, context: Context) :
    MyElementsAdapter<MyMarksViewAdapter.MyMarksViewHolder, Mark>(allElements, context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMarksViewHolder {
        val markView = LayoutInflater.from(parent.context).inflate(R.layout.my_marks_cards, parent, false)
        return MyMarksViewHolder(markView)
    }

    override fun getItemCount() = allElements.size

    override fun onBindViewHolder(holder: MyMarksViewHolder, position: Int) {
        val currentMark = allElements[position]
        holder.markNameView.text = currentMark.name
        holder.markDescriptionView.text = currentMark.description
        holder.markCategoryView.text = context.getString(R.string.my_marks_category_tag) + currentMark.category.name

        // TODO real location
        holder.markLocationDescriptionView.text = "${Random.nextInt(12000)} км от Вас"

        holder.view.findViewById<CardView>(R.id.my_marks_card_view).setCardBackgroundColor(currentMark.category.color)
    }

    inner class MyMarksViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val markNameView: TextView = view.findViewById(R.id.my_marks_mark_name)
        val markDescriptionView: TextView = view.findViewById(R.id.my_marks_mark_description)
        val markCategoryView: TextView = view.findViewById(R.id.my_marks_mark_category)
        val markLocationDescriptionView: TextView = view.findViewById(R.id.my_marks_mark_location)
    }
}
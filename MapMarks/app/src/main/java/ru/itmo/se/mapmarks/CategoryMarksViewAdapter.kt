package ru.itmo.se.mapmarks

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.itmo.se.mapmarks.data.mark.Mark
import kotlin.random.Random

class CategoryMarksViewAdapter(val marksInCategory: List<Mark>, val context: Context): RecyclerView.Adapter<CategoryMarksViewAdapter.CategoryMarksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMarksViewHolder {
        val markView = LayoutInflater.from(parent.context).inflate(R.layout.categories_marks_cards, parent, false)
        return CategoryMarksViewHolder(markView)
    }

    override fun getItemCount() = marksInCategory.size

    override fun onBindViewHolder(holder: CategoryMarksViewHolder, position: Int) {
        val currentMark = marksInCategory[position]
        holder.markNameView.text = currentMark.name
        holder.markDescriptionView.text = currentMark.description

        // TODO real location
        holder.markLocationDescriptionView.text = "${Random.nextInt(12000)} км от Вас"

        holder.view.findViewById<CardView>(R.id.category_marks_card_view).setCardBackgroundColor(currentMark.category.color)
    }

    inner class CategoryMarksViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val markNameView: TextView = view.findViewById(R.id.category_marks_mark_name)
        val markDescriptionView: TextView = view.findViewById(R.id.category_marks_mark_description)
        val markLocationDescriptionView: TextView = view.findViewById(R.id.category_marks_mark_location)
    }
}
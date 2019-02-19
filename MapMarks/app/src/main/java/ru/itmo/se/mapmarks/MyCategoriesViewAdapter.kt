package ru.itmo.se.mapmarks

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class MyCategoriesViewAdapter(val allCategories: List<Category>, val context: Context): RecyclerView.Adapter<MyCategoriesViewAdapter.MyCategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoriesViewHolder {
        val categoryView = LayoutInflater.from(parent.context).inflate(R.layout.my_categories_cards, parent, false)
        return MyCategoriesViewHolder(categoryView)
    }

    override fun getItemCount() = allCategories.size

    override fun onBindViewHolder(holder: MyCategoriesViewHolder, position: Int) {
        val currentCategory = allCategories[position]
        holder.categoryNameView.text = currentCategory.name
        holder.categoryDescriptionView.text = currentCategory.description
        holder.marksInCategoryCountView.text = context.getString(R.string.my_categories_count_tag) + DummyMarkInfoContainer.INSTANCE.getMarksForCategory(currentCategory).toList().size

        holder.view.findViewById<CardView>(R.id.my_categories_card_view).setCardBackgroundColor(currentCategory.color)
    }

    inner class MyCategoriesViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val categoryNameView: TextView = view.findViewById(R.id.my_categories_category_name)
        val categoryDescriptionView: TextView = view.findViewById(R.id.my_categories_category_description)
        val marksInCategoryCountView: TextView = view.findViewById(R.id.my_categories_marks_count)
    }
}
package ru.itmo.se.mapmarks.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import ru.itmo.se.mapmarks.R
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.storage.SavedMarkInfoContainer
import ru.itmo.se.mapmarks.myElementsActivity.MyMarksActivity

class MyCategoriesViewAdapter(allElements: List<Category>, context: Context, currentLocation: LatLng?) :
    MyElementsViewAdapter<MyCategoriesViewAdapter.MyCategoriesViewHolder, Category>(
        allElements,
        context,
        currentLocation
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoriesViewHolder {
        val categoryView = LayoutInflater.from(parent.context).inflate(R.layout.my_categories_cards, parent, false)
        return MyCategoriesViewHolder(categoryView)
    }

    override fun onBindViewHolder(holder: MyCategoriesViewHolder, position: Int) {
        val currentCategory = allElements[position]
        holder.categoryNameView.text = currentCategory.name
        holder.categoryDescriptionView.text = currentCategory.description
        holder.marksInCategoryCountView.text =
            context.getString(R.string.my_categories_count_tag) + SavedMarkInfoContainer.INSTANCE.getMarksForCategory(
                currentCategory
            ).toList().size
        holder.view.findViewById<CardView>(R.id.my_categories_card_view).setCardBackgroundColor(currentCategory.color)
    }

    inner class MyCategoriesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val categoryNameView: TextView = view.findViewById(R.id.my_categories_category_name)
        val categoryDescriptionView: TextView = view.findViewById(R.id.my_categories_category_description)
        val marksInCategoryCountView: TextView = view.findViewById(R.id.my_categories_marks_count)

        init {
            view.setOnClickListener { view ->
                val categoryMarksIntent = Intent(context, MyMarksActivity::class.java)
                categoryMarksIntent.putExtra("name", categoryNameView.text.toString())
                currentLocation?.let {
                    categoryMarksIntent.putExtra("currentLocation", doubleArrayOf(currentLocation.latitude, currentLocation.longitude))
                }
                context.startActivity(categoryMarksIntent)
            }
        }
    }
}
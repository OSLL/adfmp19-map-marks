package ru.itmo.se.mapmarks.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import ru.itmo.se.mapmarks.MainScreenActivity
import ru.itmo.se.mapmarks.R
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.getDistance

class MyMarksViewAdapter(allElements: List<Mark>,context: Context, currentLocation: LatLng) :
    MyElementsViewAdapter<MyMarksViewAdapter.MyMarksViewHolder, Mark>(allElements, context, currentLocation) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMarksViewHolder {
        val markView = LayoutInflater.from(parent.context).inflate(R.layout.my_marks_cards, parent, false)
        return MyMarksViewHolder(markView)
    }

    override fun getItemCount() = allElements.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyMarksViewHolder, position: Int) {
        val currentMark = allElements[position]
        holder.markNameView.text = currentMark.name
        holder.markDescriptionView.text = currentMark.description
        holder.markCategoryView.text = context.getString(R.string.my_marks_category_tag) + currentMark.category.name

        // TODO real location
        holder.markLocationDescriptionView.text = "${getDistance(currentLocation, currentMark.getPosition())} км от Вас"
        holder.view.findViewById<CardView>(R.id.my_marks_card_view).setCardBackgroundColor(currentMark.category.color)
    }

    inner class MyMarksViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val markNameView: TextView = view.findViewById(R.id.my_marks_mark_name)
        val markDescriptionView: TextView = view.findViewById(R.id.my_marks_mark_description)
        val markCategoryView: TextView = view.findViewById(R.id.my_marks_mark_category)
        val markLocationDescriptionView: TextView = view.findViewById(R.id.my_marks_mark_location)

        init {
            view.setOnClickListener {
                val name = (view.findViewById(R.id.my_marks_mark_name) as TextView).text.toString()
                val newMarkIntent = Intent(context, MainScreenActivity::class.java)
                newMarkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                newMarkIntent.putExtra("selectMark", name)
                (context as Activity).finish()
                context.startActivity(newMarkIntent)
            }
        }
    }
}
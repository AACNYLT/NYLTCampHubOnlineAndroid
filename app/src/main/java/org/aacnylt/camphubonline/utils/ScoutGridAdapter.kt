package org.aacnylt.camphubonline.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.aacnylt.camphubonline.R
import org.aacnylt.camphubonline.models.Scout

class ScoutGridAdapter(context: Context, list: ArrayList<Scout>) : ArrayAdapter<Scout>(context, 0, list) {
    var scoutList = ArrayList<Scout>()
    var localContext: Context

    init {
        scoutList = list
        localContext = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var listItem = convertView
        if (listItem == null)
            listItem = LayoutInflater.from(localContext).inflate(R.layout.scout_grid_item, parent, false)
        listItem!!
        val currentScout = scoutList[position]

        val scoutImage = listItem.findViewById<ImageView>(R.id.ScoutItemImage)
        val scoutName = listItem.findViewById<TextView>(R.id.ScoutItemName)
        val scoutTeam = listItem.findViewById<TextView>(R.id.ScoutItemTeam)
        val scoutPosition = listItem.findViewById<TextView>(R.id.ScoutItemPosition)
        val scoutCourse = listItem.findViewById<TextView>(R.id.ScoutItemCourse)

        Picasso.with(localContext).load(currentScout.imageUrl()).into(scoutImage)
        scoutName.text = currentScout.toString()
        scoutTeam.text = currentScout.Team
        scoutPosition.text = currentScout.Position
        if (currentScout.IsStaff == true) {
            scoutPosition.setTextColor(context.getColor(R.color.staff))
        } else {
            scoutPosition.setTextColor(scoutTeam.currentTextColor)
        }
        scoutCourse.text = StaticScoutService.CourseList.firstOrNull { it.CourseID == currentScout.CourseID }?.UnitName
                ?: currentScout.CourseID.toString()
        return listItem
    }


}
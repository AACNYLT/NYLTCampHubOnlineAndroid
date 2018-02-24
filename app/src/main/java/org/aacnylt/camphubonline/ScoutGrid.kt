package org.aacnylt.camphubonline

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import org.aacnylt.camphubonline.models.Scout
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScoutGrid : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scout_grid)
        val retrofit = Retrofit.Builder()
                .baseUrl("http://nyltcamphub.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(ScoutService::class.java)
        var scoutList = service.allScouts.execute().body()
        val ScoutGridView = findViewById<ListView>(R.id.ScoutGrid)
        val adapter = ArrayAdapter<Scout>(this, android.R.layout.simple_list_item_1, scoutList)
        ScoutGridView.adapter = adapter
    }
}

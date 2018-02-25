package org.aacnylt.camphubonline

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import org.aacnylt.camphubonline.StaticScoutService.createProgressDialog
import org.aacnylt.camphubonline.StaticScoutService.createRetrofitService
import org.aacnylt.camphubonline.models.Scout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScoutGrid : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scout_grid)
        val progressDialog = createProgressDialog(this, "Loading scouts...")
        progressDialog.show()
        createRetrofitService().allScouts.enqueue(createCallback(progressDialog))
    }

    private fun createCallback(progressDialog: ProgressDialog): Callback<ArrayList<Scout>> {
       return object : Callback<ArrayList<Scout>> {
            override fun onResponse(call: Call<ArrayList<Scout>>, response: Response<ArrayList<Scout>>) {
                val scoutGridView = findViewById(R.id.ScoutGrid) as ListView
                val scoutList = response.body()!!
                val adapter = ArrayAdapter<Scout>(this@ScoutGrid, android.R.layout.simple_list_item_1, scoutList)
                scoutGridView.adapter = adapter
                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<ArrayList<Scout>>, t: Throwable) {
                Log.e("getFailure", t.message, t)
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}

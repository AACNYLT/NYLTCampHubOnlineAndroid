package org.aacnylt.camphubonline

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import org.aacnylt.camphubonline.utils.StaticScoutService.createProgressDialog
import org.aacnylt.camphubonline.utils.StaticScoutService.createRetrofitService
import org.aacnylt.camphubonline.models.Scout
import org.aacnylt.camphubonline.utils.ScoutGridAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScoutGrid : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scout_grid)
        setSupportActionBar(findViewById(R.id.mainbar) as Toolbar)
        loadScoutGrid()
    }

    fun loadScoutGrid() {
        val progressDialog = createProgressDialog(this, "Loading scouts...")
        progressDialog.show()
        createRetrofitService().allScouts.enqueue(createCallback(progressDialog))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scoutgridmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.logout -> {
                val intent = Intent(this@ScoutGrid, Login::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            R.id.refresh -> {
                loadScoutGrid()
            }
        }
        return true
    }

//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        setContentView(R.layout.activity_scout_grid)
//    }

    private fun createCallback(progressDialog: ProgressDialog): Callback<ArrayList<Scout>> {
        return object : Callback<ArrayList<Scout>> {
            override fun onResponse(call: Call<ArrayList<Scout>>, response: Response<ArrayList<Scout>>) {
                val scoutGridView = findViewById(R.id.ScoutGrid) as ListView
                val scoutList = response.body()!!
                val adapter = ScoutGridAdapter(this@ScoutGrid,  scoutList)
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

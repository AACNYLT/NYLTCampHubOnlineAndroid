package org.aacnylt.camphubonline

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import org.aacnylt.camphubonline.models.Scout
import org.aacnylt.camphubonline.utils.ScoutGridAdapter
import org.aacnylt.camphubonline.utils.StaticScoutService.createProgressDialog
import org.aacnylt.camphubonline.utils.StaticScoutService.createRetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScoutGrid : AppCompatActivity() {

    var mainScoutList = ArrayList<Scout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scout_grid)
        setSupportActionBar(findViewById(R.id.mainbar) as Toolbar)
        (findViewById(R.id.ScoutGridSwipeContainer) as SwipeRefreshLayout).setOnRefreshListener { loadScoutGrid() }
        loadScoutGrid()
    }

    fun loadScoutGrid() {
        (findViewById(R.id.ScoutGridSwipeContainer) as SwipeRefreshLayout).isRefreshing = true
        createRetrofitService().allScouts.enqueue(createCallback())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scoutgridmenu, menu)
        val searchbox = menu!!.findItem(R.id.search).actionView as SearchView
        searchbox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                handleSearch(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                handleSearch(query)
                return true
            }
        })
        return true
    }

    private fun handleSearch(query: String?) {
        if (query != null && query.isNotEmpty()) {
            filterList(query)

        } else {
            setScoutGrid(mainScoutList)
        }
    }

    private fun filterList(query: String) {
        val generatedList = ArrayList<Scout>()
        for (scout in mainScoutList) {
            if (nullableCheckContains(scout.FirstName, query)
                    || nullableCheckContains(scout.LastName, query)
                    || nullableCheckContains(scout.Team, query)
                    || nullableCheckContains(scout.Position, query)) {
                generatedList.add(scout)
            }
        }
        setScoutGrid(generatedList)
    }

    private fun nullableCheckContains(property: String?, pattern: String): Boolean {
        if (property != null) {
            if (property.toLowerCase().contains(pattern.toLowerCase())) {
                return true
            }
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.logout -> {
                val intent = Intent(this@ScoutGrid, Login::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
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

    private fun createCallback(): Callback<ArrayList<Scout>> {
        return object : Callback<ArrayList<Scout>> {
            override fun onResponse(call: Call<ArrayList<Scout>>, response: Response<ArrayList<Scout>>) {
                mainScoutList = response.body()!!
                setScoutGrid(mainScoutList)
                (findViewById(R.id.ScoutGridSwipeContainer) as SwipeRefreshLayout).isRefreshing = false
            }

            override fun onFailure(call: Call<ArrayList<Scout>>, t: Throwable) {
                Log.e("getFailure", t.message, t)
                (findViewById(R.id.ScoutGridSwipeContainer) as SwipeRefreshLayout).isRefreshing = false
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setScoutGrid(scoutList: ArrayList<Scout>) {
        val scoutGridView = findViewById(R.id.ScoutGrid) as ListView
        val adapter = ScoutGridAdapter(this@ScoutGrid, scoutList)
        scoutGridView.adapter = adapter
    }
}

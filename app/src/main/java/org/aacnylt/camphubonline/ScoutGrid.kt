package org.aacnylt.camphubonline

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import org.aacnylt.camphubonline.models.Course
import org.aacnylt.camphubonline.models.Scout
import org.aacnylt.camphubonline.utils.ScoutGridAdapter
import org.aacnylt.camphubonline.utils.StaticScoutService
import org.aacnylt.camphubonline.utils.StaticScoutService.CurrentUser
import org.aacnylt.camphubonline.utils.StaticScoutService.createRetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScoutGrid : AppCompatActivity() {

    var mainScoutList = ArrayList<Scout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_scout_grid)
            setSupportActionBar(findViewById(R.id.mainbar))
            (findViewById<SwipeRefreshLayout>(R.id.ScoutGridSwipeContainer)).setOnRefreshListener { loadScoutGrid() }
            (findViewById<SwipeRefreshLayout>(R.id.ScoutGridSwipeContainer)).setColorSchemeResources(R.color.colorAccent)
            val scoutGridView = findViewById<ListView>(R.id.ScoutGrid)
            scoutGridView.setOnItemClickListener { _, _, position, _ ->
                val selectedScout = scoutGridView.getItemAtPosition(position) as Scout
                val intent = Intent(this@ScoutGrid, ScoutActivity::class.java)
                intent.putExtra("scout", selectedScout)
                startActivity(intent)
            }
            if (CurrentUser.IsAdmin == true) {
                scoutGridView.setOnItemLongClickListener { _, view, position, _ ->
                    val selectedScout = scoutGridView.getItemAtPosition(position) as Scout
                    val contextMenu = PopupMenu(this, view)
                    contextMenu.menuInflater.inflate(R.menu.scoutcontextmenu, contextMenu.menu)
                    contextMenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.ScoutContextEdit -> launchEditProfile(selectedScout)
                        }
                        true
                    }
                    contextMenu.show()
                    true
                }
            }
            loadScoutGrid()
        } catch (ex: Exception) {
            startActivity(Intent(this@ScoutGrid, Login::class.java))
        }
    }

    private fun launchEditProfile(selectedScout: Scout) {
        val intent = Intent(this@ScoutGrid, ScoutEditActivity::class.java)
        intent.putExtra("scout", selectedScout)
        startActivity(intent)
    }

    fun loadScoutGrid() {
        (findViewById<SwipeRefreshLayout>(R.id.ScoutGridSwipeContainer)).isRefreshing = true
        createRetrofitService().courses.enqueue(createCourseCallback())
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

    private fun createCourseCallback(): Callback<ArrayList<Course>> {
        return object : Callback<ArrayList<Course>> {
            override fun onResponse(call: Call<ArrayList<Course>>, response: Response<ArrayList<Course>>) {
                StaticScoutService.CourseList = response.body()!!
                createRetrofitService().allScouts.enqueue(createCallback())
            }

            override fun onFailure(call: Call<ArrayList<Course>>, t: Throwable) {
                handleFailure(t)
            }
        }
    }

    private fun createCallback(): Callback<ArrayList<Scout>> {
        return object : Callback<ArrayList<Scout>> {
            override fun onResponse(call: Call<ArrayList<Scout>>, response: Response<ArrayList<Scout>>) {
                mainScoutList = filterCourses(removeSelf(response.body()!!))
                setScoutGrid(mainScoutList)
                (findViewById<SwipeRefreshLayout>(R.id.ScoutGridSwipeContainer)).isRefreshing = false
            }

            override fun onFailure(call: Call<ArrayList<Scout>>, t: Throwable) {
                handleFailure(t)
            }
        }
    }

    private fun handleFailure(t: Throwable) {
        Log.e("getFailure", t.message, t)
        (findViewById<SwipeRefreshLayout>(R.id.ScoutGridSwipeContainer)).isRefreshing = false
        Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
    }

    private fun removeSelf(scoutList: ArrayList<Scout>): ArrayList<Scout> {
        if (CurrentUser.IsAdmin != true) {
            scoutList.removeAt(scoutList.indexOfFirst { scout -> scout.ScoutID == CurrentUser.ScoutID })
        }
        return scoutList
    }

    private fun filterCourses(scoutList: ArrayList<Scout>): ArrayList<Scout> {
        if (CurrentUser.IsAdmin != true) {
            return ArrayList(scoutList.filter {
                it.CourseID == CurrentUser.CourseID
            })
        }
        return scoutList
    }

    private fun setScoutGrid(scoutList: ArrayList<Scout>) {
        val scoutGridView = findViewById<ListView>(R.id.ScoutGrid)
        val adapter = ScoutGridAdapter(this@ScoutGrid, scoutList)
        scoutGridView.adapter = adapter
    }
}

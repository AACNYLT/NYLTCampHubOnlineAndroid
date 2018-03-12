package org.aacnylt.camphubonline

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_scout.*
import org.aacnylt.camphubonline.models.Evaluation
import org.aacnylt.camphubonline.models.Scout
import org.aacnylt.camphubonline.utils.EvalListAdapter
import org.aacnylt.camphubonline.utils.StaticScoutService.createRetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScoutActivity : AppCompatActivity() {

    lateinit var currentScout: Scout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scout)
        currentScout = intent.getSerializableExtra("scout") as Scout
        val toolbar = findViewById<Toolbar>(R.id.ScoutToolbar)
        toolbar.title = currentScout.toString()
        setSupportActionBar(toolbar)
        (findViewById<SwipeRefreshLayout>(R.id.EvalListContainer)).setOnRefreshListener { loadEvalList() }
        val scoutImage = findViewById<ImageView>(R.id.ScoutImage)
        Picasso.with(this).load(currentScout.imageUrl()).into(scoutImage)
        loadEvalList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scoutactivitymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.EvalListRefresh -> loadEvalList()
        }
        return true
    }

    fun loadEvalList() {
        (findViewById<SwipeRefreshLayout>(R.id.EvalListContainer)).isRefreshing = true
        createRetrofitService().getScoutEvaluations(currentScout.ScoutID).enqueue(createCallback())
    }

    private fun createCallback(): Callback<ArrayList<Evaluation>> {
        return object : Callback<ArrayList<Evaluation>> {
            override fun onResponse(call: Call<ArrayList<Evaluation>>, response: Response<ArrayList<Evaluation>>) {
                (findViewById<SwipeRefreshLayout>(R.id.EvalListContainer)).isRefreshing = false
                val evalList = response.body()!!
                setEvalList(evalList)
            }

            override fun onFailure(call: Call<ArrayList<Evaluation>>, t: Throwable) {
                (findViewById<SwipeRefreshLayout>(R.id.EvalListContainer)).isRefreshing = false
                Log.e("getFailure", t.message, t)
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setEvalList(list: ArrayList<Evaluation>) {
        val viewManager = LinearLayoutManager(this)
        val viewAdapter = EvalListAdapter(list, this)
        findViewById<RecyclerView>(R.id.EvalList).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}

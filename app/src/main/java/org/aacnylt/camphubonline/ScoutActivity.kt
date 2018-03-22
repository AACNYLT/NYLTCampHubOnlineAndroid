package org.aacnylt.camphubonline

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_scout.*
import org.aacnylt.camphubonline.models.Evaluation
import org.aacnylt.camphubonline.models.Scout
import org.aacnylt.camphubonline.utils.EvalListAdapter
import org.aacnylt.camphubonline.utils.ScoutService
import org.aacnylt.camphubonline.utils.StaticScoutService.CurrentUser
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
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (findViewById<SwipeRefreshLayout>(R.id.EvalListContainer)).setOnRefreshListener { loadEvalList() }
        val scoutImage = findViewById<ImageView>(R.id.ScoutImage)
        Picasso.with(this).load(currentScout.imageUrl()).into(scoutImage)
        loadEvalList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scoutactivitymenu, menu)
        if (CurrentUser.IsAdmin != true) {
            menu!!.findItem(R.id.EditProfile).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.EvalListRefresh -> loadEvalList()
            R.id.EditProfile -> launchEditProfile()
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this@ScoutActivity)
        }
        return true
    }

    fun launchEditProfile() {
        val intent = Intent(this@ScoutActivity, ScoutEditActivity::class.java)
        intent.putExtra("scout", currentScout)
        startActivity(intent)
    }

    fun loadEvalList() {
        (findViewById<SwipeRefreshLayout>(R.id.EvalListContainer)).isRefreshing = true
        createRetrofitService().getScoutEvaluations(currentScout.ScoutID).enqueue(createCallback())
    }

    private fun createCallback(): Callback<ArrayList<Evaluation>> {
        return object : Callback<ArrayList<Evaluation>> {
            override fun onResponse(call: Call<ArrayList<Evaluation>>, response: Response<ArrayList<Evaluation>>) {
                (findViewById<SwipeRefreshLayout>(R.id.EvalListContainer)).isRefreshing = false
                val evalList = filterEvals(response.body()!!)
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
        val viewAdapter = EvalListAdapter(list, this, {
            launchCommentModal(it)
        }, { evaluation, view -> launchEvalContextMenu(evaluation, view) })
        findViewById<RecyclerView>(R.id.EvalList).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun launchCommentModal(eval: Evaluation) {
        val builder = AlertDialog.Builder(this@ScoutActivity)
        builder.setTitle(getString(R.string.eval_comments_header_preload))
        builder.setMessage(eval.Comments)
        val alert = builder.create()
        alert.show()
        createRetrofitService().getScout(eval.EvaluatorID).enqueue(object : Callback<Scout> {
            override fun onResponse(call: Call<Scout>?, response: Response<Scout>?) {
                if (response != null) {
                    alert.setTitle(getString(R.string.eval_comments_header).replace("%s1", response.body().toString()))
                }
            }

            override fun onFailure(call: Call<Scout>?, t: Throwable?) {}
        })
    }

    private fun launchEvalContextMenu(eval: Evaluation, v: View) {
        val contextMenu = PopupMenu(this, v)
        contextMenu.menuInflater.inflate(R.menu.evalcontextmenu, contextMenu.menu)
        if (eval.EvaluatorID != CurrentUser.ScoutID) {
            contextMenu.menu.findItem(R.id.EvalContextEdit).isVisible = false
        }
        if (CurrentUser.IsAdmin != true) {
            contextMenu.menu.findItem(R.id.EvalContextDelete).isVisible = false
        }
        contextMenu.show()
    }

    private fun filterEvals(evalList: ArrayList<Evaluation>): ArrayList<Evaluation> {
        if (CurrentUser.IsElevated != true) {
            return ArrayList(evalList.filter { evaluation -> evaluation.EvaluatorID == CurrentUser.ScoutID })
        } else {
            return evalList
        }
    }
}

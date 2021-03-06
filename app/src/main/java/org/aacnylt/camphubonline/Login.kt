package org.aacnylt.camphubonline

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import org.aacnylt.camphubonline.utils.StaticScoutService.createProgressDialog
import org.aacnylt.camphubonline.utils.StaticScoutService.createRetrofitService
import org.aacnylt.camphubonline.models.Scout
import org.aacnylt.camphubonline.utils.StaticScoutService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    lateinit var loginContainer: CoordinatorLayout

    var useCustomServer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadServerAddress()
        loginContainer = findViewById<CoordinatorLayout>(R.id.LoginContainer)
        setSupportActionBar(findViewById(R.id.loginbar))
        val loginButton = findViewById<Button>(R.id.login)
        val usernameField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)
        val serverField = findViewById<AutoCompleteTextView>(R.id.server)
        val serverListAdapter = ArrayAdapter<String>(this@Login, android.R.layout.simple_list_item_1, serverList.toList())
        serverField.setAdapter(serverListAdapter)
        loginButton.setOnClickListener {
            startLogin(usernameField, passwordField, serverField)
        }
        serverField.setImeActionLabel(getString(R.string.login), KeyEvent.KEYCODE_ENTER)
        serverField.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                startLogin(usernameField, passwordField, serverField)
                return true
            }
        })
        passwordField.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                startLogin(usernameField, passwordField, serverField)
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.loginmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.loginabout -> {
                val version = packageManager.getPackageInfo(packageName, 0).versionName
                val builder = AlertDialog.Builder(this@Login)
                builder.setIcon(R.drawable.ic_help_dark)
                builder.setTitle(getString(R.string.about))
                builder.setMessage("NYLT CampHub Online by Aroon Narayanan - Version " + version)
                builder.create().show()
            }
            R.id.logincustomserver -> {
                useCustomServer = !useCustomServer
                item.isChecked = useCustomServer
                findViewById<EditText>(R.id.server).visibility = if(useCustomServer) EditText.VISIBLE else EditText.GONE
            }
        }
        return true
    }

    private fun startLogin(usernameField: EditText, passwordField: EditText, serverField: AutoCompleteTextView) {
        if (usernameField.text.isNotEmpty() && passwordField.text.isNotEmpty() && checkServerFieldEmptiness(serverField)) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus.windowToken, 0)
            val progressDialog = createProgressDialog(this@Login, "Logging in...")
            progressDialog.show()
            StaticScoutService.hostUrl = if (useCustomServer) formatServerURL(serverField.text.toString()) else "http://nyltcamphub.azurewebsites.net"
            createRetrofitService().authenticateScout(usernameField.text.toString(), passwordField.text.toString())
                    .enqueue(createCallback(progressDialog))
        }
    }

    private fun checkServerFieldEmptiness(serverField: AutoCompleteTextView): Boolean {
        if (useCustomServer) {
            return serverField.text.isNotEmpty()
        } else {
            return true
        }
    }

    private fun formatServerURL(url: String): String {
        if (url.contains("http://"))
            return url
        return "http://" + url
    }

    private fun createCallback(progressDialog: ProgressDialog): Callback<Scout> {
        return object : Callback<Scout> {
            override fun onResponse(call: Call<Scout>, response: Response<Scout>) {
                progressDialog.dismiss()
                if (response.body() != null) {
                    StaticScoutService.CurrentUser = response.body()!!
                    saveServerAddress(StaticScoutService.hostUrl)
                    val intent = Intent(this@Login, ScoutGrid::class.java)
                    startActivity(intent)
                } else {
                    Snackbar.make(loginContainer, "Incorrect username or password.", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Scout>, t: Throwable) {
                progressDialog.dismiss()
                Snackbar.make(loginContainer, "Unable to reach CampHub server - check your internet connection and try again.", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    var serverList = HashSet<String>()

    fun saveServerAddress(url: String) {
        serverList.add(url)
        val prefs = this.getSharedPreferences("campHubServers", Context.MODE_PRIVATE) ?: return
        with(prefs.edit()) {
            putStringSet("serverlist", serverList)
            commit()
        }
    }

    fun loadServerAddress() {
        val prefs = this.getSharedPreferences("campHubServers", Context.MODE_PRIVATE) ?: return
        serverList = prefs.getStringSet("serverlist", HashSet<String>()) as HashSet<String>
    }
}

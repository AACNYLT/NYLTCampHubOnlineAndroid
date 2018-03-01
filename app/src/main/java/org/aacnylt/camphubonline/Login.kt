package org.aacnylt.camphubonline

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import org.aacnylt.camphubonline.StaticScoutService.createProgressDialog
import org.aacnylt.camphubonline.StaticScoutService.createRetrofitService
import org.aacnylt.camphubonline.models.Scout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginButton = findViewById(R.id.login) as Button
        val usernameField = findViewById(R.id.username) as EditText
        val passwordField = findViewById(R.id.password) as EditText
        val serverField = findViewById(R.id.server) as EditText
        loginButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val progressDialog = createProgressDialog(this@Login, "Logging in...")
                progressDialog.show()
                StaticScoutService.hostUrl = formatServerURL(serverField.text.toString())
                createRetrofitService().authenticateScout(usernameField.text.toString(), passwordField.text.toString())
                        .enqueue(createCallback(progressDialog))
            }
        })
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
                    val intent = Intent(this@Login, ScoutGrid::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Unsuccessful login.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Scout>, t: Throwable) {
                Log.e("getFailure", t.message, t)
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}

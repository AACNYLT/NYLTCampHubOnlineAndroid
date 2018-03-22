package org.aacnylt.camphubonline

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.EditText
import org.aacnylt.camphubonline.models.Scout

class ScoutEditActivity : AppCompatActivity() {

    lateinit var currentScout: Scout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scout_edit)
        currentScout = intent.getSerializableExtra("scout") as Scout
        val toolbar = findViewById<Toolbar>(R.id.scouteditbar)
        toolbar.title = currentScout.toString()
        setSupportActionBar(toolbar)
        populateFields()
    }

    fun populateFields() {
        findViewById<EditText>(R.id.ScoutEditFirstName).setText(currentScout.FirstName)
        findViewById<EditText>(R.id.ScoutEditLastName).setText(currentScout.LastName)

    }
}

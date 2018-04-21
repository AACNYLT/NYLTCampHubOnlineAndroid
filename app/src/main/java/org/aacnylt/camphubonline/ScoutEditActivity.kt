package org.aacnylt.camphubonline

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import org.aacnylt.camphubonline.models.Message
import org.aacnylt.camphubonline.models.Scout
import org.aacnylt.camphubonline.utils.StaticScoutService
import org.aacnylt.camphubonline.utils.StaticScoutService.createRetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        findViewById<FloatingActionButton>(R.id.ScoutEditSave).setOnClickListener {
            val progressDialog = StaticScoutService.createProgressDialog(this@ScoutEditActivity, "Saving...")
            progressDialog.show()
            scrapeFields()
            createRetrofitService().updateScout(currentScout.ScoutID, currentScout).enqueue(object : Callback<Message> {
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    progressDialog.hide()
                    Snackbar.make(findViewById<CoordinatorLayout>(R.id.ScoutEditContainer), response.body().toString(), Snackbar.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    progressDialog.hide()
                    Log.e("getFailure", t.message, t)
                    Snackbar.make(findViewById<CoordinatorLayout>(R.id.ScoutEditContainer), t.toString(), Snackbar.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun populateFields() {
        findViewById<EditText>(R.id.ScoutEditFirstName).setText(currentScout.FirstName)
        findViewById<EditText>(R.id.ScoutEditLastName).setText(currentScout.LastName)
        findViewById<EditText>(R.id.ScoutEditCourseID).setText(currentScout.CourseID.toString())
        findViewById<TextView>(R.id.ScoutEditScoutID).setText(currentScout.ScoutID.toString())
        findViewById<EditText>(R.id.ScoutEditBSAID).setText(currentScout.BSAID.toString())
        findViewById<EditText>(R.id.ScoutEditCourseName).setText(currentScout.CourseName)
        findViewById<EditText>(R.id.ScoutEditGender).setText(currentScout.Gender)
        findViewById<EditText>(R.id.ScoutEditPosition).setText(currentScout.Position)
        findViewById<EditText>(R.id.ScoutEditPwd).setText(currentScout.Password)
        findViewById<EditText>(R.id.ScoutEditTeam).setText(currentScout.Team)
        findViewById<EditText>(R.id.ScoutEditUsername).setText(currentScout.Username)
        findViewById<TextView>(R.id.ScoutEditCreated).setText(getString(R.string.created_formatted).replace("%s1", currentScout.Created.toString()))
        findViewById<TextView>(R.id.ScoutEditLastModified).setText(getString(R.string.lastmodified_formatted).replace("%s1", currentScout.LastModified.toString()))
        findViewById<Switch>(R.id.ScoutEditIsAdmin).isChecked = currentScout.IsAdmin ?: false
        findViewById<Switch>(R.id.ScoutEditIsElevated).isChecked = currentScout.IsElevated ?: false
        findViewById<Switch>(R.id.ScoutEditIsStaff).isChecked = currentScout.IsStaff ?: false
        findViewById<Switch>(R.id.ScoutEditIsYouth).isChecked = currentScout.IsYouth ?: false
    }

    private fun scrapeFields() {
        scrapeStringField("FirstName", R.id.ScoutEditFirstName)
        scrapeStringField("LastName", R.id.ScoutEditLastName)
        scrapeIntField("CourseID", R.id.ScoutEditCourseID)
        scrapeIntField("BSAID", R.id.ScoutEditBSAID)
        scrapeStringField("CourseName", R.id.ScoutEditCourseName)
        scrapeStringField("Gender", R.id.ScoutEditGender)
        scrapeStringField("Position", R.id.ScoutEditPosition)
        scrapeStringField("Password", R.id.ScoutEditPwd)
        scrapeStringField("Team", R.id.ScoutEditTeam)
        scrapeStringField("Username", R.id.ScoutEditUsername)
        scrapeBooleanField("IsAdmin", R.id.ScoutEditIsAdmin)
        scrapeBooleanField("IsElevated", R.id.ScoutEditIsElevated)
        scrapeBooleanField("IsStaff", R.id.ScoutEditIsStaff)
        scrapeBooleanField("IsYouth", R.id.ScoutEditIsYouth)
    }

    private fun checkUnchangedNull(property: Any?, field: EditText): Boolean {
        return (property == null) && (field.text.isEmpty())
    }

    private fun checkUnchangedNullBool(property: Any?, field: Switch): Boolean {
        return (property == null) && (!field.isChecked)
    }

    private fun scrapeStringField(propertyName: String, fieldName: Int) {
        val propertyValue = currentScout.getStringProperty(propertyName)
        val field = findViewById<EditText>(fieldName)
        val fieldValue = field.text.toString()
        if (!checkUnchangedNull(propertyValue, field)) {
            when (propertyName) {
                "FirstName" -> currentScout.FirstName = fieldValue
                "LastName" -> currentScout.LastName = fieldValue
                "CourseName" -> currentScout.CourseName = fieldValue
                "Gender" -> currentScout.Gender = fieldValue
                "Position" -> currentScout.Position = fieldValue
                "Password" -> currentScout.Password = fieldValue
                "Team" -> currentScout.Team = fieldValue
                "Username" -> currentScout.Username = fieldValue
            }
        }
    }

    private fun scrapeIntField(propertyName: String, fieldName: Int) {
        val propertyValue = currentScout.getIntProperty(propertyName)
        val field = findViewById<EditText>(fieldName)
        val fieldValue = field.text.toString().toInt()
        if (!checkUnchangedNull(propertyValue, field)) {
            when (propertyName) {
                "CourseID" -> currentScout.CourseID = fieldValue
                "ScoutID" -> currentScout.ScoutID = fieldValue
                "BSAID" -> currentScout.BSAID = fieldValue
            }
        }
    }

    private fun scrapeBooleanField(propertyName: String, fieldName: Int) {
        val propertyValue = currentScout.getBooleanProperty(propertyName)
        val field = findViewById<Switch>(fieldName)
        val fieldValue = field.isChecked
        if (!checkUnchangedNullBool(propertyValue, field)) {
            when (propertyName) {
                "IsAdmin" -> currentScout.IsAdmin = fieldValue
                "IsStaff" -> currentScout.IsStaff = fieldValue
                "IsElevated" -> currentScout.IsElevated = fieldValue
                "IsYouth" -> currentScout.IsYouth = fieldValue
            }
        }
    }
}

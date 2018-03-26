package org.aacnylt.camphubonline

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
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

    private fun populateFields() {
        findViewById<EditText>(R.id.ScoutEditFirstName).setText(currentScout.FirstName)
        findViewById<EditText>(R.id.ScoutEditLastName).setText(currentScout.LastName)
        findViewById<EditText>(R.id.ScoutEditCourseID).setText(currentScout.CourseID.toString())
        findViewById<EditText>(R.id.ScoutEditScoutID).setText(currentScout.ScoutID.toString())
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
        scrapeStringField("FirstName", currentScout.FirstName, R.id.ScoutEditFirstName)
        scrapeStringField("LastName", currentScout.LastName, R.id.ScoutEditLastName)
    }

    private fun checkUnchangedNull(property: Any?, field: EditText): Boolean {
        return (property == null) && (field.text.isEmpty())
    }

    private fun checkUnchangedNullBool(property: Any?, field: Switch): Boolean {
        return (property == null) && (!field.isChecked)
    }

    private fun scrapeStringField(propertyName: String, propertyValue: String?, fieldName: Int) {
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

    private fun scrapeIntField(propertyName: String, propertyValue: Int?, fieldName: Int) {
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

    private fun scrapeBooleanField(propertyName: String, propertyValue: Boolean?, fieldName: Int) {
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

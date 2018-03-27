package org.aacnylt.camphubonline.models

import android.net.Uri
import org.aacnylt.camphubonline.utils.StaticScoutService
import java.io.Serializable
import java.net.URI
import java.util.*

/**
 * Created by Aroon on 2/24/2018.
 */

class Scout(
        var FirstName: String?,
        var LastName: String?,
        var DateOfBirth: String?, // FIX THIS NONSENSE
        var Gender: String?,
        var ScoutID: Int?,
        var BSAID: Int?,
        var Username: String?,
        var Password: String?,
        var Position: String?,
        var Team: String?,
        var IsAdmin: Boolean?,
        var IsYouth: Boolean?,
        var IsStaff: Boolean?,
        var IsElevated: Boolean?,
        var CourseName: String?,
        var CourseID: Int?,
        var LastModified: Date?,
        var Created: Date?
) : Serializable {
    override fun toString(): String {
        return FirstName + " " + LastName
    }

    fun imageUrl(): String {
        return Uri.parse(StaticScoutService.hostUrl).buildUpon()
                .appendPath("scouts")
                .appendPath(ScoutID.toString())
                .appendPath("image")
                .build().toString()
    }

    fun getStringProperty(propertyName: String): String? {
        when (propertyName) {
            "FirstName" -> return FirstName
            "LastName" -> return LastName
            "CourseName" -> return CourseName
            "Gender" -> return Gender
            "Position" -> return Position
            "Password" -> return Password
            "Team" -> return Team
            "Username" -> return Username
        }
        return null
    }

    fun getIntProperty(propertyName: String): Int? {
        when (propertyName) {
            "CourseID" -> return CourseID
            "ScoutID" -> return ScoutID
            "BSAID" -> return BSAID
        }
        return null
    }

    fun getBooleanProperty(propertyName: String): Boolean? {
        when (propertyName) {
            "IsAdmin" -> return IsAdmin
            "IsStaff" -> return IsStaff
            "IsElevated" -> return IsElevated
            "IsYouth" -> return IsYouth
        }
        return null
    }
}
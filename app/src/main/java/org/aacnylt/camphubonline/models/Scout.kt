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
        var LastModified: Date?,
        var Created: Date?
): Serializable {
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
}
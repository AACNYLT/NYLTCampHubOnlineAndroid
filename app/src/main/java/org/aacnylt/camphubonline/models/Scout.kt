package org.aacnylt.camphubonline.models

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
) {
    override fun toString(): String {
        return FirstName + " " + LastName
    }
}
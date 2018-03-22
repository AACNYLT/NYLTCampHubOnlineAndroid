package org.aacnylt.camphubonline.models

import java.util.*

/**
 * Created by Aroon on 3/21/2018.
 */
class Course(
        var UnitName: String?,
        var CourseID: Int?,
        var StartDate: Date?
) {
    override fun toString(): String {
        return UnitName ?: ""
    }
}
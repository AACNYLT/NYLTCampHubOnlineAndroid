package org.aacnylt.camphubonline.models

import java.io.Serializable
import java.util.*

/**
 * Created by Aroon on 3/11/2018.
 */
class Evaluation(
        var EvalID: String?,
        var ScoutID: Int?,
        var Knowledge: Double?,
        var Skill: Double?,
        var Confidence: Double?,
        var Enthusiasm: Double?,
        var Motivation: Double?,
        var Comments: String?,
        var Day: String?,
        var EvaluatorID: Int?,
        var EvaluatorPosition: String?,
        var IsFinal: Boolean?,
        var Created: Date?,
        var LastModified: Date?,
        var LastModifiedBy: Int?,
        var Recommend: Int?
): Serializable
package org.aacnylt.camphubonline

import android.app.Dialog
import android.app.DialogFragment
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

class EvaluationFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_evaluation, container)
        return view
    }

    override fun onResume() {
        super.onResume()
        val size = Point()
        dialog.window.windowManager.defaultDisplay.getSize(size)
        var width = (size.x * 0.8).toInt()
        width = if (width < 300) 300 else width
        dialog.window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity, theme) {
            override fun onBackPressed() {
                this@EvaluationFragment.dismiss()
            }
        }
    }
}
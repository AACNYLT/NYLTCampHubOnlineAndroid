package org.aacnylt.camphubonline

import android.app.Dialog
import android.app.DialogFragment
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import org.aacnylt.camphubonline.models.Evaluation
import org.aacnylt.camphubonline.utils.StaticScoutService

class EvaluationFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_evaluation, container)
        val knowledgeSlider = view.findViewById<SeekBar>(R.id.EvalFragKnowledge)
        val knowledgeLabel = view.findViewById<TextView>(R.id.EvalFragLabelKnowledge)
        val skillSlider = view.findViewById<SeekBar>(R.id.EvalFragSkill)
        val skillLabel = view.findViewById<TextView>(R.id.EvalFragLabelSkill)
        val confidenceSlider = view.findViewById<SeekBar>(R.id.EvalFragConfidence)
        val confidenceLabel = view.findViewById<TextView>(R.id.EvalFragLabelConfidence)
        val motivationSlider = view.findViewById<SeekBar>(R.id.EvalFragMotivation)
        val motivationLabel = view.findViewById<TextView>(R.id.EvalFragLabelMotivation)
        val enthusiasmSlider = view.findViewById<SeekBar>(R.id.EvalFragEnthusiasm)
        val enthusiasmLabel = view.findViewById<TextView>(R.id.EvalFragLabelEnthusiasm)
        knowledgeLabel.text = adjustSliderNumber(knowledgeSlider.progress).toString()
        skillLabel.text = adjustSliderNumber(skillSlider.progress).toString()
        confidenceLabel.text = adjustSliderNumber(confidenceSlider.progress).toString()
        motivationLabel.text = adjustSliderNumber(motivationSlider.progress).toString()
        enthusiasmLabel.text = adjustSliderNumber(enthusiasmSlider.progress).toString()
        knowledgeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                knowledgeLabel.text = adjustSliderNumber(progress).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        skillSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                skillLabel.text = adjustSliderNumber(progress).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        confidenceSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                confidenceLabel.text = adjustSliderNumber(progress).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        motivationSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                motivationLabel.text = adjustSliderNumber(progress).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        enthusiasmSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                enthusiasmLabel.text = adjustSliderNumber(progress).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        view.findViewById<Button>(R.id.EvalFragSave).setOnClickListener {
//            val progressDialog = StaticScoutService.createProgressDialog(this.context, "Saving evaluation...")
//            progressDialog.show()
        }
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

    fun adjustSliderNumber(sliderNumber: Int): Double {
        return (sliderNumber + 2).toDouble() / 2
    }
}
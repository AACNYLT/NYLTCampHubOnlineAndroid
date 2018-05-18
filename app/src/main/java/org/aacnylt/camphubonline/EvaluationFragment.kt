package org.aacnylt.camphubonline

import android.app.Dialog
import android.app.DialogFragment
import android.app.ProgressDialog
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import org.aacnylt.camphubonline.models.Evaluation
import org.aacnylt.camphubonline.models.Message
import org.aacnylt.camphubonline.models.Scout
import org.aacnylt.camphubonline.utils.StaticScoutService
import org.aacnylt.camphubonline.utils.StaticScoutService.CurrentUser
import org.aacnylt.camphubonline.utils.StaticScoutService.createRetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EvaluationFragment : DialogFragment() {

    lateinit var currentScout: Scout
    var isFinal = false
    lateinit var onSaveAction: () -> Unit

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
        val day = view.findViewById<EditText>(R.id.EvalFragDay)
        val sliderLayout = view.findViewById<LinearLayout>(R.id.EvalFragSliders)
        if (isFinal) {
            day.setText(R.string.finalCaps)
            day.isEnabled = false
            sliderLayout.visibility = GONE
        }
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
            val progressDialog = StaticScoutService.createProgressDialog(this.context, "Saving evaluation...")
            progressDialog.show()
            saveNewEval(createEval(view), progressDialog)
        }
        return view
    }

    companion object {
        fun newInstance(isFinal: Boolean, currentScout: Scout): EvaluationFragment {
            val fragment = EvaluationFragment()
            val args = Bundle()
            args.putBoolean("isFinal", isFinal)
            args.putSerializable("currentScout", currentScout)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentScout = arguments.getSerializable("currentScout") as Scout
        isFinal = arguments.getBoolean("isFinal")
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

    fun createEval(view: View): Evaluation {
        val knowledgeSlider = view.findViewById<SeekBar>(R.id.EvalFragKnowledge)
        val skillSlider = view.findViewById<SeekBar>(R.id.EvalFragSkill)
        val confidenceSlider = view.findViewById<SeekBar>(R.id.EvalFragConfidence)
        val motivationSlider = view.findViewById<SeekBar>(R.id.EvalFragMotivation)
        val enthusiasmSlider = view.findViewById<SeekBar>(R.id.EvalFragEnthusiasm)
        val comments = view.findViewById<EditText>(R.id.EvalFragComments)
        val day = view.findViewById<EditText>(R.id.EvalFragDay)
        return Evaluation(StaticScoutService.createUniqueID(currentScout.ScoutID.toString()), currentScout.ScoutID, adjustSliderNumber(knowledgeSlider.progress), adjustSliderNumber(skillSlider.progress), adjustSliderNumber(confidenceSlider.progress), adjustSliderNumber(enthusiasmSlider.progress), adjustSliderNumber(motivationSlider.progress), comments.text.toString(), day.text.toString(), CurrentUser.ScoutID, CurrentUser.Position, isFinal, Calendar.getInstance().time, Calendar.getInstance().time, CurrentUser.ScoutID, getRecommendValue(view))
    }

    fun saveNewEval(evaluation: Evaluation, progressDialog: ProgressDialog) {
        createRetrofitService().insertEvaluation(evaluation).enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                progressDialog.dismiss()
                this@EvaluationFragment.dialog.dismiss()
                onSaveAction()
            }

            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                Toast.makeText(this@EvaluationFragment.context, t.toString(), Toast.LENGTH_LONG).show()

            }
        })
    }

    fun getRecommendValue(view: View): Int {
        val yes = view.findViewById<RadioButton>(R.id.EvalFragYesRecommend)
        val maybe = view.findViewById<RadioButton>(R.id.EvalFragMaybeRecommend)
        val no = view.findViewById<RadioButton>(R.id.EvalFragNoRecommend)
        if (yes.isChecked) {
            return 3
        } else if (maybe.isChecked) {
            return 2
        }
        return 1
    }
}
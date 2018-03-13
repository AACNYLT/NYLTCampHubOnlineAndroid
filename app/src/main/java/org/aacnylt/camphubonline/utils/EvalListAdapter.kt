package org.aacnylt.camphubonline.utils

import android.content.Context
import android.provider.Settings.Global.getString
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.TextView
import org.aacnylt.camphubonline.R
import org.aacnylt.camphubonline.models.Evaluation
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Aroon on 3/11/2018.
 */
class EvalListAdapter(private val evalList: ArrayList<Evaluation>, private val context: Context, private val listener: (Evaluation) -> Unit) : RecyclerView.Adapter<EvalListAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.evaluation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eval = evalList[position]
        val df = SimpleDateFormat("MM/dd/yyyy HH:mm a", Locale.US)
        holder.view.findViewById<TextView>(R.id.EvalItemHeader).text = context.getString(R.string.eval_header).replace("%s1", eval.Day ?: "").replace("%s2", eval.EvaluatorPosition ?: "")
        if (eval.Created != null) {
            holder.view.findViewById<TextView>(R.id.EvalItemTime).text = df.format(eval.Created)
        }
        holder.view.findViewById<ProgressBar>(R.id.KnowledgeItem).progress = ((eval.Knowledge ?: 0.0) * 20).toInt()
        holder.view.findViewById<ProgressBar>(R.id.SkillItem).progress = ((eval.Skill ?: 0.0) * 20).toInt()
        holder.view.findViewById<ProgressBar>(R.id.ConfidenceItem).progress = ((eval.Confidence ?: 0.0) * 20).toInt()
        holder.view.findViewById<ProgressBar>(R.id.MotivationItem).progress = ((eval.Motivation ?: 0.0) * 20).toInt()
        holder.view.findViewById<ProgressBar>(R.id.EnthusiamItem).progress = ((eval.Enthusiasm ?: 0.0) * 20).toInt()
        holder.view.findViewById<TextView>(R.id.EvalItemRecommend).text = context.getString(R.string.eval_recommend).replace("%s1", getRecommendation(eval))
        holder.view.setOnClickListener {listener(eval)}
    }

    private fun getRecommendation(eval: Evaluation): String {
        when (eval.Recommend) {
            1 -> return "no"
            2 -> return "maybe"
            3 -> return "yes"
        }
        return "n/a"
    }

    override fun getItemCount() = evalList.size
}
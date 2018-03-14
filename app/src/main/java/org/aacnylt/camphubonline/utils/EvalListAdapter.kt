package org.aacnylt.camphubonline.utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import org.aacnylt.camphubonline.R
import org.aacnylt.camphubonline.models.Evaluation
import org.aacnylt.camphubonline.utils.StaticScoutService.CurrentUser
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Aroon on 3/11/2018.
 */
class EvalListAdapter(private val evalList: ArrayList<Evaluation>, private val context: Context, private val clickListener: (Evaluation) -> Unit, private val longPressListener: (Evaluation, View) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    private val TYPE_ITEM = 0
    private val TYPE_HEADER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.evaluation_item_header, parent, false)
            return HeaderViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.evaluation_item, parent, false)
            return ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val eval = evalList[position - 1]
            val df = SimpleDateFormat("MM/dd/yyyy HH:mm a", Locale.US)
            holder.view.findViewById<TextView>(R.id.EvalItemHeader).text = context.getString(R.string.eval_header).replace("%s1", eval.Day ?: "").replace("%s2", eval.EvaluatorPosition ?: "")
            if (eval.Created != null) {
                holder.view.findViewById<TextView>(R.id.EvalItemTime).text = df.format(eval.Created)
            }
            if (eval.EvaluatorID == CurrentUser.ScoutID) {
                holder.view.findViewById<TextView>(R.id.EvalItemCommentLabel).text = context.getString(R.string.hold_edit)
            }
            holder.view.findViewById<ProgressBar>(R.id.KnowledgeItem).progress = ((eval.Knowledge ?: 0.0) * 20).toInt()
            holder.view.findViewById<ProgressBar>(R.id.SkillItem).progress = ((eval.Skill ?: 0.0) * 20).toInt()
            holder.view.findViewById<ProgressBar>(R.id.ConfidenceItem).progress = ((eval.Confidence ?: 0.0) * 20).toInt()
            holder.view.findViewById<ProgressBar>(R.id.MotivationItem).progress = ((eval.Motivation ?: 0.0) * 20).toInt()
            holder.view.findViewById<ProgressBar>(R.id.EnthusiamItem).progress = ((eval.Enthusiasm ?: 0.0) * 20).toInt()
            holder.view.findViewById<TextView>(R.id.EvalItemRecommend).text = context.getString(R.string.eval_recommend).replace("%s1", getRecommendation(eval))
            holder.view.setOnClickListener { clickListener(eval) }
            holder.view.setOnLongClickListener { longPressListener(eval, holder.view); true }
        } else if (holder is HeaderViewHolder) {

        }
    }

    private fun getRecommendation(eval: Evaluation): String {
        when (eval.Recommend) {
            1 -> return "no"
            2 -> return "maybe"
            3 -> return "yes"
        }
        return "n/a"
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEADER
        }
        return TYPE_ITEM
    }

    override fun getItemCount() = evalList.size + 1
}
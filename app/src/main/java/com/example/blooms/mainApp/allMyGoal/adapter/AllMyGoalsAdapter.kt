package com.example.blooms.mainApp.allMyGoal.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import at.grabner.circleprogress.CircleProgressView
import com.example.blooms.R
import com.example.blooms.general.Constance
import com.example.blooms.model.Category
import com.example.blooms.model.Goal
import com.example.blooms.model.GoalStep
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class AllMyGoalsAdapter(private val context: Context
, private val goals: List<Goal>, private val onItemClick: (Goal) -> Unit ) :
        RecyclerView.Adapter<AllMyGoalsAdapter.GridViewHolder>() {

        class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val shareToggle: SwitchCompat = view.findViewById(R.id.goal_share_toggle)
            val category: AppCompatTextView = view.findViewById(R.id.goal_category)
            val title: AppCompatTextView = view.findViewById(R.id.goal_title)
            val circleProgressView : CircleProgressView = view.findViewById(R.id.goal_circleProgressView)
            val countdown: AppCompatTextView = view.findViewById(R.id.goal_countdownTextView)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_goal_item, parent, false)
            return GridViewHolder(view)
        }

        override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
            val item = goals[position]
            holder.itemView.setOnClickListener {
                onItemClick.invoke(item)
            }

            holder.shareToggle.isChecked = item.shareGoal
            holder.category.text = getCategoryById(item.categoryId)?.name ?:""
            holder.title.text = item.title
            setCircleProgressView(holder.circleProgressView, item.goalStep)
            holder.countdown.text = startCountdown(item.deadlineDate)
        }

        private fun getCategoryById(id: Int): Category? {
            return Constance.categories.find{ it.id == id }
        }

        private fun setCircleProgressView(circleProgressView: CircleProgressView
                                          , goalStep: ArrayList<GoalStep>)  {
            circleProgressView.blockCount = goalStep.size
            val checkedCount = goalStep.count { it.isChecked }
            circleProgressView.setText("$checkedCount/${goalStep.size}")
            circleProgressView.setValueAnimated((5/goalStep.size.toFloat())*checkedCount)
        }

    private fun startCountdown(date : String) : String {
        val timeLeft = getTimeLeft(date)
        return timeLeft
    }

    @SuppressLint("DefaultLocale")
    private fun getTimeLeft(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val givenDate = LocalDate.parse(date, formatter)
        val today = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(today, givenDate)
        if (daysBetween <= 0) return "00M 00W 00D"
        val period = Period.between(today, givenDate)
        println("Difference: ${period.years} years, ${period.months} months, and ${period.days} days")
        return String.format(
            "%02dY %02dM %02dD",
            period.years, period.months, period.days
        )
    }

        override fun getItemCount(): Int = goals.size
        }


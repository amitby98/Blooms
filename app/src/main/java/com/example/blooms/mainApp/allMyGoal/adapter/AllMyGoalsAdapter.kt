package com.example.blooms.mainApp.allMyGoal.adapter

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.general.Constance
import com.example.blooms.model.Category
import com.example.blooms.model.Goal
import com.example.blooms.model.GoalStep
import com.google.android.material.progressindicator.LinearProgressIndicator
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AllMyGoalsAdapter(
    private val context: Context,
    private val goals: List<Goal>,
    private val onItemClick: (Goal) -> Unit
) : RecyclerView.Adapter<AllMyGoalsAdapter.GridViewHolder>() {

    private val countdownTimers = mutableMapOf<Int, CountDownTimer>()

    class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: AppCompatTextView = view.findViewById(R.id.goal_category)
        val title: AppCompatTextView = view.findViewById(R.id.goal_title)
        val progressBar: LinearProgressIndicator = view.findViewById(R.id.goal_progress_bar)
        val countdown: AppCompatTextView = view.findViewById(R.id.goal_countdownTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_goal_item, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = goals[position]
        countdownTimers[position]?.cancel()
        val timer = createCountdownTimer(item.deadlineDate, holder.countdown)
        timer.start()
        countdownTimers[position] = timer

        // Rest of the existing code
        holder.itemView.setOnClickListener {
            onItemClick.invoke(item)
        }

        holder.category.text = getCategoryById(item.categoryId)?.name ?: ""
        holder.title.text = item.title
        setProgressBar(holder.progressBar, item.goalStep)
    }

    private fun createCountdownTimer(deadline: String, countdownView: AppCompatTextView): CountDownTimer {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val givenDate = LocalDate.parse(deadline, formatter)

        // Calculate milliseconds until deadline
        val now = LocalDateTime.now()
        val deadlineDateTime = givenDate.atStartOfDay()

        // If deadline is in the past, return a timer that immediately finishes
        if (now.isAfter(deadlineDateTime)) {
            return object : CountDownTimer(0, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    countdownView.text = "00 : 00 : 00"
                }
            }
        }

        val millisToCountdown = ChronoUnit.MILLIS.between(now, deadlineDateTime)

        return object : CountDownTimer(millisToCountdown, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = millisUntilFinished / (60 * 60 * 1000)
                val minutes = (millisUntilFinished % (60 * 60 * 1000)) / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000

                val formattedTime = String.format(
                    "%02d : %02d : %02d",
                    hours, minutes, seconds
                )
                countdownView.text = formattedTime
            }

            override fun onFinish() {
                // When timer ends, display 00:00:00
                countdownView.text = "00 : 00 : 00"
            }
        }
    }

    private fun getCategoryById(id: Int): Category? {
        return Constance.categories.find { it.id == id }
    }

    private fun setProgressBar(progressBar: LinearProgressIndicator, goalStep: ArrayList<GoalStep>) {
        val totalSteps = goalStep.size
        val checkedCount = goalStep.count { it.isChecked }
        val progressPercentage = if (totalSteps > 0) {
            (checkedCount.toFloat() / totalSteps) * 100
        } else {
            0f
        }
        val progressColor = when {
            progressPercentage == 0f -> R.color.progress_red
            progressPercentage < 25f -> R.color.progress_red
            progressPercentage < 50f -> R.color.progress_orange
            progressPercentage < 75f -> R.color.progress_yellow
            progressPercentage < 100f -> R.color.progress_light_green
            else -> R.color.progress_green
        }
        progressBar.setIndicatorColor(ContextCompat.getColor(progressBar.context, progressColor))
        progressBar.progress = progressPercentage.toInt()
    }

    override fun getItemCount(): Int = goals.size
}
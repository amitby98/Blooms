package com.example.blooms.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import at.grabner.circleprogress.CircleProgressView
import com.example.blooms.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


class MainActivity2 : AppCompatActivity() {
    lateinit var circleProgressView: CircleProgressView
    private lateinit var countdownTextView: AppCompatTextView
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 1000L // Update every second
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_goal_item)
        circleProgressView = findViewById(R.id.goal_circleProgressView)
        countdownTextView = findViewById(R.id.goal_countdownTextView)

        circleProgressView.blockCount = 5
        circleProgressView.setText("2/5")
        circleProgressView.setValueAnimated((5/5f)*2)
        circleProgressView.setOnClickListener {
            1==1
        }
        startCountdown()
    }


    private fun startCountdown() {
        val targetDateString = "2030-11-23" // Target date with hour & minute
        val timeLeft = getTimeLeft(targetDateString)
        countdownTextView.text = timeLeft
    }

    private fun getTimeLeft(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
}
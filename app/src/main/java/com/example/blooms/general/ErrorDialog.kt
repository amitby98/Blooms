package com.example.blooms.general

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.lottie.LottieAnimationView
import com.example.blooms.R

class ErrorDialog(private val context: Context) {

    lateinit var lottieView : LottieAnimationView
    lateinit var titleText : AppCompatTextView
    lateinit var messageText : AppCompatTextView
    lateinit var actionButton : AppCompatButton


    fun show(
        title: String,
        message: String,
        buttonText: String,
        onButtonClick: () -> Unit = {}
    ) {
        val dialog = Dialog(context)

        val view = LayoutInflater.from(context).inflate(R.layout.general_popup, null)

        lottieView = view.findViewById<LottieAnimationView>(R.id.alertIcon)
        titleText = view.findViewById<AppCompatTextView>(R.id.popupTitle)
        messageText = view.findViewById<AppCompatTextView>(R.id.popupMessage)
        //actionButton = view.findViewById<AppCompatButton>(R.id.errorButton)

        try {
            lottieView.apply {
                setAnimation(R.raw.register_animation)
                repeatCount = 0
                speed = 1f
                playAnimation()
            }
        } catch (e: Exception) {
            lottieView.visibility = android.view.View.GONE
        }

        // Set content
        titleText.text = title
        messageText.text = message
       // actionButton.text = buttonText


        // Set button click listener
//        actionButton.setOnClickListener {
//            onButtonClick()
//            dialog.dismiss()
//        }

        // Configure dialog window
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
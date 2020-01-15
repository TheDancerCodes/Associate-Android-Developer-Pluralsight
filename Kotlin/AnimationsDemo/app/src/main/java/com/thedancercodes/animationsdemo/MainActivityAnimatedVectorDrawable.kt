package com.thedancercodes.animationsdemo

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_animated_vector_drawable.*

class MainActivityAnimatedVectorDrawable : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_animated_vector_drawable)

        /* Code to implement animation */

        // Flag to help toggle the animation
        var isChecked = true

        // setOnClickListener on ImageView
        avdImage.setOnClickListener {
            if (isChecked)
                checkToClose()
            else
                closeToCheck()

            isChecked = !isChecked
        }

    }

    private fun closeToCheck() {
        // Set animated vector as image resource
        avdImage.setImageResource(R.drawable.avd_close_to_check)

        // Get drawable from ImageView, cast it to AnimatedVectorDrawable & assign it to
        // variable avdCheckToClose
        val avdCheckToClose = avdImage.drawable as AnimatedVectorDrawable

        // Start Animation
        avdCheckToClose.start()
    }

    private fun checkToClose() {

        // Set animated vector as image resource
        avdImage.setImageResource(R.drawable.avd_check_to_close)

        // Get drawable from ImageView, cast it to AnimatedVectorDrawable & assign it to
        // variable avdCheckToClose
        val avdCheckToClose = avdImage.drawable as AnimatedVectorDrawable

        // Start Animation
        avdCheckToClose.start()

    }
}
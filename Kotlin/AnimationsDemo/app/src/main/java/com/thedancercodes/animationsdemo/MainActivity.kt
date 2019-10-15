package com.thedancercodes.animationsdemo

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Code to implement animation */

        // setOnClickListener on ImageView
        avdImage.setOnClickListener {
            checkToClose()
        }

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
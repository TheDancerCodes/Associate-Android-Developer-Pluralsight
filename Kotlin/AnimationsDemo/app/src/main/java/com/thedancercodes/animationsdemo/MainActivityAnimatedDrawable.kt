package com.thedancercodes.animationsdemo

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main_animated_drawable.*

class MainActivityAnimatedDrawable : AppCompatActivity() {

    // Define AnimationDrawable
    lateinit var batteryAnimation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_animated_drawable)
    }

    /**
     * -----
     * NOTE
     * -----
     *
     * Application views in Android are only visible when onStart() lifecycle method is executed.
     *   -> Hence Animation won't work if executed in onCreate()
     */
    override fun onStart() {
        super.onStart()
        // Set Background Resource Drawable of ImageView
        targetImage.setBackgroundResource(R.drawable.battery_animation_list)

        // Extract BackgroundResource & cast it to AnimationDrawable & assign it to our batteryAnimation
        batteryAnimation = targetImage.background as AnimationDrawable

        // Start Animation
        batteryAnimation.start()
    }

    // Button click event handler
    fun startStopAnimation(view: View) {

        if (batteryAnimation.isRunning)
            batteryAnimation.stop()
        else
            batteryAnimation.start()
    }
}
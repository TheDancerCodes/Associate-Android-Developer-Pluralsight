package com.thedancercodes.animationsdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.activity_main_transitions_without_scenes.*

class MainActivityTransitionsWithoutScenes : AppCompatActivity() {

    private var visibility = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_transitions_without_scenes)

    }

    /* Button click event handler */
    fun fadeAnimation(view: View) {

        // Define transition
        val transition = Fade()

        TransitionManager.beginDelayedTransition(sceneRootNoScene, transition)

        // Toggle visibility of target view
        txvDescription.visibility = if (visibility) View.INVISIBLE else View.VISIBLE
        visibility = !visibility // Toggle boolean value
    }

    fun slideEffect(view: View) {

        val transition = Slide(Gravity.END)
        TransitionManager.beginDelayedTransition(sceneRootNoScene, transition)

        txvDescription.visibility = if (visibility) View.INVISIBLE else View.VISIBLE
        visibility = !visibility
    }
}

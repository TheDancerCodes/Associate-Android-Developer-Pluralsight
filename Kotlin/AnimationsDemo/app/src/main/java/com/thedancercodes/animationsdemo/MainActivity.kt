package com.thedancercodes.animationsdemo

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var isDetailLayout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * NOTE: constraintLayout is the id of the ConstrainTLayout present in both
         * activity_main and activity_main_detail.
          */

        constraintLayout.setOnClickListener {
            if (isDetailLayout)
            swapFrames(R.layout.activity_main) // switch to default layout
            else
            swapFrames(R.layout.activity_main_detail) // switch to detail layout
        }
    }

    // Function to swap layouts or transition between layouts.
    private fun swapFrames(layoutId: Int){

        // Instantiate ConstraintSet object
        val constraintSet = ConstraintSet()

        // Get all the constraints of the child views of the new layout; which will replace 
        // the old one.
        constraintSet.clone(this, layoutId)

        // Adding effect to transition
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        // Animate
        TransitionManager.beginDelayedTransition(constraintLayout, transition)

        // Apply new constraints to our root layout
        constraintSet.applyTo(constraintLayout)

        // Toggle the Boolean value
        isDetailLayout = !isDetailLayout
    }
}

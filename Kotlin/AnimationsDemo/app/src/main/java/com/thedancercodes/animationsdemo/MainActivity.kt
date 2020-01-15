package com.thedancercodes.animationsdemo

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        val adapter = RecyclerAdapter(this, Landscape.data)

        // Custom adapter adds sale animation to items being loaded
        recyclerView.adapter = ScaleInAnimationAdapter(adapter)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager

        recyclerView.itemAnimator = OvershootInRightAnimator()

        // Specify duration of operations
        recyclerView.itemAnimator?.apply {
            addDuration = 500 // duration of add operation
            removeDuration = 500 // duration of delete operation.
        }
    }
}



package com.thedancercodes.animationsdemo

import android.animation.Animator
import android.animation.AnimatorInflater
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Animator.AnimatorListener {

    // Variables as Class Properties
    private var rotateAnimator: Animator? = null
    private var translateAnimator: Animator? = null
    private var scaleAnimator: Animator? = null
    private var fadeAnimator: Animator? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun rotateAnimation(view: View) {

        rotateAnimator = AnimatorInflater.loadAnimator(this, R.animator.rotate)
        rotateAnimator?.apply {
            setTarget(targetImage)
            addListener(this@MainActivity)
            start()
        }

    }

    fun scaleAnimation(view: View) {

        scaleAnimator = AnimatorInflater.loadAnimator(this, R.animator.scale)
        scaleAnimator?.apply {
            setTarget(targetImage)
            addListener(this@MainActivity)
            start()
        }
    }

    fun translateAnimation(view: View) {

        translateAnimator = AnimatorInflater.loadAnimator(this, R.animator.translate)
        translateAnimator?.apply {
            setTarget(targetImage)
            addListener(this@MainActivity)
            start()
        }
    }

    fun fadeAnimation(view: View) {

        fadeAnimator = AnimatorInflater.loadAnimator(this, R.animator.alpha)
        fadeAnimator?.apply {
            setTarget(targetImage)
            addListener(this@MainActivity)
            start()
        }
    }

    // Implementation of AnimatorListener interface
    override fun onAnimationStart(animation: Animator?) {

        if (animation == rotateAnimator)
            Toast.makeText(this, "Rotate Animation Started", Toast.LENGTH_SHORT).show()

        if (animation == scaleAnimator)
            Toast.makeText(this, "Scale Animation Started", Toast.LENGTH_SHORT).show()

        if (animation == translateAnimator)
            Toast.makeText(this, "Translate Animation Started", Toast.LENGTH_SHORT).show()

        if (animation == fadeAnimator)
            Toast.makeText(this, "Fade Animation Started", Toast.LENGTH_SHORT).show()

    }

    override fun onAnimationRepeat(animation: Animator?) {

        Toast.makeText(this, "Animation Repeated", Toast.LENGTH_SHORT).show()

    }

    override fun onAnimationEnd(animation: Animator?) {

        Toast.makeText(this, "Animation Ended", Toast.LENGTH_SHORT).show()

    }

    override fun onAnimationCancel(animation: Animator?) {

        Toast.makeText(this, "Animation Cancelled", Toast.LENGTH_SHORT).show()

    }
}

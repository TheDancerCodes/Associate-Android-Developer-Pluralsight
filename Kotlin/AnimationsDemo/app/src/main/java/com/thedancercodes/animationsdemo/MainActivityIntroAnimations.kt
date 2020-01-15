package com.thedancercodes.animationsdemo

import android.animation.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_intro_animations.*

class MainActivityIntroAnimations : AppCompatActivity(), Animator.AnimatorListener {

    // Variables as Class Properties
    private var rotateAnimator: ObjectAnimator? = null
    private var translateAnimator: ObjectAnimator? = null
    private var scaleAnimator: ObjectAnimator? = null
    private var fadeAnimator: ObjectAnimator? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_intro_animations)
    }

    fun rotateAnimation(view: View) {

        rotateAnimator = ObjectAnimator.ofFloat(targetImage, "rotation", 0.0f, -180.0f)
        rotateAnimator?.apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            addListener(this@MainActivityIntroAnimations)
            start()
        }

    }

    fun scaleAnimation(view: View) {

        scaleAnimator = ObjectAnimator.ofFloat(targetImage, "scaleX", 1.0f, 3.0f)
        scaleAnimator?.apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            addListener(this@MainActivityIntroAnimations)
            start()
        }
    }

    fun translateAnimation(view: View) {

        translateAnimator = ObjectAnimator.ofFloat(targetImage, "translationX", 0f, 200f)
        translateAnimator?.apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            addListener(this@MainActivityIntroAnimations)
            start()
        }

        // translateAnimator.cancel()
    }

    fun fadeAnimation(view: View) {

        fadeAnimator = ObjectAnimator.ofFloat(targetImage, "alpha", 1.0f, 0.0f)
        fadeAnimator?.apply {
            duration = 1500
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            addListener(this@MainActivityIntroAnimations)
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

    // AnimatorSet Button Click EventHandlers
    fun setFromXML(view: View) {

        val animator = AnimatorInflater.loadAnimator(this, R.animator.set)
        animator.apply {
            setTarget(targetImage)
            start()
        }
    }

    fun setFromCode(view: View) {

        // Root Animator Set
        val rootSet = AnimatorSet()

        // Flip Animation
        val flip = ObjectAnimator.ofFloat(targetImage, "rotationX", 0.0f, 360.0f)
        flip.duration = 500

        // Child Animator Set
        val childSet = AnimatorSet()

        // Scale Animations
        val scaleX = ObjectAnimator.ofFloat(targetImage, "scaleX", 1.0f, 1.5f)
        scaleX.duration = 500
        scaleX.interpolator = OvershootInterpolator()

        val scaleY = ObjectAnimator.ofFloat(targetImage, "scaleY", 1.0f, 1.5f)
        scaleY.duration = 500
        scaleY.interpolator = OvershootInterpolator()

        // rootSet.playSequentially(flip, childSet)
        // childSet.playTogether(scaleX, scaleY)

        // Method Chaining
        rootSet.play(flip).before(childSet)
        childSet.play(scaleX).with(scaleY)

        rootSet.start()
    }

    fun viewPropertyAnimator(view: View) {

        val vpa = targetImage.animate()

        vpa.apply {
            duration = 1000
            rotationX(360.0f)
            scaleX(1.5f)
            scaleY(1.5f)
            translationX(200.0f)
            alpha(0.5f)
            interpolator = OvershootInterpolator()
            start()
        }
    }

    // Perform animation using PropertyValuesHolder & ObjectAnimator
    fun propertyValuesHolder(view: View) {

        val rotX = PropertyValuesHolder.ofFloat("rotationX", 360f)
        val scaX = PropertyValuesHolder.ofFloat("scaleX", 1.5f)
        val scaY = PropertyValuesHolder.ofFloat("scaleY", 1.5f)

        val objA = ObjectAnimator.ofPropertyValuesHolder(targetImage, rotX, scaX, scaY)
        objA.apply {
            duration = 1000
            interpolator = OvershootInterpolator()
            start()
        }
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * Difference between ViewPropertyAnimator and PropertyValuesHolder
     * ---------------------------------------------------------------------------------------------
     *
     * For ViewPropertyAnimator:
     *   -> you don't have much control over the animation because it isn't linked to ObjectAnimator.
     *   -> You can't use any AnimatorListener or coordinate the animation events.
     *   -> You can only perform simple animations.
     *
     * For PropertyValuesHolder:
     *   -> Better control over animation events since we are using ObjectAnimator.
     *   -> Use AnimatorListener & control any event of your animantion.
     */


}

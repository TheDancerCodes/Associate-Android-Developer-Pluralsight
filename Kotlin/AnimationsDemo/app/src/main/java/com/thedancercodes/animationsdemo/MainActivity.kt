package com.thedancercodes.animationsdemo

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.thedancercodes.animationsdemo.transformers.*
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageArray = intArrayOf(
            R.drawable.first, R.drawable.second,
            R.drawable.third, R.drawable.fourth, R.drawable.fifth
        )

        // Instantiate ViewPagerAdapter
        val adapter = ViewPagerAdapter(this@MainActivity, imageArray)
        viewPager!!.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when (id) {

            R.id.zoom_out       -> setPageTransformer(ZoomOutTransformation())
            R.id.depth_page     -> setPageTransformer(DepthPageTransformation())
            R.id.vertical_flip  -> setPageTransformer(VerticalFlipTransformation())
            R.id.fade_out       -> setPageTransformer(FadeOutTransformation())
            R.id.cube_out       -> setPageTransformer(CubeOutDepthTransformation())
            R.id.hinge          -> setPageTransformer(HingeTransformation())
        }

        return super.onOptionsItemSelected(item)
    }

    // Add animation to ViewPager.
    private fun setPageTransformer(transformer: ViewPager.PageTransformer ) {
        viewPager!!.setPageTransformer(true, transformer)
    }
}




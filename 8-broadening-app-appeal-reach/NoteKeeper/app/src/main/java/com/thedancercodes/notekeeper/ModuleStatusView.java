package com.thedancercodes.notekeeper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class ModuleStatusView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;
    private float mOutlineWidth;
    private float mShapeSize;
    private float mSpacing;
    private Rect[] mModuleRectangles;
    private int mOutlineColor;
    private Paint mPaintOutline;
    private int mFillColor;
    private Paint mPaintFill;
    private float mRadius;


    public boolean[] getmModuleStatus() {
        return mModuleStatus;
    }

    public void setmModuleStatus(boolean[] mModuleStatus) {
        this.mModuleStatus = mModuleStatus;
    }

    // Boolean Array
    private boolean[] mModuleStatus;

    /* 3 Different Constructors */
    public ModuleStatusView(Context context) {
        super(context);
        init(null, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ModuleStatusView, defStyle, 0);

        a.recycle();

        /* Set up sizing values */

        // Variable specifying the width of the outline we want to draw around each of our circles.
        mOutlineWidth = 6f;

        // Size of the shapes we will draw
        mShapeSize = 144f;

        // Spacing between each of our shapes
        mSpacing = 30f;

        // Circle's radius
        mRadius = (mShapeSize - mOutlineWidth) / 2;

        // Create a list of rectangles that we'll use to position each of the circles when it comes
        // time to draw them
        setupModuleRectangles();

        // Variable to hold outline color
        mOutlineColor = Color.BLACK;

        // Paint instance we will use to draw the outlines.
        mPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOutline.setStyle(Paint.Style.STROKE);
        mPaintOutline.setStrokeWidth(mOutlineWidth);
        mPaintOutline.setColor(mOutlineColor);

        // Paint instance we will use to fill in our circles.
        mFillColor = getContext().getResources().getColor(R.color.pluralSightOrange);

        mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(mFillColor);

    }

    private void setupModuleRectangles() {

        // Store the rectangles
        mModuleRectangles = new Rect[mModuleStatus.length];

        // Loop to populate the array for our rectangles
        for (int moduleIndex=0; moduleIndex < mModuleRectangles.length; moduleIndex++) {

            // Rectangle left edge position for the module.
            int x = (int) (moduleIndex * (mShapeSize + mSpacing));

            // Rectangle top edge position for the module.
            int y = 0;

            /* Creating the Rectangle */

            // Assign our current array element a new instance of the class Rect
            mModuleRectangles[moduleIndex] = new Rect(x, y, x + (int) mShapeSize, y + (int) mShapeSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /* Positioning each of our circles in the right place */

        // for loop that counts for the indexes we need to access our mModuleRectangles array
        for (int moduleIndex=0; moduleIndex < mModuleRectangles.length; moduleIndex++) {

            // x & y coordinates of our circles center point.
            float x = mModuleRectangles[moduleIndex].centerX();
            float y = mModuleRectangles[moduleIndex].centerY();

            // Check whether the corresponding element of our mModuleStatus array is currently true.
            // We only fill in the circle for those modules that are marked as completed.
            if (mModuleStatus[moduleIndex])
                // Draw the filled in circle
                canvas.drawCircle(x, y, mRadius, mPaintFill);

            // Draw the outline circle
            canvas.drawCircle(x, y, mRadius, mPaintOutline);

        }

    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}

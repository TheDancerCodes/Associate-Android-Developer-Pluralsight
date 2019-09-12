package com.thedancercodes.notekeeper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;

import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class ModuleStatusView extends View {
    private static final int EDIT_MODE_MODULE_COUNT = 7;
    private static final int INVALID_INDEX = -1;
    private static final int SHAPE_CIRCLE = 0;
    private static final float DEFAULT_OUTLINE_WIDTH_DP = 2f;
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
    private int mMaxHorizontalModules;
    private int mShape;
    private ModuleStatusAccessibilityHelper mAccessibilityHelper;


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

    // Called when an instance of our custom view class is initially created.
    private void init(AttributeSet attrs, int defStyle) {

        // isInEditMode - check to see if our view is currently being used inside the designer.
        if (isInEditMode())
            setUpEditModeValues();

        /* Adding accessibility support to our custom view class. */
        setFocusable(true);

        // Create an instance of ModuleStatusAccessibilityHelper & pass in instance to our custom view.
        mAccessibilityHelper = new ModuleStatusAccessibilityHelper(this);

        // Inform accessibility system that this helper class provides the accessibility information
        // for our custom view.
        ViewCompat.setAccessibilityDelegate(this, mAccessibilityHelper);



        /* Use a constant number of device independent pixels that we convert to the appropriate
        * number of physical pixels for the current screen density. */

        // Get device's Display Metrics
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();

        float displayDensity = dm.density;

        float defaultOutlineWidthPixels = displayDensity * DEFAULT_OUTLINE_WIDTH_DP;


        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ModuleStatusView, defStyle, 0);

        // Variable to hold outline color attribute
        mOutlineColor = a.getColor(R.styleable.ModuleStatusView_outlineColor, Color.BLACK);

        // Variable to hold/ retrieve our module shape attribute
        mShape = a.getInt(R.styleable.ModuleStatusView_shape, SHAPE_CIRCLE);

        // Variable specifying the width of the outline we want to draw around each of our circles.
        mOutlineWidth = a.getDimension(R.styleable.ModuleStatusView_outlineWidth, defaultOutlineWidthPixels);

        // Tell the system when we are done with the TypedArray as it can be reused by the system.
        // Call recycle() method on the TypedArray once we have gotten all our attribute values.
        a.recycle();

        /* Set up sizing values */

        // Size of the shapes we will draw
        mShapeSize = 144f;

        // Spacing between each of our shapes
        mSpacing = 30f;

        // Circle's radius
        mRadius = (mShapeSize - mOutlineWidth) / 2;

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

    /* Forward callbacks to our helper class by overriding appropriate methods. */


    /**
     * Called by the view system when the focus state of this view changes.
     * When the focus change event is caused by directional navigation, direction
     * and previouslyFocusedRect provide insight into where the focus is coming from.
     * When overriding, be sure to call up through to the super class so that
     * the standard focus handling will occur.
     *
     * @param gainFocus             True if the View has focus; false otherwise.
     * @param direction             The direction focus has moved when requestFocus()
     *                              is called to give this view focus. Values are
     *                              {@link #FOCUS_UP}, {@link #FOCUS_DOWN}, {@link #FOCUS_LEFT},
     *                              {@link #FOCUS_RIGHT}, {@link #FOCUS_FORWARD}, or {@link #FOCUS_BACKWARD}.
     *                              It may not always apply, in which case use the default.
     * @param previouslyFocusedRect The rectangle, in this view's coordinate
     *                              system, of the previously focused view.  If applicable, this will be
     *                              passed in as finer grained information about where the focus is coming
     */
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        // Call corresponding method on our helper class.
        mAccessibilityHelper.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    /**
     * Dispatch a key event to the next view on the focus path. This path runs
     * from the top of the view tree down to the currently focused view. If this
     * view has focus, it will dispatch to itself. Otherwise it will dispatch
     * the next node down the focus path. This method also fires any key
     * listeners.
     *
     * @param event The key event to be dispatched.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        // First call helper class implementation dispatchKeyEvent to return true if it returned
        // the event otherwise it will return false.
        return mAccessibilityHelper.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    /**
     * Dispatch a hover event.
     * <p>
     * Do not call this method directly.
     * Call {@link #dispatchGenericMotionEvent(MotionEvent)} instead.
     * </p>
     *
     * @param event The motion event to be dispatched.
     * @return True if the event was handled by the view, false otherwise.
     */
    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {

        // Give the helper class the first chance at the event, if it doesn't handle the event,
        // then we pass the event to our super class.
        return mAccessibilityHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
    }

    // Any extra set up work necessary for when our view is being used inside the designer:
    // Provide status values to display an example of what the view might look like at runtime.
    private void setUpEditModeValues() {
        boolean[] exampleModuleValues = new boolean[EDIT_MODE_MODULE_COUNT];

        /* Having half the module status values set to true */
        int middle = EDIT_MODE_MODULE_COUNT / 2;

        for (int i=0; i < middle; i++)
            exampleModuleValues[i] = true;

        setmModuleStatus(exampleModuleValues);
    }

    // Calculates the positioning of where the module circles will be drawn.
    private void setupModuleRectangles(int width) {

        // Determine availableWidth
        int availableWidth = width - getPaddingLeft() - getPaddingRight();

        // Determine how many module can fit horizontally
        int horizontalModulesThatCanFit = (int) (availableWidth/ (mShapeSize + mSpacing));

        int maxHorizontalModules = Math.min(horizontalModulesThatCanFit, mModuleStatus.length);

        // Store the rectangles
        mModuleRectangles = new Rect[mModuleStatus.length];

        // Loop to populate the array for our rectangles
        for (int moduleIndex=0; moduleIndex < mModuleRectangles.length; moduleIndex++) {

            // Determine which column & row to position each module.
            int column = moduleIndex % maxHorizontalModules;
            int row = moduleIndex / maxHorizontalModules;

            // Rectangle left edge position for the module.
            int x = getPaddingLeft () + (int) (column * (mShapeSize + mSpacing));

            // Rectangle top edge position for the module.
            int y = getPaddingTop() + (int) (row * (mShapeSize + mSpacing));

            /* Creating the Rectangle */

            // Assign our current array element a new instance of the class Rect
            mModuleRectangles[moduleIndex] = new Rect(x, y, x + (int) mShapeSize, y + (int) mShapeSize);
        }
    }

    /**
     * Measure the view and its content to determine the measured width and the
     * measured height. This method is invoked by {@link #measure(int, int)} and
     * should be overridden by subclasses to provide accurate and efficient
     * measurement of their contents.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /* Calculating the desired width & height for our custom view */
        int desiredWidth = 0;
        int desiredHeight = 0;

        /* Calculations to determine mMaxHorizontalModules */

        // Check value passed into widthMeasureSpec parameter
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);

        int availableWidth = specWidth - getPaddingLeft() - getPaddingRight();

        // Determine how many module circles will fit into the width
        int horizontalModulesThatCanFit = (int) (availableWidth / (mShapeSize + mSpacing));

        mMaxHorizontalModules = Math.min(horizontalModulesThatCanFit, mModuleStatus.length);


        // Width required to draw each of the module circles & the spaces between each of the circles.
        desiredWidth = (int) ((mMaxHorizontalModules * (mShapeSize + mSpacing)) - mSpacing);

        // Add padding to the desiredWidth
        desiredWidth += getPaddingLeft() + getPaddingRight();

        // Determine number of rows required to draw all our modules.
        int rows = ((mModuleStatus.length - 1) / mMaxHorizontalModules) + 1;

        desiredHeight = (int) ((rows * (mShapeSize + mSpacing)) - mSpacing);
        desiredHeight += getPaddingTop() + getPaddingBottom();

        // Resolve our desired values against the constrained values that are passed into onMeasure()
        int width = resolveSizeAndState(desiredWidth, widthMeasureSpec, 0);
        int height = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0);

        // Inform the system what the values are
        setMeasuredDimension(width, height);
    }

    /**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     *
     * @param w    Current width of this view.
     * @param h    Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     *
     * We need to know our view size in order to calculate our drawing positions.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // Create a list of rectangles that we'll use to position each of the circles when it comes
        // time to draw them
        setupModuleRectangles(w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /* Positioning each of our circles in the right place */

        // for loop that counts for the indexes we need to access our mModuleRectangles array
        for (int moduleIndex=0; moduleIndex < mModuleRectangles.length; moduleIndex++) {

            if (mShape == SHAPE_CIRCLE) {
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
            } else {
                drawSquare(canvas, moduleIndex);
            }
        }

    }

    private void drawSquare(Canvas canvas, int moduleIndex) {
        Rect moduleRectangle = mModuleRectangles[moduleIndex];

        if(mModuleStatus[moduleIndex])
            canvas.drawRect(moduleRectangle, mPaintFill);

        canvas.drawRect(moduleRectangle.left + (mOutlineWidth/2),
                moduleRectangle.top + (mOutlineWidth/2),
                moduleRectangle.right - (mOutlineWidth/2),
                moduleRectangle.bottom - (mOutlineWidth/2),
                mPaintOutline);
    }

    /**
     * Implement this method to handle touch screen motion events.
     * <p>
     * If this method is used to detect click actions, it is recommended that
     * the actions be performed by implementing and calling
     * {@link #performClick()}. This will ensure consistent system behavior,
     * including:
     * <ul>
     * <li>obeying click sound preferences
     * <li>dispatching OnClickListener calls
     * <li>handling {@link AccessibilityNodeInfo#ACTION_CLICK ACTION_CLICK} when
     * accessibility features are enabled
     * </ul>
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;

            case MotionEvent.ACTION_UP:

                // Determine which module was touched
                int moduleIndex = findItemAtPoint(event.getX(), event.getY());

                // Code to update the Module Status
                onModuleSelected(moduleIndex);

                return true;
        }

        return super.onTouchEvent(event);
    }

    private void onModuleSelected(int moduleIndex) {

        // For when a user touches our custom view outside all our module rectangles.
        if (moduleIndex == INVALID_INDEX)
            return;

        // Set mModuleStatus value that corresponds to the moduleIndex & set it to the opposite
        // value it currently has.
        mModuleStatus[moduleIndex] = ! mModuleStatus[moduleIndex];

        // Inform system that the view needs to be redrawn.
        invalidate();
    }

    private int findItemAtPoint(float x, float y) {

        // Find index of the rectangle that the user touched.
        int moduleIndex = INVALID_INDEX;

        // Loop to figure out which module was touched.
        for (int i = 0; i < mModuleRectangles.length; i++) {
            if (mModuleRectangles[i].contains((int) x, (int) y)) {

                // Assign array index to our moduleIndex variable
                moduleIndex = i;
                break;
            }
        }

        return moduleIndex;
    }

    // Private Nested Class
    private class ModuleStatusAccessibilityHelper extends ExploreByTouchHelper {

        /**
         * Constructs a new helper that can expose a virtual view hierarchy for the
         * specified host view.
         *
         * @param host view whose virtual view hierarchy is exposed by this helper
         */
        public ModuleStatusAccessibilityHelper(@NonNull View host) {
            super(host);
        }

        /**
         * Provides a mapping between view-relative coordinates and logical
         * items.
         *
         * @param x The view-relative x coordinate
         * @param y The view-relative y coordinate
         * @return virtual view identifier for the logical item under
         * coordinates (x,y) or {@link #HOST_ID} if there is no item at
         * the given coordinates
         */
        @Override
        protected int getVirtualViewAt(float x, float y) {
            return 0;
        }

        /**
         * Populates a list with the view's visible items. The ordering of items
         * within {@code virtualViewIds} specifies order of accessibility focus
         * traversal.
         *
         * @param virtualViewIds The list to populate with visible items
         */
        @Override
        protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {

        }

        /**
         * Populates an {@link AccessibilityNodeInfoCompat} with information
         * about the specified item.
         * <p>
         * Implementations <strong>must</strong> populate the following required
         * fields:
         * <ul>
         * <li>event text, see
         * {@link AccessibilityNodeInfoCompat#setText(CharSequence)} or
         * {@link AccessibilityNodeInfoCompat#setContentDescription(CharSequence)}
         * <li>bounds in parent coordinates, see
         * {@link AccessibilityNodeInfoCompat#setBoundsInParent(Rect)}
         * </ul>
         * <p>
         * The helper class automatically populates the following fields with
         * default values, but implementations may optionally override them:
         * <ul>
         * <li>enabled state, set to {@code true}, see
         * {@link AccessibilityNodeInfoCompat#setEnabled(boolean)}
         * <li>keyboard focusability, set to {@code true}, see
         * {@link AccessibilityNodeInfoCompat#setFocusable(boolean)}
         * <li>item class name, set to {@code android.view.View}, see
         * {@link AccessibilityNodeInfoCompat#setClassName(CharSequence)}
         * </ul>
         * <p>
         * The following required fields are automatically populated by the
         * helper class and may not be overridden:
         * <ul>
         * <li>package name, identical to the package name set by
         * {@link #onPopulateEventForVirtualView(int, AccessibilityEvent)}, see
         * {@link AccessibilityNodeInfoCompat#setPackageName}
         * <li>node source, identical to the event source set in
         * {@link #onPopulateEventForVirtualView(int, AccessibilityEvent)}, see
         * {@link AccessibilityNodeInfoCompat#setSource(View, int)}
         * <li>parent view, set to the host view, see
         * {@link AccessibilityNodeInfoCompat#setParent(View)}
         * <li>visibility, computed based on parent-relative bounds, see
         * {@link AccessibilityNodeInfoCompat#setVisibleToUser(boolean)}
         * <li>accessibility focus, computed based on internal helper state, see
         * {@link AccessibilityNodeInfoCompat#setAccessibilityFocused(boolean)}
         * <li>keyboard focus, computed based on internal helper state, see
         * {@link AccessibilityNodeInfoCompat#setFocused(boolean)}
         * <li>bounds in screen coordinates, computed based on host view bounds,
         * see {@link AccessibilityNodeInfoCompat#setBoundsInScreen(Rect)}
         * </ul>
         * <p>
         * Additionally, the helper class automatically handles keyboard focus and
         * accessibility focus management by adding the appropriate
         * {@link AccessibilityNodeInfoCompat#ACTION_FOCUS},
         * {@link AccessibilityNodeInfoCompat#ACTION_CLEAR_FOCUS},
         * {@link AccessibilityNodeInfoCompat#ACTION_ACCESSIBILITY_FOCUS}, or
         * {@link AccessibilityNodeInfoCompat#ACTION_CLEAR_ACCESSIBILITY_FOCUS}
         * actions. Implementations must <strong>never</strong> manually add these
         * actions.
         * <p>
         * The helper class also automatically modifies parent- and
         * screen-relative bounds to reflect the portion of the item visible
         * within its parent.
         *
         * @param virtualViewId The virtual view identifier of the item for
         *                      which to populate the node
         * @param node          The node to populate
         */
        @Override
        protected void onPopulateNodeForVirtualView(int virtualViewId, @NonNull AccessibilityNodeInfoCompat node) {

        }

        /**
         * Performs the specified accessibility action on the item associated
         * with the virtual view identifier. See
         * {@link AccessibilityNodeInfoCompat#performAction(int, Bundle)} for
         * more information.
         * <p>
         * Implementations <strong>must</strong> handle any actions added manually
         * in
         * {@link #onPopulateNodeForVirtualView(int, AccessibilityNodeInfoCompat)}.
         * <p>
         * The helper class automatically handles focus management resulting
         * from {@link AccessibilityNodeInfoCompat#ACTION_ACCESSIBILITY_FOCUS}
         * and
         * {@link AccessibilityNodeInfoCompat#ACTION_CLEAR_ACCESSIBILITY_FOCUS}
         * actions.
         *
         * @param virtualViewId The virtual view identifier of the item on which
         *                      to perform the action
         * @param action        The accessibility action to perform
         * @param arguments     (Optional) A bundle with additional arguments, or
         *                      null
         * @return true if the action was performed
         */
        @Override
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, @Nullable Bundle arguments) {
            return false;
        }
    }
}

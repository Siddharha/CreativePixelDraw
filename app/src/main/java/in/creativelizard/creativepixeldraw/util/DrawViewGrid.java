package in.creativelizard.creativepixeldraw.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by siddhartha on 14/9/17.
 */

public class DrawViewGrid extends View implements ColorChooser.ColorChooserListener {

    private static final int GRID_SIZE = 32;
    private static final String TAG = "DrawViewGrid";

    private short[][] grid;
    private double mHeightInPixels;
    private short mSelectedColor = 1;
    private int lastGridX = -1;
    private int lastGridY = -1;

    // These are the four colors provided for painting.
    // If years of classic has taught me anything, these
    // are enough colors for anything. Anything at all.

    // Interface for the Activity to know when a square is drawn


    // Some temporary variables so we don't allocate while rendering
    private Rect mRect = new Rect();
    private Paint mPaint = new Paint();

    private Boolean keepAnimating = false;

    public DrawViewGrid(Context context, AttributeSet attrs) {
        super(context, attrs);

        grid = new short[GRID_SIZE][GRID_SIZE];
        setAnimating(true);
    }

    public void setAnimating(Boolean val) {
        keepAnimating = val;
        if (val) {
            invalidate();
        }
    }

    /**
     * Convert from screen space.
     *
     * @param screenSpaceCoordinate the coordinate in screen space.
     * @return the pixel coordinate in the View.
     */
    public int sp(float screenSpaceCoordinate) {
        return (int) Math.floor(screenSpaceCoordinate * mHeightInPixels);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Assume this is a square (as we will make it so in onMeasure()
        // and figure out how many pixels there are.
        mHeightInPixels = this.getHeight();

        // Now, draw with 0,0 in upper left and 9,9 in lower right
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(3);
                mPaint.setColor(Color.GRAY);

                mRect.top = sp(((float) y) / GRID_SIZE);
                mRect.left = sp(((float) x) / GRID_SIZE);
                mRect.right = sp(((float) (x + 1)) / GRID_SIZE);
                mRect.bottom = sp(((float) (y + 1)) / GRID_SIZE);


                canvas.drawRect(mRect, mPaint);
            }
        }
        if (keepAnimating) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // We would like a square working area, so at layout time, we'll figure out how much room we
        // have grow to fit the larger of the parent's measure. This way we'll completely fill the
        // parent in one dimension.
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (parentWidth > parentHeight) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(parentHeight,
                    MeasureSpec.EXACTLY);
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidth,
                    MeasureSpec.EXACTLY);
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Paint a pixel with the currently selected color.
     *
     * @param gridX      the column of the pixel to paint.
     * @param gridY      the row of the pixel to paint.
     * @param colorIndex the index into the color array to paint with.
     */
    public void setMacroPixel(int gridX, int gridY, short colorIndex) {
        // paint that pixel with the currently selected color
        grid[gridX][gridY] = colorIndex;
    }

    @Override
    public void setColor(short color) {
        mSelectedColor = color;
    }

    @Override
    public short getColor() {
        return mSelectedColor;
    }
}
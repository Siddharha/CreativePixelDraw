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

import static android.R.attr.lines;

/**
 * Created by siddhartha on 14/9/17.
 */

public class DrawViewGrid extends DrawView implements ColorChooser.ColorChooserListener {

    private static final int GRID_SIZE = 64;
    private static final String TAG = "DrawViewGrid";

    private short[][] grid;
    private double mHeightInPixels;
    private short mSelectedColor = 1;
    private int cellHeight;
    private int cellWidth;

    // These are the four colors provided for painting.
    // If years of classic has taught me anything, these
    // are enough colors for anything. Anything at all.

    // Interface for the Activity to know when a square is drawn


    // Some temporary variables so we don't allocate while rendering
    private Rect mRect = new Rect();
    public Paint mPaint = new Paint();

    private Boolean keepAnimating = false;

    public DrawViewGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }
    public DrawViewGrid(Context context) {
        super(context);
        init();
    }
    public DrawViewGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        mHeightInPixels = this.getHeight();
        grid = new short[GRID_SIZE][GRID_SIZE];
        setAnimating(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.BLACK);
        cellWidth = super.getWidth() / GRID_SIZE;
        cellHeight = super.getHeight() / GRID_SIZE;

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
      /*  mHeightInPixels = this.getHeight();

        // Now, draw with 0,0 in upper left and 9,9 in lower right
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(1);
                mPaint.setColor(Color.GRAY);

                mRect.top = sp(((float) y) / GRID_SIZE);
                mRect.left = sp(((float) x) / GRID_SIZE);
                mRect.right = sp(((float) (x + 1)) / GRID_SIZE);
                mRect.bottom = sp(((float) (y + 1)) / GRID_SIZE);


                canvas.drawRect(mRect, mPaint);
               // canvas.drawLine(x,y,32f,32f);
            }
        }*/

        mHeightInPixels = this.getHeight();
        canvas.drawColor(Color.TRANSPARENT);

        for (int i = 0; i < GRID_SIZE; i++)
        {
            canvas.drawLine(0, /*i * cellHeight*/sp(((float) (i + 1)) / GRID_SIZE), getWidth(), /*i * cellHeight*/sp(((float) (i + 1)) / GRID_SIZE),
                    mPaint);
        }

        for (int i = 0; i < GRID_SIZE; i++)
        {
            canvas.drawLine(/*i * cellWidth*/sp(((float) (i + 1)) / GRID_SIZE), 0, /*i* cellWidth*/sp(((float) (i + 1)) / GRID_SIZE), getHeight(),
                    mPaint);
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellWidth = super.getWidth() / GRID_SIZE;
        cellHeight = super.getHeight() / GRID_SIZE;

        invalidate();
    }
}
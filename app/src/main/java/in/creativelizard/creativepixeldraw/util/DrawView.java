package in.creativelizard.creativepixeldraw.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import in.creativelizard.creativepixeldraw.activities.MainActivity;

/**
 * Created by siddhartha on 14/9/17.
 */

public class DrawView  extends View implements View.OnTouchListener, ColorChooser.ColorChooserListener {

    private static final int GRID_SIZE = 32;
    private static final String TAG = "DrawView";
    private short[][] grid;
    private double mHeightInPixels;
    public short mSelectedColor = 1;
    private int lastGridX = -1;
    private int lastGridY = -1;
    private DrawViewListener mListener;
    public ArrayList<Integer> arrayListColorIndex;

    // These are the four colors provided for painting.
    // If years of classic has taught me anything, these
    // are enough colors for anything. Anything at all.



    // Interface for the Activity to know when a square is drawn
    public interface DrawViewListener {
        void onDrawEvent(int gridX, int gridY, short colorIndex);
    }
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }
    public DrawView(Context context) {
        super(context);
        init();
    }
    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        arrayListColorIndex = new ArrayList<>();
        grid = new short[GRID_SIZE][GRID_SIZE];

        setOnTouchListener(this);

        setAnimating(true);
        /*mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.BLACK);*/

    }

    // Some temporary variables so we don't allocate while rendering
    private Rect mRect = new Rect();
    private Paint mPaint = new Paint();

    private Boolean keepAnimating = false;

    private boolean touchEnabled;

    public void setListener(DrawViewListener listener) {
        mListener = listener;
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
                if(arrayListColorIndex.size()==0) {
                    arrayListColorIndex.add(0x00ffffff);
                }else {
                    if(arrayListColorIndex.size()>1) {
                        if (!arrayListColorIndex.get(arrayListColorIndex.size()-1).equals(((MainActivity) getContext()).COLOR_MAP.get(1))) {
                            arrayListColorIndex.add(((MainActivity) getContext()).COLOR_MAP.get(1));
                         //   Toast.makeText(getContext(), "SIZE: " + arrayListColorIndex.size(), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        arrayListColorIndex.add(((MainActivity) getContext()).COLOR_MAP.get(1));
                    }
                }
                mPaint.setColor(arrayListColorIndex.get(grid[x][y]));
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

    public void setTouchEnabled(boolean touchEnabled) {
        this.touchEnabled = touchEnabled;
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent me) {
        if (!touchEnabled) {
            return false;
        }

        switch (me.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Find where the touch event, which is in pixels, maps
                // to our 10x10 grid. (0,0) is in the upper left, (9, 9)
                // is in the lower right.
                int gridX = (int) Math.floor(1.0 * me.getX() / mHeightInPixels
                        * GRID_SIZE);
                int gridY = (int) Math.floor(1.0 * me.getY() / mHeightInPixels
                        * GRID_SIZE);

              //  Log.d(TAG, "You touched " + gridX + " " + gridY + "/" + me.getY());
                drawGridWithColor(gridX, gridY);
                return true;
        }

        return false;
    }

     private void drawGridWithColor(int gridX, int gridY) {
        if (gridX < GRID_SIZE && gridY < GRID_SIZE && gridX >= 0
                && gridY >= 0) {

            short oldColor = grid[gridX][gridY];
            //(short) (arrayListColorIndex.size()-1)
            grid[gridX][gridY] = (short) (arrayListColorIndex.size()-1);

            // Don't double-draw or send messages where the color does not change
            boolean notSameSpot = (lastGridX != gridX) || (lastGridY != gridY);
            boolean notSameColor = oldColor != (short) (arrayListColorIndex.size()-1);
            if (notSameSpot && notSameColor) {
                mListener.onDrawEvent(gridX,gridY,(short) (arrayListColorIndex.size()-1));
                lastGridX = gridX;
                lastGridY = gridY;
            }
        }
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

    /**
     * Clear paint from all pixels.
     */
    public void clear() {
        lastGridX = -1;
        lastGridY = -1;

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                grid[x][y] = 0;
            }
        }
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
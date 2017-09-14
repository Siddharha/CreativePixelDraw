package in.creativelizard.creativepixeldraw;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity implements DrawView.DrawViewListener{

    private DrawView mDrawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        mDrawView = (DrawView) findViewById(R.id.drawView);
        mDrawView.setListener(this);
        mDrawView.setTouchEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDrawView.setMacroPixel((int) x, (int) y, (short)Color.WHITE);
                break;
            case MotionEvent.ACTION_MOVE:
              /*  dv.touch_move(x, y);
                dv.invalidate();*/
                break;
            case MotionEvent.ACTION_UP:
               /* dv.touch_up();
                dv.invalidate();*/
                break;
        }
        return true;
    }

    @Override
    public void onDrawEvent(int gridX, int gridY, short colorIndex) {

    }
}

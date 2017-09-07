package in.creativelizard.creativepixeldraw;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /*dv.touch_start(x, y);
                dv.invalidate();*/
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
}

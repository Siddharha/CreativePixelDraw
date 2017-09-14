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
        ((ColorChooser) findViewById(R.id.colorChooser))
                .setDrawView(((DrawView) findViewById(R.id.drawView)));
    }
    @Override
    public void onDrawEvent(int gridX, int gridY, short colorIndex) {

    }
}

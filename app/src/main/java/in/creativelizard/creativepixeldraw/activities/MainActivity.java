package in.creativelizard.creativepixeldraw.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import in.creativelizard.creativepixeldraw.util.ColorChooser;
import in.creativelizard.creativepixeldraw.util.ConstantClass;
import in.creativelizard.creativepixeldraw.util.DrawView;
import in.creativelizard.creativepixeldraw.R;
import in.creativelizard.creativepixeldraw.util.DrawViewGrid;
import in.creativelizard.creativepixeldraw.util.Pref;

public class MainActivity extends AppCompatActivity implements DrawView.DrawViewListener {

    private DrawView mDrawView;
    private DrawViewGrid drawViewGrid;
    private Button btnClearDrawing;
    private SwitchCompat swShowGrid;
    private Pref _pref;
    ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        loadStates();
        onActionPerform();
    }

    private void loadStates() {
        if(_pref.getBooleanSession(ConstantClass.IS_GRID_VIEW)){
            drawViewGrid.setVisibility(View.VISIBLE);
            swShowGrid.setChecked(true);
        }else {
            drawViewGrid.setVisibility(View.GONE);
            swShowGrid.setChecked(false);
        }
    }

    private void onActionPerform() {
        btnClearDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawView.clear();
            }
        });

        swShowGrid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    _pref.setSession(ConstantClass.IS_GRID_VIEW,true);
                    drawViewGrid.setVisibility(View.VISIBLE);
                }else {
                    _pref.setSession(ConstantClass.IS_GRID_VIEW,false);
                    drawViewGrid.setVisibility(View.GONE);
                }

              //  mDrawView.refrash();
            }
        });
    }

    private void initialize() {
        _pref = new Pref(this);
        btnClearDrawing = (Button)findViewById(R.id.btnClearDrawing);
        swShowGrid = (SwitchCompat)findViewById(R.id.swShowGrid);
        mDrawView = (DrawView) findViewById(R.id.drawView);
        drawViewGrid = (DrawViewGrid)findViewById(R.id.drawViewGrid);
        mDrawView.setListener(this);
        mDrawView.setTouchEnabled(true);
        ((ColorChooser) findViewById(R.id.colorChooser))
                .setDrawView(((DrawView) findViewById(R.id.drawView)));
    }
    @Override
    public void onDrawEvent(int gridX, int gridY, short colorIndex) {

    }
}

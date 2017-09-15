package in.creativelizard.creativepixeldraw.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import in.creativelizard.creativepixeldraw.util.ColorChooser;
import in.creativelizard.creativepixeldraw.util.ConstantClass;
import in.creativelizard.creativepixeldraw.util.DrawView;
import in.creativelizard.creativepixeldraw.R;
import in.creativelizard.creativepixeldraw.util.DrawViewGrid;
import in.creativelizard.creativepixeldraw.util.Pref;

public class MainActivity extends AppCompatActivity implements DrawView.DrawViewListener {

    private static final String TAG = "response";
    private static final int REQUEST_CODE = 100;
    private DrawView mDrawView;
    private DrawViewGrid drawViewGrid;
    private Button btnClearDrawing,btnSaveDrawing;
    private SwitchCompat swShowGrid;
    private Pref _pref;
    private SwitchCompat swlockDraw;
    private LinearLayout llMain;
    private FrameLayout flCanv,flMainCanvCont;
    private SeekBar zoomBar;
    float dX, dY;
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

        if(swlockDraw.isChecked()){
            mDrawView.setTouchEnabled(false);
        }else {
            mDrawView.setTouchEnabled(true);
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
        swlockDraw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mDrawView.setTouchEnabled(!b);
            }
        });

        btnSaveDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Log.v(TAG,"Permission is not granted!");
                        //File write logic here
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    }else {
                        saveImage();
                    }
                }else {
                    saveImage();
                }

            }
        });

        zoomBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                flCanv.setScaleX(1+(i));
                flCanv.setScaleY(1+(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        flCanv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

if(swlockDraw.isChecked()) {
    switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            dX = v.getX() - event.getRawX();
            dY = v.getY() - event.getRawY();
            break;
        case MotionEvent.ACTION_MOVE:


            v.setY(event.getRawY() + dY);
            v.setX(event.getRawX() + dX);
            // .x(event.getRawX() + dX)
            break;
        case MotionEvent.ACTION_UP:
                        /*if(v.getY()<(flMainCanvCont.getY()-v.getHeight()+10)){
                            v.setY(flMainCanvCont.getY()-v.getHeight()+10);
                        }else if((v.getY()+v.getHeight())>(flMainCanvCont.getY()+flMainCanvCont.getHeight())){
                            v.setY((flMainCanvCont.getY()/90)+flMainCanvCont.getHeight()-v.getHeight());
                        } else {
                            v.setY(v.getY());
                        }*/

            return true;

        default:
            return false;
    }
    return true;
}else {
    return false;
}
            }
        });

    }

    public void saveImage() {
        Bitmap toDisk = null;
        try {
            mDrawView.setDrawingCacheEnabled(true);
            // TODO: Get the size of the canvas, replace the 640, 480
            toDisk = mDrawView.getDrawingCache();
           // toDisk = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
            Bitmap scaled = Bitmap.createScaledBitmap(toDisk, 32, 32, false);
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.e("rr",path);
            File file = new File(path+File.separator+"name"+".png");
         // mDrawView.setBitmap(toDisk);
            // toDisk.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("pixelDraw.jpg")));
            try
            {
                if(!file.exists())

                {
                    file.createNewFile();
                }
                FileOutputStream ostream = new FileOutputStream(file);
                scaled.compress(Bitmap.CompressFormat.PNG, 10, ostream);

                ostream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initialize() {
        _pref = new Pref(this);
        llMain = (LinearLayout)findViewById(R.id.llMain);
        flMainCanvCont = (FrameLayout)findViewById(R.id.flMainCanvCont);
        zoomBar = (SeekBar)findViewById(R.id.zoomBar);
        swlockDraw = (SwitchCompat) findViewById(R.id.swlockDraw);
        flCanv = (FrameLayout)findViewById(R.id.flCanv);
        btnClearDrawing = (Button)findViewById(R.id.btnClearDrawing);
        swShowGrid = (SwitchCompat)findViewById(R.id.swShowGrid);
        mDrawView = (DrawView) findViewById(R.id.drawView);
        drawViewGrid = (DrawViewGrid)findViewById(R.id.drawViewGrid);
        btnSaveDrawing = (Button)findViewById(R.id.btnSaveDrawing);
        mDrawView.setListener(this);
        mDrawView.setTouchEnabled(true);
        ((ColorChooser) findViewById(R.id.colorChooser))
                .setDrawView(((DrawView) findViewById(R.id.drawView)));
    }
    @Override
    public void onDrawEvent(int gridX, int gridY, short colorIndex) {

    }
}

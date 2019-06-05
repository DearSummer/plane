package com.billy.plane;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.billy.plane.constant.APIContants;
import com.billy.plane.util.NetworkUtils;
import com.billy.plane.view.JoystickView;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0x333;
    private ImageView setting_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initJoystick();
        registerPermission();
        setting_btn = (ImageView)findViewById(R.id.settings);

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void registerPermission()
    {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE);
        }
        else{
            initSurfaceView();
        }


    }


    private void initJoystick()
    {
        JoystickView jv_l = findViewById(R.id.jv_left);
        JoystickView jv_r = findViewById(R.id.jv_right);

        jv_l.setOnJoystickActiveListener(new JoystickView.OnJoystickActiveListenerAdapter() {
            @Override
            public void onJoystickMove(final Vec2 vec2) {
                Log.d("joystick_l",vec2.toString());
                NetworkUtils.postMethod(APIContants.APIVec2.log,new HashMap<String, String>()
                {
                    {
                        put("name", "joystick_l");
                        put("x", String.valueOf(vec2.getX()));
                        put("y", String.valueOf(vec2.getY()));
                    }
                },null,null);
            }
        });

        jv_r.setOnJoystickActiveListener(new JoystickView.OnJoystickActiveListenerAdapter() {
            @Override
            public void onJoystickMove(final Vec2 vec2) {
                Log.d("joystick_r",vec2.toString());
                NetworkUtils.postMethod(APIContants.APIVec2.log,new HashMap<String, String>()
                {
                    {
                        put("name", "joystick_r");
                        put("x", String.valueOf(vec2.getX()));
                        put("y", String.valueOf(vec2.getY()));
                    }
                },null,null);
            }
        });
    }

    Camera camera;
    private void initSurfaceView()
    {
        SurfaceView sv_view = findViewById(R.id.sv_view);
        final SurfaceHolder holder = sv_view.getHolder();

        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder){
                camera = Camera.open();
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                if(camera != null)
                {
                    camera.stopPreview();
                    camera.release();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initSurfaceView();
            }
        }
    }
}

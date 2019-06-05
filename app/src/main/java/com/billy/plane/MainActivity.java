package com.billy.plane;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.billy.plane.constant.APIConstants;
import com.billy.plane.entity.dto.Vec2Back;
import com.billy.plane.util.INetCallback;
import com.billy.plane.util.NetworkUtils;
import com.billy.plane.view.JoystickView;
import com.billy.plane.view.PingView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0x333;
    private PingView pv_ping;


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
        ImageView setting_btn = findViewById(R.id.settings);

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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.INTERNET,
                            Manifest.permission.CAMERA},
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

        pv_ping = findViewById(R.id.pv_ping);
        pv_ping.setZOrderOnTop(true);

        final Class<Vec2Back> cls = Vec2Back.class;
        final HashMap<String,String> lmap = new HashMap<String, String>() {
            {
                put("name", "joystick_l");
                put("x", String.valueOf(0));
                put("y", String.valueOf(0));
                put("time","0");
            }
        };
        final INetCallback<Vec2Back> joystickLCallback = new INetCallback<Vec2Back>() {
            @Override
            public void onSuccess(Vec2Back msg) {
                pv_ping.ping = System.currentTimeMillis() - msg.getTime();
            }
        };
        jv_l.setOnJoystickActiveListener(new JoystickView.OnJoystickActiveListenerAdapter() {
            @Override
            public void onJoystickMove(Vec2 vec2) {
               // Log.d("joystick_l",vec2.toString());
                long time = System.currentTimeMillis();
                for (Map.Entry<String, String> stringStringEntry : lmap.entrySet()) {
                    Map.Entry<String, String> entry = (Map.Entry) stringStringEntry;
                    if (entry.getKey().equals("time")) {
                        entry.setValue(String.valueOf(time));
                    } else if (entry.getKey().equals("x")) {
                        entry.setValue(String.valueOf(vec2.getX()));
                    } else if (entry.getKey().equals("y")) {
                        entry.setValue(String.valueOf(vec2.getY()));
                    }
                }
                NetworkUtils.postMethod(APIConstants.APIVec2.log, lmap,cls, joystickLCallback);
            }
        });


        final HashMap<String,String> rmap = new HashMap<String, String>() {
            {
                put("name", "joystick_l");
                put("x", String.valueOf(0));
                put("y", String.valueOf(0));
                put("time","0");
            }
        };
        final INetCallback<Vec2Back> joystickRCallback = new INetCallback<Vec2Back>() {
            @Override
            public void onSuccess(Vec2Back msg) {
                pv_ping.ping = System.currentTimeMillis() - msg.getTime();
            }
        };
        jv_r.setOnJoystickActiveListener(new JoystickView.OnJoystickActiveListenerAdapter() {
            @Override
            public void onJoystickMove(Vec2 vec2) {
                //Log.d("joystick_r",vec2.toString());
                long time = System.currentTimeMillis();
                for (Map.Entry<String, String> stringStringEntry : rmap.entrySet()) {
                    Map.Entry<String, String> entry = (Map.Entry) stringStringEntry;
                    if (entry.getKey().equals("time")) {
                        entry.setValue(String.valueOf(time));
                    } else if (entry.getKey().equals("x")) {
                        entry.setValue(String.valueOf(vec2.getX()));
                    } else if (entry.getKey().equals("y")) {
                        entry.setValue(String.valueOf(vec2.getY()));
                    }
                }
                NetworkUtils.postMethod(APIConstants.APIVec2.log, rmap, cls, joystickRCallback);
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

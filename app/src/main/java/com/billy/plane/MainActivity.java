package com.billy.plane;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.billy.plane.view.JoystickView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JoystickView jv_l = findViewById(R.id.jv_left);
        JoystickView jv_r = findViewById(R.id.jv_right);

        jv_l.setOnJoystickActiveListener(new JoystickView.OnJoystickActiveListenerAdapter() {
            @Override
            public void onJoystickMove(Vec2 vec2) {
                Log.d("joystick_l",vec2.toString());
            }
        });

        jv_r.setOnJoystickActiveListener(new JoystickView.OnJoystickActiveListenerAdapter() {
            @Override
            public void onJoystickMove(Vec2 vec2) {
                Log.d("joystick_r",vec2.toString());
            }
        });
    }
}

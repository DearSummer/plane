package com.billy.plane.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PingView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Paint paint;

    public PingView(Context context) {
        super(context);
        init();
    }

    public PingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public long ping = 0;
    private void init() {
        holder = getHolder();
        holder.addCallback(this);

        paint = new Paint();


    }
    private void drawPing(long ping)
    {
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

 //       canvas.drawColor(Color.WHITE);
//        paint.setTextSize(25f);
//        paint.setStrokeWidth(5f);
//        paint.setColor(Color.BLACK);
        canvas.drawColor(Color.TRANSPARENT);
        paint.setColor(Color.BLUE);
        paint.setTextSize(25);
        canvas.drawText(String.valueOf(ping),0,20,paint);
        holder.unlockCanvasAndPost(canvas);
    }


    boolean flag = true;
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(flag) {
                    drawPing(ping);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        flag = false;
    }
}

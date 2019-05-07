package com.billy.plane.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.billy.plane.R;
import com.billy.plane.Vec2;

public class JoystickView extends View {

    private static final String TAG = "JOYSTICK_VIEW";

    private static final int BACKGROUND_RADIUS_DEFAULT = 400;
    private static final int JOYSTICK_RADIUS_DEFAULT = BACKGROUND_RADIUS_DEFAULT / 8;

    private Paint backgroundPaint;
    private Paint joystickPaint;

    private Point joystickPos;
    private Point centerPos;

    private int radius;
    private int joystickRadius;

    private DrawableMode joystickMode;
    private DrawableMode backgroundMode;

    private Bitmap background;
    private Bitmap joystickBackground;

    private int backgroundColor;
    private int joystickColor;

    public JoystickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initAttribute(context,attrs);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);

        joystickPaint = new Paint();
        joystickPaint.setAntiAlias(true);

        centerPos = new Point();
        joystickPos = new Point();
    }



    private void initAttribute(Context context,AttributeSet attrs)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JoystickView);

        Drawable background = typedArray.getDrawable(R.styleable.JoystickView_areaBackground);
        if(background != null) {
            if (background instanceof BitmapDrawable) {
                this.background = ((BitmapDrawable) background).getBitmap();
                backgroundMode = DrawableMode.MODE_PIC;
            } else if (background instanceof GradientDrawable) {
                this.background = drawable2Bitmap(background);
                backgroundMode = DrawableMode.MODE_XML;
            } else if (background instanceof ColorDrawable) {
                backgroundColor = ((ColorDrawable) background).getColor();
                backgroundMode = DrawableMode.MODE_COLOR;
            } else {
                backgroundMode = DrawableMode.MODE_DEF;
            }
        }else{
            backgroundMode = DrawableMode.MODE_DEF;
        }

        Drawable joystickDrawable = typedArray.getDrawable(R.styleable.JoystickView_joystickBackground);
        if(joystickDrawable != null) {
            if (joystickDrawable instanceof BitmapDrawable) {
                this.joystickBackground = ((BitmapDrawable) joystickDrawable).getBitmap();
                joystickMode = DrawableMode.MODE_PIC;
            } else if (joystickDrawable instanceof GradientDrawable) {
                this.joystickBackground = drawable2Bitmap(joystickDrawable);
                joystickMode = DrawableMode.MODE_XML;
            } else if (joystickDrawable instanceof ColorDrawable) {
                joystickColor = ((ColorDrawable) joystickDrawable).getColor();
                joystickMode = DrawableMode.MODE_COLOR;
            } else {
                joystickMode = DrawableMode.MODE_DEF;
            }
        }else{
            joystickMode = DrawableMode.MODE_DEF;
        }


        joystickRadius = typedArray.getDimensionPixelOffset(R.styleable.JoystickView_joystickRadius,JOYSTICK_RADIUS_DEFAULT);
        radius = typedArray.getDimensionPixelOffset(R.styleable.JoystickView_areaRadius,BACKGROUND_RADIUS_DEFAULT);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth, measureHeight;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            measureWidth = widthSize;
        } else {
            measureWidth = radius;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measureHeight = heightSize;
        } else {
            measureHeight = radius;
        }

        setMeasuredDimension(measureWidth,measureHeight);
    }


    Rect res = new Rect(),dst = new Rect();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        int cx = w / 2;
        int cy = h / 2;

        centerPos.set(cx, cy);
        radius = w <= h ? cx : cy;

        if (0 == joystickPos.x || 0 == joystickPos.y) {
            joystickPos.set(centerPos.x, centerPos.y);
        }

        switch (backgroundMode) {
            case MODE_PIC:
            case MODE_XML:
                setRect(res, 0, 0, background.getWidth(), background.getHeight());
                setRect(dst, centerPos.x - radius, centerPos.y - radius, centerPos.x + radius, centerPos.y + radius);
                canvas.drawBitmap(background, res, dst, backgroundPaint);
                break;
            case MODE_COLOR:
                backgroundPaint.setColor(backgroundColor);
                canvas.drawCircle(centerPos.x, centerPos.y, radius, backgroundPaint);
                break;
            case MODE_DEF:
                backgroundPaint.setColor(Color.GRAY);
                canvas.drawCircle(centerPos.x, centerPos.y, radius, backgroundPaint);
                break;
        }

        switch (joystickMode){
            case MODE_PIC:
            case MODE_XML:
                setRect(res, 0, 0, joystickBackground.getWidth(), joystickBackground.getHeight());
                setRect(dst, joystickPos.x - joystickRadius, joystickPos.y - joystickRadius,
                        joystickPos.x + joystickRadius, joystickPos.y + joystickRadius);
                canvas.drawBitmap(joystickBackground, res, dst, joystickPaint);
                break;
            case MODE_COLOR:
                joystickPaint.setColor(joystickColor);
                canvas.drawCircle(joystickPos.x, joystickPos.y, joystickRadius, joystickPaint);
                break;
            case MODE_DEF:
                joystickPaint.setColor(Color.CYAN);
                canvas.drawCircle(joystickPos.x, joystickPos.y, joystickRadius, joystickPaint);
                break;
        }

    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    Vec2 pos = new Vec2();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (listener != null)
                    listener.onFingerDown();
                performClick();
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();

                float lenX = moveX - centerPos.x;
                float lenY = moveY - centerPos.y;

                float len = (float) Math.sqrt(lenX * lenX + lenY * lenY);

                if (len + joystickRadius <= radius) {
                    joystickPos.set((int) moveX, (int) moveY);
                    pos.reset(lenX / (radius - joystickRadius), -lenY / (radius - joystickRadius));
                } else {
                    float showX = (centerPos.x + (radius - joystickRadius) * lenX / len);
                    float showY = (centerPos.y + (radius - joystickRadius) * lenY / len);
                    pos.reset((showX - centerPos.x) / (radius - joystickRadius),
                            -(showY - centerPos.y) / (radius - joystickRadius));
                    joystickPos.set((int) showX, (int) showY);
                }

                if (listener != null)
                    listener.onJoystickMove(pos);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null)
                    listener.onFingerUp();

                joystickPos.set(centerPos.x, centerPos.y);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (listener != null)
                    listener.onCancel();

                joystickPos.set(centerPos.x, centerPos.y);
                invalidate();
                break;
        }

        return true;
    }


    private OnJoystickActiveListener listener;
    public void setOnJoystickActiveListener(OnJoystickActiveListener listener)
    {
        this.listener = listener;
    }

    private Bitmap drawable2Bitmap(Drawable drawable)
    {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;

        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        drawable.draw(canvas);

        return bitmap;
    }

    private void setRect(Rect res, int l, int t, int r, int b)
    {
        res.bottom = b;
        res.left = l;
        res.right = r;
        res.top = t;
    }
    enum  DrawableMode {
        MODE_PIC,
        MODE_XML,
        MODE_COLOR,
        MODE_DEF
    }

    public interface OnJoystickActiveListener {
        void onFingerDown();
        void onJoystickMove(Vec2 vec2);
        void onFingerUp();
        void onCancel();
    }

    public abstract static class OnJoystickActiveListenerAdapter implements OnJoystickActiveListener
    {
        @Override
        public void onCancel() {
        }

        @Override
        public void onFingerDown() {
        }

        @Override
        public void onFingerUp() {
        }

        @Override
        public void onJoystickMove(Vec2 vec2) {
        }
    }
}

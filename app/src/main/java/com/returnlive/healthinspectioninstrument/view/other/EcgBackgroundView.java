package com.returnlive.healthinspectioninstrument.view.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.returnlive.healthinspectioninstrument.R;


/**
 * Created by ccl on 2017/3/27.
 * 心电图背景格子图控件
 */

public class EcgBackgroundView extends View {

//    private final static String TAG = "EcgBackgroundView";

    private int mThinLineColor;
    private int mThickLineColor;

    private Paint mPaintBold;
    private Paint mPaintNor;
    private int sizeX;
    private int sizeY;
    public static float xS;//每个格子的毫米尺寸
    public static float totalLattices;//平均总格子数
    private float mViewWidth;

    private float mViewHeight;
    private float mViewHalfWidth;
    private float mViewHalfHeight;
    private final static float mm2Inches = 0.03937f;

    public EcgBackgroundView(Context context) {
        super(context);
        init();
    }

    public EcgBackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        init();
    }

    public EcgBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EcgBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initTypedArray(context, attrs);
        init();
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EcgBackgroundView);
        mThinLineColor = typedArray.getColor(R.styleable.EcgBackgroundView_thinLineColor, 0xff11172a);
        mThickLineColor = typedArray.getColor(R.styleable.EcgBackgroundView_thickLineColor, 0xff545767);
        typedArray.recycle();
    }

    private void init() {
        mPaintBold = new Paint();
        mPaintBold.setAntiAlias(true);
        mPaintBold.setColor(mThinLineColor);
        mPaintBold.setStrokeWidth(1f);
        mPaintBold.setStyle(Paint.Style.FILL);

        mPaintNor = new Paint();
        mPaintNor.setAntiAlias(true);
        mPaintNor.setColor(mThickLineColor);
        mPaintNor.setStrokeWidth(1f);
        mPaintNor.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < sizeX; i++) {
            final float x1 = mViewHalfWidth - i * xS;
            final float x2 = mViewHalfWidth + i * xS;
            if (i % 5 == 0) {
                canvas.drawLine(x1, 0, x1, mViewHeight, mPaintBold);
                if (i > 0) canvas.drawLine(x2, 0, x2, mViewHeight, mPaintBold);
            } else {
                canvas.drawLine(x1, 0, x1, mViewHeight, mPaintNor);
                canvas.drawLine(x2, 0, x2, mViewHeight, mPaintNor);
            }
        }
        for (int i = 0; i < sizeY; i++) {
            final float y1 = mViewHalfHeight - i * xS;
            final float y2 = mViewHalfHeight + i * xS;
            if (i % 5 == 0) {
                canvas.drawLine(0, y1, mViewWidth, y1, mPaintBold);
                if (i > 0) canvas.drawLine(0, y2, mViewWidth, y2, mPaintBold);
            } else {
                canvas.drawLine(0, y1, mViewWidth, y1, mPaintNor);
                canvas.drawLine(0, y2, mViewWidth, y2, mPaintNor);
            }
        }
//        canvas.drawCircle(mViewHalfWidth, mViewHalfHeight, 4f, mPaintNor);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        initDrawLatticeParams();
        super.onLayout(changed, left, top, right, bottom);
    }

    private void initDrawLatticeParams() {
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        mViewWidth = getWidth();
        mViewHeight = getHeight();
//        Log.e(TAG, "test : mViewWidth  x : mViewHeight = " + mViewWidth + " x " + mViewHeight);
        mViewHalfWidth = mViewWidth / 2.0f;
        mViewHalfHeight = mViewHeight / 2.0f;
        final float xdpi = dm.xdpi;
        final float ydpi = dm.ydpi;
//        Log.e(TAG, "test : xdpi  x : ydpi = " + xdpi + " x " + ydpi);
        final float diffX = xdpi - 160.0f;
        final float diffY = ydpi - 160.0f;
        float multi = 1.0f;
        //判断 xdpi和ydpi 是否约等于160.0
        if ((diffX > -1.0f && diffX < 1.0f) && (diffY > -1.0f && diffY < 1.0f)) {
            //若是 根据根据像素密度校正。
            if (density == 2.0d) multi = 1.70f;
            if (density == 3.0d) multi = 2.55f;
        }
//        Log.e(TAG, "test : multi = " + multi);
        //算出控件物理英寸宽度和高度
        final float viewXInches = mViewWidth / (xdpi * multi);
        final float viewYInches = mViewHeight / (ydpi * multi);
        //根据毫米和英寸的转换率将英寸转换成毫米，
        // 由于需要以控件中点为中心向两轴画线，所以需要按以控件的一半去计算，
        // 以此得出X轴方向和Y轴方向半布局所需画的格子数。
        float totalViewXmm = viewXInches / mm2Inches;//控件X轴毫米尺寸
        final float totalViewYmm = viewYInches / mm2Inches;
        final float mmX0 = 0.5f * totalViewXmm;
        sizeX = (int) mmX0;
        if (mmX0 - sizeX >= 0.5f) sizeX = sizeX + 1;
        final double mmY0 = 0.5f * totalViewYmm;
        sizeY = (int) mmY0;
        if (mmY0 - sizeY >= 0.5f) sizeY = sizeY + 1;
        xS = mViewWidth / (viewXInches / mm2Inches);
        totalLattices = mViewWidth / xS;//平均总格子数
//        Log.e(TAG, "test -- xS:" + xS);
    }
}

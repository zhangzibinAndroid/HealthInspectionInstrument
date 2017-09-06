package com.returnlive.healthinspectioninstrument.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 7:29
 * 描述： 心电图折线
 */

public class EcgPathViewHistory extends EcgBackGroundView {
    private static final String TAG = "PathView";
    private Context context;

    public ArrayList<Integer> arrast = new ArrayList();
    float tmp = 0;

    public EcgPathViewHistory(Context context) {
        super(context);
        this.context = context;
        mPaint = new Paint();
        mPath = new Path();

    }

    public EcgPathViewHistory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EcgPathViewHistory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void addAllDATA(ArrayList<Integer> list) {
        arrast.clear();
        arrast.addAll(list);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawCanvas(canvas);

    }

    private void drawCanvas(Canvas canvas) {
        canvas.translate(0, mHeight / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        float endY = 0;
        for (int i = 0; i < arrast.size(); i++) {
            endY = arrast.get(i) / 50;//按屏幕比例缩小Y轴比例
            tmp = (float) (tmp+1);
            mPath.lineTo(tmp, endY);
            mPath.moveTo(tmp, endY);
        }
        scrollTo((int) tmp,0);
        canvas.drawPath(mPath, mPaint);
        invalidate();
    }


}

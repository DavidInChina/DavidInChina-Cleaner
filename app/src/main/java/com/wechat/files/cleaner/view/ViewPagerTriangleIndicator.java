package com.wechat.files.cleaner.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * author:davidinchina on 2019/5/13 17:16
 * email:davicdinchina@gmail.com
 * version:1.0.0
 * des:
 */
public class ViewPagerTriangleIndicator extends LinearLayout {

    private int mTriangleWidth;//三角形底边宽
    private int mTriangleHeigh;//三角形高度
    private int mTriangleInitPos;//三角形起始点
    private int mTriangleMoveWidth;//三角形移动偏移
    private int tabSize = 2;
    private Paint mPaint;
    private Path mPath;

    public ViewPagerTriangleIndicator(Context context) {
        super(context, null);
    }

    public ViewPagerTriangleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeigh);
        mPath.close();
    }


    /**
     * 当布局大小发生变化时回调
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = w / 3 / 10;
        mTriangleHeigh = mTriangleWidth / tabSize;
        mTriangleInitPos = getScreenWidth() / 2 / 2 - mTriangleWidth / 2;
        initTriangle();
    }

    /**
     * 绘制子View
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.translate(mTriangleInitPos + mTriangleMoveWidth, getHeight());
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 监听ViewPager滑动,联动Indicator
     *
     * @param position
     * @param positionOffset
     */
    public void scroll(int position, float positionOffset) {
        int tabWidth = getScreenWidth() / tabSize;
        mTriangleMoveWidth = (int) (tabWidth * position + tabWidth * positionOffset);
        invalidate();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
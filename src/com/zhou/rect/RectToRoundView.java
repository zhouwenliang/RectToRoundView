package com.zhou.rect;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/4/10.
 */
public class RectToRoundView extends View {

    private Paint mPaint;
    private Handler mCanvasHandler;
    private static final int FRAME_RATE = 10;
    private float mRotateAngle = 0;

    private int mRoundX,mRoundY;
    private int mRadius;

    private boolean mIsReverse = false;
    private boolean mIsWait = false;
    private int mWaitTime = 0;

    Bitmap mBitmap;
    BitmapDrawable mDrawable;

    private final Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            invalidate();
        }
    };




    public void init(){
        if(mPaint == null){
            mPaint = new Paint();
            mPaint.setColor(0xff9c27b0);
            mPaint.setAntiAlias(true);
        }
        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);
        mDrawable = new BitmapDrawable(mBitmap);
        mCanvasHandler = new Handler();
    }

    public RectToRoundView(Context context) {
        super(context);
        init();
    }

    public RectToRoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RectToRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void calculate(){
        mRoundX = getMeasuredWidth() / 2;
        mRoundY = getMeasuredHeight() / 2;
        mRadius = mRoundX - (int)(mRotateAngle/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(mRotateAngle, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        Path path = new Path();
        calculate();
        if(mRotateAngle == 0){
            path.moveTo(mRoundX - mRadius, mRoundY - mRadius);
            path.lineTo(mRoundX + mRadius, mRoundY - mRadius);
            path.lineTo(mRoundX + mRadius, mRoundY + mRadius);
            path.lineTo(mRoundX - mRadius, mRoundY + mRadius);
            path.close();
        }else if(mRotateAngle < 22.5f){
            RectF oval3 = new RectF(mRoundX - mRadius, mRoundY - mRadius, mRoundX + mRadius, mRoundY + mRadius);
            path.addRoundRect(oval3,mRotateAngle*4,mRotateAngle*4, Path.Direction.CW);
        }else {
            path.addCircle(mRoundX, mRoundY, mRadius, Path.Direction.CW);
        }

        canvas.drawPath(path,mPaint);
        canvas.restore();
        super.onDraw(canvas);
        mDrawable.setBounds(mRoundX - mRadius + 10, mRoundY - mRadius + 10, mRoundX + mRadius - 10, mRoundY + mRadius -10 );
        mDrawable.draw(canvas);
        if(mIsWait && mWaitTime < 100){
            mWaitTime++;
            invalidate();
            return;
        }else {
            mIsWait = false;
            mWaitTime = 0;
        }
        if(!mIsReverse){
            mRotateAngle += 3.5f;
        }else {
            mRotateAngle -= 3.5f;
        }

        if(mRotateAngle > 22.5f && mRotateAngle - 3.5f < 22.5f){
            mRotateAngle = 22.5f;
            mIsReverse = true;
            mIsWait = true;
        }

        if(mRotateAngle < 0f && mRotateAngle + 3.5f > 0){
            mRotateAngle = 0f;
            mIsReverse = false;
            mIsWait = true;
        }
        invalidate();
    }

}

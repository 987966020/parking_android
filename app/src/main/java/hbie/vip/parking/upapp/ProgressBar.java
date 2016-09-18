package hbie.vip.parking.upapp;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class ProgressBar extends View {
    private Paint mArcPaint;
    private Paint mArcBGPaint;

    private RectF mOval;
    private float mSweep = 0;
    private int mSpeedMax = 200;
    private int mThreshold = 100;
    private int mIncSpeedValue = 0;
    private int mCurrentSpeedValue = 0;
    private float mCenterX;
    private float mCenterY;
    private float mSpeedArcWidth;

    private final float SPEED_VALUE_INC = 2;



    public ProgressBar(Context context) {
        super(context);
        initPaint();
    }
    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void initPaint() {
        mArcPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mSpeedArcWidth);
        mArcPaint.setColor(0xff81ccd6);
        BlurMaskFilter mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.INNER);
        mArcPaint.setMaskFilter(mBlur);
        mArcBGPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcBGPaint.setStyle(Paint.Style.STROKE);
        mArcBGPaint.setStrokeWidth(mSpeedArcWidth + 8);
        mArcBGPaint.setColor(0xff171717);
        BlurMaskFilter mBGBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.INNER);
        mArcBGPaint.setMaskFilter(mBGBlur);
    }
//    设置两个画笔，颜色，宽度，样式等等，BlurMaskFilter笔是边缘模糊效果，有几种，可以自己尝试
    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        Log.i("onSizeChanged w", w + "");
        Log.i("onSizeChanged h", h + "");
        mCenterX = w * 0.5f;  // remember the center of the screen
        mCenterY = h - mSpeedArcWidth;
        mOval = new RectF(mCenterX - mCenterY, mSpeedArcWidth, mCenterX + mCenterY, mCenterY * 2);
    }
//    重写父类View的onSizeChanged，为的是自己根据布局中的大小做居中处理

    @Override
    protected void onDraw(Canvas canvas) {
        drawSpeed(canvas);
        calcSpeed();
    }

    private void drawSpeed(Canvas canvas) {
        canvas.drawArc(mOval, 179, 181, false, mArcBGPaint);

        mSweep = (float) mIncSpeedValue / mSpeedMax * 180;
        if (mIncSpeedValue > mThreshold) {
            mArcPaint.setColor(0xFFFF0000);
        } else {
            mArcPaint.setColor(0xFF00B0F0);
        }

        canvas.drawArc(mOval, 180, mSweep, false, mArcPaint);
    }

    private void calcSpeed() {
        if (mIncSpeedValue < mCurrentSpeedValue) {
            mIncSpeedValue += SPEED_VALUE_INC;
            if (mIncSpeedValue > mCurrentSpeedValue) {
                mIncSpeedValue = mCurrentSpeedValue;
            }
            invalidate();
        } else if (mIncSpeedValue > mCurrentSpeedValue) {
            mIncSpeedValue -= SPEED_VALUE_INC;
            if (mIncSpeedValue < mCurrentSpeedValue) {
                mIncSpeedValue = mCurrentSpeedValue;
            }
            invalidate();
        }
    }

}

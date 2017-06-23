package net.archeryc.radarview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;


/**
 * author: shell
 * date 2016/12/25 下午7:18
 **/
public class RippleView extends View  {

    private int mRadius;
    private int mAlpha;
    private Paint mPaint;
    private AnimatorSet mAnimator;
    private int mX, mY;
    private RadialGradient gradient;
    private Matrix matrix;
    private int minRadius;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        matrix = new Matrix();
//        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mPaint.setAlpha(mAlpha);
        if (gradient==null) {
            gradient = new RadialGradient(mX, mY, mRadius, Color.parseColor("#99ffffff"), Color.parseColor("#04ffffff"), Shader.TileMode.CLAMP);
        }else{
            float scale=(float)mRadius/(float)minRadius;
            matrix.setScale(scale,scale,mRadius/2,mRadius/2);
            gradient.setLocalMatrix(matrix);
        }
        mPaint.setShader(gradient);
        canvas.drawCircle(mX, mY, mRadius, mPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRipple();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRipple();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setCenterPoint(getMeasuredWidth()/2,getMeasuredWidth()/2);
//        mRadius=getMeasuredWidth()/2;
    }

    public void startRipple() {
        if (mAnimator!=null)
        mAnimator.start();
    }

    public void stopRipple() {
        if (mAnimator!=null)
            mAnimator.end();
    }

    //设置水波纹半径
    public void setRippleRadius(int radius) {
        mRadius = radius;
        invalidate();
    }

    //设置水波纹 alpha 范围[0-255]
    public void setRippleAlpha(int alpha) {
        mAlpha = alpha;
        invalidate();
    }

    public void setCenterPoint(int x, int y) {
        mX = x;
        mY = y;

    }

    public void initAnimator(int minRadius, int maxRadius, int alpha) {
        this.minRadius=minRadius;
        ObjectAnimator radiusAnimator = ObjectAnimator.ofInt(this, "RippleRadius", minRadius, maxRadius,minRadius);
        radiusAnimator.setRepeatMode(ValueAnimator.RESTART);
        radiusAnimator.setRepeatCount(ValueAnimator.INFINITE);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofInt(this, "RippleAlpha", alpha, 200,alpha);
        alphaAnimator.setRepeatMode(ValueAnimator.RESTART);
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator = new AnimatorSet();
        mAnimator.playTogether(radiusAnimator, alphaAnimator);
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new AccelerateInterpolator());
    }
}

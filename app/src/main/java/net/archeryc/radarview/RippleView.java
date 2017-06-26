package net.archeryc.radarview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;


/**
 * author: shell
 * date 2016/12/25 下午7:18
 **/
public class RippleView extends View {

    private int mRadius;
    private int mAlpha;
    private Paint mRipplePaint;
    private Paint mCirclePaint;
    private AnimatorSet mAnimator;
    private int mX, mY;
    private RadialGradient gradient;
    private Matrix matrix;
    private int minRadius;
    private int mAvatarRadius;
    private int mCircleAlpha;
    private OnAnimatorEndListener mOnAnimatorEndListener;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRipplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStrokeWidth(dip2px(getContext(), 2));
        matrix = new Matrix();
//        mRipplePaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("yc","getwidth/2===>"+getWidth()/2+"mRadius===>"+mRadius);
        mRipplePaint.setAlpha(mAlpha);
        if (gradient == null) {
            gradient = new RadialGradient(getWidth() / 2, getWidth() / 2, mRadius, Color.parseColor("#ffffffff"), Color.parseColor("#06ffffff"), Shader.TileMode.CLAMP);
        } else {
            float scale = (float) mRadius / (float) minRadius;
            Log.d("yc","getwidth/2===>"+getWidth()/2+"mRadius===>"+mRadius+"scale===>"+scale);

//            Log.d("yc", "scale==>" + scale + "0.5width===>" + getWidth() / 2);
            matrix.setScale(scale, scale, getWidth() / 2, getWidth() / 2);
            gradient.setLocalMatrix(matrix);
        }
        mRipplePaint.setShader(gradient);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, mRadius, mRipplePaint);

        mCirclePaint.setAlpha(mCircleAlpha);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, mAvatarRadius, mCirclePaint);
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
        setCenterPoint(getMeasuredWidth() / 2, getMeasuredWidth() / 2);
        mRadius = getMeasuredWidth() / 2;
    }

    public void startRipple() {
        if (mAnimator != null)
            mAnimator.start();
    }

    public void stopRipple() {
        if (mAnimator != null)
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

    public void setCircleAlpha(int alpha) {
        mCircleAlpha = alpha;
        invalidate();
    }


    public void setCenterPoint(int x, int y) {
        mX = x;
        mY = y;
    }

    public void initAnimator(int minRadius, int maxRadius, int avatarRadius, int alpha) {
        this.minRadius = minRadius;
        this.mAvatarRadius = avatarRadius;
        Log.d("avatar", "avatarRadius===>" + avatarRadius);
        Log.d("yc", "minRadius===>" + minRadius + "maxRadius===>" + maxRadius);
        ObjectAnimator radiusAnimator = ObjectAnimator.ofInt(this, "RippleRadius", minRadius, maxRadius, minRadius);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofInt(this, "RippleAlpha", 140, 255, 140);
        ObjectAnimator circleAlphaAnimator = ObjectAnimator.ofInt(this, "CircleAlpha", 0, 255, 0);


        mAnimator = new AnimatorSet();
        mAnimator.playTogether(radiusAnimator, alphaAnimator, circleAlphaAnimator);
        mAnimator.setDuration(3500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnAnimatorEndListener != null) {
                    mAnimator.removeAllListeners();
                    mOnAnimatorEndListener.onAnimatorEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    interface OnAnimatorEndListener {
        void onAnimatorEnd();
    }

    public void setOnAnimatorEndListener(OnAnimatorEndListener listener) {
        this.mOnAnimatorEndListener = listener;
    }
}

package net.archeryc.radarview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * 一句话功能简述 自定义雷达页面
 * 功能详细描述
 *
 * @author 杨晨 on 2017/6/16 13:57
 * @e-mail 247067345@qq.com
 * @see [相关类/方法](可选)
 */

public class RadarView extends View {

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    private static final int CIRCLE_NUM=3;
    private Context mContext;
    private int defaultWidth;
    private int defaultHeight;
    private Paint[] circlePaints;
    private ObjectAnimator[] circleAnimators;
    private float[] animatorValues;
    private float[] circleRadius;
    private float[] displayCircleRadius;
    private boolean isPlayAnimator=false;
    private boolean shouldStopAnimator=false;

    public RadarView(Context context) {
        this(context,null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initPaint();
    }

    private void initPaint() {
        circlePaints = new Paint[CIRCLE_NUM];
        for (int i = 0; i< circlePaints.length; i++){
            circlePaints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            circlePaints[i].setStrokeWidth(dip2px(mContext,1));
            circlePaints[i].setColor(Color.WHITE);
            circlePaints[i].setStyle(Paint.Style.STROKE);
        }
    }

    private void init() {
        mContext=getContext();
        defaultWidth = dip2px(mContext, DEFAULT_WIDTH);
        defaultHeight = dip2px(mContext, DEFAULT_HEIGHT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resultWidth;
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (modeWidth == MeasureSpec.EXACTLY)
        {
            resultWidth = sizeWidth;
        }
        else
        {
            resultWidth = defaultWidth;
            if (modeWidth == MeasureSpec.AT_MOST)
            {
                resultWidth = Math.min(resultWidth, sizeWidth);
            }
        }

        int resultHeight;
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (modeHeight == MeasureSpec.EXACTLY)
        {
            resultHeight = sizeHeight;
        }
        else
        {
            resultHeight = defaultHeight;
            if (modeHeight == MeasureSpec.AT_MOST)
            {
                resultHeight = Math.min(resultHeight, sizeHeight);
            }
        }
        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animatorValues!=null&&circlePaints!=null) {
            if (isPlayAnimator()) {
                for (int i = 0; i < CIRCLE_NUM; i++) {
                    canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, animatorValues[i], circlePaints[i]);
                }
            }else{
                for (int i = 0; i < CIRCLE_NUM; i++) {
                    circlePaints[i].setAlpha(255);
                    canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, displayCircleRadius[i], circlePaints[i]);
                }
            }
        }
    }


    public void showLoading(){
        post(new Runnable() {
            @Override
            public void run() {
                startAnimator();
            }
        });
    }
    /**
     * 显示正在加载状态
     */
    private void startAnimator() {
        setPlayAnimator(true);
        circleAnimators=new ObjectAnimator[CIRCLE_NUM];
        animatorValues = new float[CIRCLE_NUM];
        circleRadius = new float[CIRCLE_NUM];
        displayCircleRadius = new float[CIRCLE_NUM];

        for (int i=0;i<CIRCLE_NUM;i++){
            circleRadius[i]=getMeasuredWidth()/2-i*getMeasuredWidth()/((CIRCLE_NUM)*2);
            displayCircleRadius[i]=getMeasuredWidth()/2/5*(i+1);
            circleAnimators[i] = ObjectAnimator
                    .ofFloat(this, "yc", circleRadius[i] / 3.5f, getMeasuredWidth()/(2+(float)i*i*i/4))
                    .setDuration(1400);
            circleAnimators[i].setRepeatCount(ValueAnimator.INFINITE);
            circleAnimators[i].setInterpolator(new AccelerateInterpolator((float) (i+1)/CIRCLE_NUM));
            circleAnimators[i].start();
            final int finalI = i;
            circleAnimators[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animatorValues[finalI] = (Float) animation.getAnimatedValue();
//                    Log.d("yc", finalI + "===>" + animatorValues[finalI]/
//                            (getMeasuredWidth()/2));
                    float percent = (animatorValues[finalI]-circleRadius[finalI]/3.5f) / (getMeasuredWidth()/(2+(float)finalI*finalI*finalI/4)-circleRadius[finalI]/3.5f);
                    int alpha;
                    if (percent<0.5f){
                        alpha= (int) (255*(percent*2)*(1-(float)finalI/4));
                    }else{
                        alpha= (int) (255*(1-percent)*2*(1-(float)finalI/4));
                    }
//                    Log.d("yc", "i===>"+finalI+"percent===>" + percent + "alpha====>" + alpha);
                    circlePaints[finalI].setAlpha(alpha);
                    invalidate();
                }
            });
            circleAnimators[i].addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Log.d("yc","onAnimationStart");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d("yc","onAnimationEnd");
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    Log.d("yc","onAnimationCancel");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    Log.d("yc","onAnimationRepeat");
                    //判断是否终止了动画，保证一次动画可以完整走完
                    if (isShouldStopAnimator()){
                        setShouldStopAnimator(false);
                        setPlayAnimator(false);
                        ObjectAnimator objectAnimator= (ObjectAnimator) animation;
                        objectAnimator.removeAllUpdateListeners();
                        objectAnimator.removeAllListeners();
                        objectAnimator.cancel();
                    }
                }
            });

        }
    }

    public void stopLoading(){
        setShouldStopAnimator(true);
    }

    public boolean isShouldStopAnimator() {
        return shouldStopAnimator;
    }

    public void setShouldStopAnimator(boolean shouldStopAnimator) {
        this.shouldStopAnimator = shouldStopAnimator;
    }

    public boolean isPlayAnimator() {
        return isPlayAnimator;
    }

    public void setPlayAnimator(boolean playAnimator) {
        isPlayAnimator = playAnimator;
    }

    private int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

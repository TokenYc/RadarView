package net.archeryc.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author 杨晨 on 2017/6/22 08:48
 * @e-mail 247067345@qq.com
 * @see [相关类/方法](可选)
 */

public class RadarView extends FrameLayout implements RadarLoadingView.OnLoadingStateChangedListener {

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;

    private static final int CIRCLE_NUM = 5;

    private int defaultWidth;
    private int defaultHeight;

    private Context mContext;

    private float[] circleRadius;

    private Paint circlePaint;

    private boolean isDrawBgCircle = false;

    private RadarLoadingView radarLoadingView;
    private List<RadarUserView> radarUserViews;
    private List<RadarUserEntity> radarUserEntities;

    private List<Point> circlePositions;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        circleRadius = new float[CIRCLE_NUM];
        circlePositions = new ArrayList<>();
        initPaint();
        defaultWidth = dip2px(mContext, DEFAULT_WIDTH);
        defaultHeight = dip2px(mContext, DEFAULT_HEIGHT);
        radarLoadingView = new RadarLoadingView(mContext);
        radarLoadingView.setOnLoadingStateChangedListener(this);
        addView(radarLoadingView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    private void initPosition() {

        circlePositions.clear();
        circlePositions.add(calculatePointPosition(1,30));
        circlePositions.add(calculatePointPosition(2,90));
        circlePositions.add(calculatePointPosition(3,180));
        circlePositions.add(calculatePointPosition(2,280));

    }


    private void initPaint() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(dip2px(mContext, 1));
    }

    private void initCircleRadius() {
        circleRadius[0] = dip2px(mContext, 34);
        circleRadius[1] = dip2px(mContext, 68);
        circleRadius[2] = dip2px(mContext, 118);
        circleRadius[3] = getMeasuredWidth() / 2;
        circleRadius[4] = dip2px(mContext, 248);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("yc", "onLayout:" + "left===>" + left + "top====>" + top + "right====>" + right + "bottom===>" + bottom);
        if (radarUserViews != null && radarUserViews.size() > 0) {
            int width = radarUserViews.get(0).getMeasuredWidth();
            int height = radarUserViews.get(0).getMeasuredHeight();
            Log.d("yc", "width===>" + width + "height===>" + height);
            for (int i=0;i<radarUserViews.size();i++){
                if (i<circlePositions.size()) {
                    Point point = circlePositions.get(i);
                    radarUserViews.get(i).layout(point.x - width / 2, point.y - height / 2, point.x + width / 2, point.y + height / 2);
                    radarUserViews.get(i).startRipple();
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initCircleRadius();
        initPosition();
    }

    public void setUserData(List<RadarUserEntity> userEntities) {
        if (radarUserViews == null) {
            radarUserViews = new ArrayList<>();
        }
        this.radarUserEntities = userEntities;
        for (int i = 0; i < userEntities.size(); i++) {
            RadarUserEntity userEntity = userEntities.get(i);
            RadarUserView radarUserView = new RadarUserView(mContext);
            radarUserViews.add(radarUserView);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDrawBgCircle()) {
            for (int i = 0; i < CIRCLE_NUM; i++) {
                circlePaint.setAlpha((int) (255 * (1 - (float) i / CIRCLE_NUM)));
                canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, circleRadius[i], circlePaint);
            }
        }
    }

    /**
     * 显示加载动画
     */
    public void showLoading() {
        radarLoadingView.showLoading();
    }

    /**
     * 停止加载动画
     */
    public void stopLoading() {
        radarLoadingView.stopLoading();
    }


    @Override
    public void onLoadingStart() {
        Log.d("yc", "onLoadingStart");
        removeAllViews();
        addView(radarLoadingView);
        setDrawBgCircle(false);
        invalidate();
    }

    @Override
    public void onLoadingStop() {
        Log.d("yc", "onLoadingStop");
        removeAllViews();
        for (RadarUserView radarUserView : radarUserViews) {
            addView(radarUserView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        requestLayout();
        setDrawBgCircle(true);
        invalidate();
    }

    public boolean isDrawBgCircle() {
        return isDrawBgCircle;
    }

    public void setDrawBgCircle(boolean drawBgCircle) {
        isDrawBgCircle = drawBgCircle;
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     *
     * @param degree 0-360
     * @return
     */
    private Point calculatePointPosition(int circlePosition,int degree) {
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;
        Point point = new Point();
        if (degree >= 0 && degree < 90) {
            point.x = centerX + (int) (circleRadius[circlePosition] * Math.sin(Math.PI * (float) 60 / 180));
            point.y = centerY - (int) (circleRadius[circlePosition] * Math.cos(Math.PI * 60 / 180));
        } else if (degree >= 90 && degree < 180) {
            point.x = centerX + (int) (circleRadius[circlePosition] * Math.sin(Math.PI * (float) 60 / 180));
            point.y = centerY + (int) (circleRadius[circlePosition] * Math.cos(Math.PI * 60 / 180));
        } else if (degree >= 180 && degree <= 270) {
            point.x = centerX - (int) (circleRadius[circlePosition] * Math.sin(Math.PI * (float) 60 / 180));
            point.y = centerY + (int) (circleRadius[circlePosition] * Math.cos(Math.PI * 60 / 180));
        } else if (degree >= 270 && degree <= 360) {
            point.x = centerX - (int) (circleRadius[circlePosition] * Math.sin(Math.PI * (float) 60 / 180));
            point.y = centerY - (int) (circleRadius[circlePosition] * Math.cos(Math.PI * 60 / 180));
        }
        return point;
    }

}

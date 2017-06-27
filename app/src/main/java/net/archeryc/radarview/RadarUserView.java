package net.archeryc.radarview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author 杨晨 on 2017/6/22 10:05
 * @e-mail 247067345@qq.com
 * @see [相关类/方法](可选)
 */

public class RadarUserView extends FrameLayout {

    private Context mContext;

    private View view;

    private RippleView rippleView;

    private SimpleDraweeView sdvAvatar;

    public RadarUserView(@NonNull Context context) {
        this(context, null);
    }

    public RadarUserView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarUserView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        view = LayoutInflater.from(mContext).inflate(R.layout.radar_user, this, false);
        rippleView = (RippleView) view.findViewById(R.id.rippleView);
        sdvAvatar = (SimpleDraweeView) view.findViewById(R.id.avatar);
        addView(view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnAnimatorEndListener(RippleView.OnAnimatorEndListener onAnimatorEndListener) {
        rippleView.setOnAnimatorEndListener(onAnimatorEndListener);
    }

    public void startRipple() {
        rippleView.initAnimator(sdvAvatar.getMeasuredWidth() / 2, getMeasuredWidth() / 2, sdvAvatar.getMeasuredWidth() / 2, 0);
        rippleView.startRipple();
    }

    public void stopRipple() {
        rippleView.stopRipple();
    }

    public void clear() {
        rippleView.clear();
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

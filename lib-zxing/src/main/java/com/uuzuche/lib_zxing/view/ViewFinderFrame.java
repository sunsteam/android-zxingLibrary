package com.uuzuche.lib_zxing.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.uuzuche.lib_zxing.DisplayUtil;
import com.uuzuche.lib_zxing.R;


/**
 * 画扫描线的控件, 包裹在ViewfinderView外
 * Created by Yomii on 2017/5/22.
 */

public class ViewFinderFrame extends FrameLayout {

    /**
     * 扫描线
     */
    private Drawable scanLight;

    /**
     * 扫描线移动速度
     */
    private int scanVelocity;

    private int innerMarginTop;
    private int width;
    private int height;

    public ViewFinderFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewFinderFrame(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.innerrect);
        // 扫描控件
        //scanLight = BitmapFactory.decodeResource(getResources(), ta.getResourceId(R.styleable.innerrect_inner_scan_bitmap, R.drawable.scan_light));

        // 扫描框距离顶部
        innerMarginTop = (int) ta.getDimension(R.styleable.innerrect_inner_margintop, 0);
        // 扫描框的宽度
        width = (int) ta.getDimension(R.styleable.innerrect_inner_width, DisplayUtil.screenWidthPx / 2);
        // 扫描框的高度
        height = (int) ta.getDimension(R.styleable.innerrect_inner_height, DisplayUtil.screenWidthPx / 2);

        scanLight = ta.getDrawable(R.styleable.innerrect_inner_scan_bitmap);
        if (scanLight == null)
            scanLight = ActivityCompat.getDrawable(getContext(), R.drawable.scan_light);
        // 扫描速度
        scanVelocity = ta.getInt(R.styleable.innerrect_inner_scan_speed, 1000);

        ta.recycle();

    }

    private void startScanLight(Rect framingRect) {
        if (framingRect == null)
            return;

        ImageView scanLightView = new ImageView(getContext());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources()
                .getDisplayMetrics());
        LayoutParams layoutParams = new LayoutParams(framingRect.width(), height);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        scanLightView.setImageDrawable(scanLight);
        addView(scanLightView, layoutParams);
        ObjectAnimator animator = ObjectAnimator.ofFloat(scanLightView, "translationY",
                framingRect.top, framingRect.bottom - height);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(scanVelocity);
        animator.start();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View finderView = getChildAt(0);
        if (!(finderView instanceof ViewfinderView))
            throw new IllegalStateException("first child must be ViewFinderView");

        Rect rect = new Rect();
        rect.top = innerMarginTop;
        rect.bottom = innerMarginTop + height;
        rect.left = (DisplayUtil.screenWidthPx - width) / 2;
        rect.right = rect.left + width;
        startScanLight(rect);
    }

}

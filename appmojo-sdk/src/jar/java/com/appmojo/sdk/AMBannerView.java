package com.appmojo.sdk;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appmojo.sdk.base.AMAdNetwork;
import com.appmojo.sdk.base.AMAdSize;

/**
 * Created by nutron on 6/23/15 AD.
 */
public class AMBannerView extends ViewGroup implements AMView {

    private AMController mAMController;
    private String mPlacementUid;
    private AMListener mListener;
    private AMAdSize mAdSize = AMAdSize.BANNER;
    private Context mContext;

    public AMBannerView(Context context) {
        super(context);
        initView(context);
    }

    public AMBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AMBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        mContext = context;
        if (!isInEditMode()) {
            mAMController = AMViewControllerFactory.create(mContext, this, AMAdType.BANNER);
        }
        initPreview(context);

    }

    public void setAdSize(AMAdSize size) {
        mAdSize = size;
    }

    public AMAdSize getAdSize() {
        return this.mAdSize;
    }

    public int getRefreshRate() {
        return ((AMBannerController) this.mAMController).getRefreshRate();
    }

    public void setListener(AMBannerListener listener) {
        mListener = listener;
    }

    @Override
    public void setPlacementUid(String placementUid) {
        mPlacementUid = placementUid;
    }

    @Override
    public String getPlacementUid() {
        return mPlacementUid;
    }

    @Override
    public AMListener getListener() {
        return mListener;
    }

    @Override
    public String getCurrentAdUnitId() {
        return mAMController.getCurrentAdUnitId();
    }

    @Override
    public AMAdNetwork getAdNetwork() {
        return mAMController.getAdNetwork();
    }

    @Override
    public void reloadAd() {
        mAMController.reloadAd();
    }

    @Override
    public void loadAd(AMAdRequest adRequest) {
        mAMController.loadAd(adRequest);
    }

    @Override
    public void destroy() {
        mAMController.onDestroy();
        mContext = null;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy();
    }

    private void initPreview(Context context) {
        if (isInEditMode()) {
            FrameLayout frame = new FrameLayout(context);
            TextView textView = new TextView(context);
            textView.setGravity(17);
            textView.setTextColor(0xff512DA8);
            textView.setText("Ads by AppMojo");

            ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
            shapeDrawable.getPaint().setColor(0xFF512DA8);
            shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
            shapeDrawable.getPaint().setStrokeWidth(5);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                //noinspection deprecation
                textView.setBackgroundDrawable(shapeDrawable);
            } else {
                textView.setBackground(shapeDrawable);
            }

            int[] sizePixels = AMAdSize.getAdSizePixel(mContext, getAdSize());
            frame.addView(textView, new FrameLayout.LayoutParams(
                    sizePixels[0], sizePixels[1], Gravity.CENTER));
            this.addView(frame, sizePixels[0], sizePixels[1]);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View child = this.getChildAt(0);
        if (child != null && child.getVisibility() != GONE) {
            int childW = child.getMeasuredWidth();
            int childH = child.getMeasuredHeight();
            int childLeft = (right - left - childW) / 2;
            int childTop = (bottom - top - childH) / 2;
            child.layout(childLeft, childTop, childLeft + childW, childTop + childH);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childW;
        int childH;
        View child = this.getChildAt(0);
        if (child != null && child.getVisibility() != GONE) {
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
            childW = child.getMeasuredWidth();
            childH = child.getMeasuredHeight();
        } else {
            int[] sizePixels = AMAdSize.getAdSizePixel(mContext, getAdSize());
            childW = sizePixels[0];
            childH = sizePixels[1];
        }

        childW = Math.max(childW, this.getSuggestedMinimumWidth());
        childH = Math.max(childH, this.getSuggestedMinimumHeight());
        this.setMeasuredDimension(View.resolveSize(childW, widthMeasureSpec),
                View.resolveSize(childH, heightMeasureSpec));
    }

    AMController getController() {
        return mAMController;
    }

}

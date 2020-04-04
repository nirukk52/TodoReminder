package com.example.android.architecture.blueprints.todoapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;

public class FixedAspectRatioFrameLayout extends FrameLayout
{
    private static final String TAG = "FixedAspectRatioFrameLa";
    private int mAspectRatioWidth;
    private int mAspectRatioHeight;

    public FixedAspectRatioFrameLayout(Context context)
    {
        super(context);
    }

    public FixedAspectRatioFrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context);
    }

    public FixedAspectRatioFrameLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context)
    {
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedAspectRatioFrameLayout);

        int[] ratio = getDeviceRatio(getScreenWidth(),getScreenHeight());
        mAspectRatioWidth = 9;
        mAspectRatioHeight = 16;


        Log.d(TAG,getScreenResolution(context));
        Log.d(TAG,mAspectRatioWidth + " " + mAspectRatioHeight);
//        a.recycle();
    }

    private String getScreenResolution(Context context) {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return "" + width + "  " + height;
    }

    // **overrides**

    @Override protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);

        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);

        int calculatedHeight = originalWidth * mAspectRatioHeight / mAspectRatioWidth;

        int finalWidth, finalHeight;

        if (calculatedHeight > originalHeight)
        {
            finalWidth = originalHeight * mAspectRatioWidth / mAspectRatioHeight;
            finalHeight = originalHeight;
        }
        else
        {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
        }

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


//    private int getNavigationBarHeight() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            DisplayMetrics metrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            int usableHeight = metrics.heightPixels;
//            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
//            int realHeight = metrics.heightPixels;
//            if (realHeight > usableHeight)
//                return realHeight - usableHeight;
//            else
//                return 0;
//        }
//        return 0;
//    }
    int gcd(int p, int q) {
        if (q == 0) return p;
        else return gcd(q, p % q);
    }

    private int[] getDeviceRatio(int a, int b) {
        final int gcd = gcd(a,b);
        int[] ratio = new int[2];
        if(a > b) {
            ratio[0] = a/gcd;
            ratio[1] = b/gcd;
        } else {
            ratio[0] = b/gcd;
            ratio[1] = a/gcd;
        }
        return ratio;
    }

}
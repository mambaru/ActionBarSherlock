package com.actionbarsherlock.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.reflect.Field;

public class PopupWindow extends android.widget.PopupWindow {

    public PopupWindow(Context context) {
        super(context);
        fixScrollListenerNPE();
    }

    public PopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        fixScrollListenerNPE();
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        fixScrollListenerNPE();
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        fixScrollListenerNPE();
    }

    public PopupWindow() {
        super();
        fixScrollListenerNPE();
    }

    public PopupWindow(View contentView) {
        super(contentView);
        fixScrollListenerNPE();
    }

    public PopupWindow(int width, int height) {
        super(width, height);
        fixScrollListenerNPE();
    }

    public PopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        fixScrollListenerNPE();
    }

    public PopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        fixScrollListenerNPE();
    }

    private void fixScrollListenerNPE() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                final Field fAnchor = PopupWindow.class.getDeclaredField("mAnchor");
                fAnchor.setAccessible(true);
                Field listener = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
                listener.setAccessible(true);
                final ViewTreeObserver.OnScrollChangedListener originalListener = (ViewTreeObserver.OnScrollChangedListener) listener.get(PopupWindow.this);
                ViewTreeObserver.OnScrollChangedListener newListener = new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        try {
                            View mAnchor = (View) fAnchor.get(PopupWindow.this);
                            if (mAnchor != null) {
                                originalListener.onScrollChanged();
                            }
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                listener.set(PopupWindow.this, newListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

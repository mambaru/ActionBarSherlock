package com.actionbarsherlock.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;
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
                final Field fAnchor = getClass().getSuperclass().getDeclaredField("mAnchor");
                fAnchor.setAccessible(true);
                Field listener = getClass().getSuperclass().getDeclaredField("mOnScrollChangedListener");
                listener.setAccessible(true);
                final ViewTreeObserver.OnScrollChangedListener originalListener = (ViewTreeObserver.OnScrollChangedListener) listener.get(this);
                ViewTreeObserver.OnScrollChangedListener newListener = new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        try {
                            WeakReference<View> mAnchor = (WeakReference<View>) fAnchor.get(this);
                            View anchor = mAnchor != null ? mAnchor.get() : null;
                            if (anchor != null) {
                                originalListener.onScrollChanged();
                            }
                        } catch (IllegalAccessException ex) {
                            Log.e("MambaPopupWindow", "", ex);
                        }
                    }
                };
                listener.set(PopupWindow.this, newListener);
            } catch (Exception e) {
                Log.e("MambaPopupWindow", "", e);
            }
        }
    }
}

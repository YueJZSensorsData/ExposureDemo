package com.example.yuejz.exposuredemo;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yuejz on 2018/12/6
 **/
public class MyButton extends AppCompatButton {

    public static final String TAG = "MyButton";

    public MyButton(Context context) {
        this(context, null);
    }

    public MyButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            Log.d(TAG, "可见");
        } else {
            Log.d(TAG, "不可见");
        }
    }

    protected boolean isCover() {

        boolean cover = false;
        Rect rect = new Rect();
        cover = getGlobalVisibleRect(rect);
        if (cover) {
            if (rect.width() >= getMeasuredWidth() && rect.height() >= getMeasuredHeight()) {
                return !cover;
            }
        }
        return true;
    }


}

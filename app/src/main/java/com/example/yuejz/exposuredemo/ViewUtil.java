package com.example.yuejz.exposuredemo;

import android.graphics.Rect;
import android.view.View;

/**
 * Created by yuejz on 2018/12/6
 **/
public class ViewUtil {
    public static boolean isVisible(View view) {
        if (view.getVisibility() != View.VISIBLE ) {
            return false;
        }

        if (!view.isShown()) {
            return false;
        }

        if (!isVisibleLocal(view)) {
            return false;
        }

        return true;
    }

    /**
     *
     * @param view
     * @return
     */
    public static boolean isVisibleLocal(View view){
        return view.getGlobalVisibleRect(new Rect());
    }
}
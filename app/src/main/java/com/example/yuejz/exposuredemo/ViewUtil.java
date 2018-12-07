package com.example.yuejz.exposuredemo;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

/**
 * Created by yuejz on 2018/12/6
 **/
public class ViewUtil {
    private static final String TAG = "ViewUtil";
    public static boolean isVisible(View view) {
        if (view.getVisibility() != View.VISIBLE ) {
            Log.d(TAG, "VISIBLE");
            return false;
        }
        if (!view.isShown()) {
            Log.d(TAG, "isShown");
            return false;
        }
        if (isCover(view)) {
            Log.d(TAG, "isCover");
            return false;
        }
        if (!isVisibleLocal(view)) {
            Log.d(TAG, "isVisibleLocal");
            return false;
        }

        return true;
    }


    //当 View 有一点点不可见时立即返回false!
    public static boolean isVisibleLocal(View view){
        Rect rect =new Rect();
        view.getLocalVisibleRect(rect);
        return rect.top==0;
    }

    /**
     * true 表示部分在屏幕中，不可见
     * false 表示全部在屏幕中，可见
     * @param view
     * @return
     */
    public static boolean isCover(View view) {
        boolean cover = false;
        Rect rect = new Rect();
        cover = view.getGlobalVisibleRect(rect);
        if (cover) {
            //Rect表示在屏幕中的尺寸
            if (rect.width() >= view.getMeasuredWidth() && rect.height() >= view.getMeasuredHeight()) {
                //返回 false
                return !cover;
            }
        }
        return true;
    }
}

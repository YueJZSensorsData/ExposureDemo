@file:JvmName("ViewUtil")
package com.example.yuejz.exposuredemo

import android.graphics.Rect
import android.view.View


/**
 * view 自身是否可见
 *
 * @param view 对象
 * @return true 代表可见，false 代表不可见
 */
fun isVisible(view: View):Boolean {

    if (view == null) {
        return false
    }

    if (view.width <= 0 || view.height <= 0 || view.alpha <= 0.0f || !view.getLocalVisibleRect(Rect())) {
        return false
    }

    if (!view.isShown) {
        return false
    }

    if (view.visibility != View.VISIBLE && view.animation != null && view.animation.fillAfter) {
        return true
    }

    return view.visibility == View.VISIBLE
}


package com.cj.library.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat


object ViewUtil {
    private val TINT_ATTRS = intArrayOf(
            android.R.attr.backgroundTint, //in v7
            android.R.attr.backgroundTintMode) //in v7

    fun fixTopPadding(view: View, withStatusBarHeight: Boolean = true, extraPadding: Int = 0) {
        if (Build.VERSION.SDK_INT >= 19) {
            var paddingTop = view.paddingTop + extraPadding
            if (withStatusBarHeight) {
                paddingTop += getStatusBarHeight(view.context)
            }
            view.setPadding(view.paddingLeft,
                    paddingTop,
                    view.paddingRight,
                    view.paddingBottom)
        }
    }

    fun fixTopMargin(view: View, withStatusBarHeight: Boolean = true, extraMargin: Int = 0) {
        if (Build.VERSION.SDK_INT >= 19) {
            val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
            var topMargin = params.topMargin + extraMargin
            if (withStatusBarHeight) {
                topMargin += getStatusBarHeight(view.context)
            }
            params.topMargin = topMargin
            view.layoutParams = params
        }
    }

    fun getPaddings(view: View): Rect {
        return Rect(view.paddingLeft, view.paddingTop, view.paddingRight,
                view.paddingBottom)
    }

    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        return resources.getDimensionPixelSize(ResourceUtil.getResourceId(context,"status_bar_height", "dimen", "android"))
    }

    fun getNavigationBarHeight(context: Context): Int {
        val resources = context.resources
        return resources.getDimensionPixelSize(ResourceUtil.getResourceId(context,"navigation_bar_height", "dimen", "android"))
    }

    fun setPaddings(view: View, rect: Rect) {
        view.setPadding(rect.left, rect.top, rect.right, rect.bottom)
    }

    fun unfixTopPadding(view: View) {
        if (Build.VERSION.SDK_INT >= 19) {
            view.setPadding(view.paddingLeft,
                    view.paddingTop - getStatusBarHeight(view.context),
                    view.paddingRight, view.paddingBottom)
        }
    }

    @SuppressLint("ResourceType")
    fun supportTint(view: View, attrs: AttributeSet) {
        val a = view.context.obtainStyledAttributes(attrs, TINT_ATTRS)
        if (a.hasValue(0)) {
            //set backgroundTint
            val colorStateList = a.getColorStateList(0)
            ViewCompat.setBackgroundTintList(view, colorStateList)
        }
        if (a.hasValue(1)) {
            //set backgroundTintMode
            val mode = a.getInt(1, -1)
            ViewCompat.setBackgroundTintMode(view, parseTintMode(mode, null))
        }
        a.recycle()
    }

    /**
     * Parses a [android.graphics.PorterDuff.Mode] from a tintMode
     * attribute's enum value.
     *
     * @hide
     */
    fun parseTintMode(value: Int, defaultMode: PorterDuff.Mode?): PorterDuff.Mode? {
        when (value) {
            3 -> return PorterDuff.Mode.SRC_OVER
            5 -> return PorterDuff.Mode.SRC_IN
            9 -> return PorterDuff.Mode.SRC_ATOP
            14 -> return PorterDuff.Mode.MULTIPLY
            15 -> return PorterDuff.Mode.SCREEN
            16 -> return PorterDuff.Mode.ADD
            else -> return defaultMode
        }
    }
}
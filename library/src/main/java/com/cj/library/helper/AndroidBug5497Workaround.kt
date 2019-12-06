package com.cj.library.helper

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout

/**
 *最近在给项目添加沉浸式状态栏时遇到了一个很奇葩的问题，在Android4.4以上系统底部聊天及评论框不能被系统输入法顶上去
 * 使用AndroidBug5497Workaround 类动态计算布局的高度
 */
class AndroidBug5497Workaround private constructor(activity: Activity) {
    private val mChildOfContent: View
    private var usableHeightPrevious: Int = 0
    private val frameLayoutParams: FrameLayout.LayoutParams

    private var initheight: Int = 0
    private var listener: ViewTreeObserver.OnGlobalLayoutListener? = null

    init {
        val content = activity.findViewById<View>(android.R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)
        listener = ViewTreeObserver.OnGlobalLayoutListener { possiblyResizeChildOfContent() }
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener(listener)
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }


    fun unRegisterLayoutListener() {
        if (listener != null && Build.VERSION.SDK_INT >= 19) {
            mChildOfContent.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    fun registerLayoutListener() {
        if (listener != null && Build.VERSION.SDK_INT >= 19) {
            mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener(listener)
        }
    }

    private fun possiblyResizeChildOfContent() {
        val tempheight = mChildOfContent.height
        if (initheight < tempheight) {
            initheight = tempheight
        }

        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {

            val usableHeightSansKeyboard = mChildOfContent.rootView.height
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {

                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
            } else {
                frameLayoutParams.height = initheight
            }
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top
    }

    companion object {
        fun assistActivity(activity: Activity):  AndroidBug5497Workaround{
            return AndroidBug5497Workaround(activity)
        }
    }
}
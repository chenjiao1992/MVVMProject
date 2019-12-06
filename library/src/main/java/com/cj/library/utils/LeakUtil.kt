package com.cj.library.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Field

object LeakUtil {
    private val TEXT_LINE_CACHED: Field?
    init {
        var textLineCached: Field? = null
        try {
            textLineCached = Class.forName("android.text.TextLine").getDeclaredField("sCached")
            textLineCached!!.isAccessible = true
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        TEXT_LINE_CACHED = textLineCached
    }

    fun fixInputManagerLeak(context: Context) {
        val inputManager = context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val inputManagerClass = inputManager::class.java
        val params = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        for (param in params) {
            try {
                val field = inputManagerClass.getDeclaredField(param) ?: continue
                field.isAccessible = true
                val target = field.get(inputManager) as? View ?: continue
                if (target.context == context) {
                    field.set(inputManager, null)
                } else {
                    break
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearTextLineCache() {
        // If the field was not found for whatever reason just return.
        if (TEXT_LINE_CACHED == null) return
        var cached: Any? = null
        try {
            // Get reference to the TextLine sCached array.
            cached = TEXT_LINE_CACHED.get(null)
            if (cached != null) {
                // Clear the array.
                var i = 0

                val size = java.lang.reflect.Array.getLength(cached)
                while (i < size) {
                    java.lang.reflect.Array.set(cached, i, null)
                    i++
                }
            }
        } catch (ex: Exception) {
        }
    }
}
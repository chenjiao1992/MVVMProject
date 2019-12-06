package com.cj.library.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue

/**
 * 由 Harreke 创建于 2017/11/27.
 */
@Suppress("DEPRECATION", "UNCHECKED_CAST")
object ResourceUtil {
    fun obtainResourceId(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.resourceId
    }

    fun obtainResourceIds(context: Context, vararg attrs: Int): IntArray {
        val resources = IntArray(attrs.size)
        val typedValue = TypedValue()
        val theme = context.theme
        attrs.forEachIndexed { index, attr ->
            theme.resolveAttribute(attr, typedValue, true)
            resources[index] = typedValue.resourceId
        }
        return resources
    }

    fun obtainData(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

    fun obtainDatas(context: Context, vararg attrs: Int): IntArray {
        val datas = IntArray(attrs.size)
        val typedValue = TypedValue()
        val theme = context.theme
        attrs.forEachIndexed { index, attr ->
            theme.resolveAttribute(attr, typedValue, true)
            datas[index] = typedValue.data
        }
        return datas
    }

    fun obtainDrawable(context: Context, attr: Int): Drawable {
        return context.resources.getDrawable(obtainResourceId(context, attr))
    }

    fun obtainDrawables(context: Context, vararg attrs: Int): Array<Drawable> {
        val resourceIds = obtainResourceIds(context, *attrs)
        val colors = arrayOfNulls<Drawable>(attrs.size)
        val resource = context.resources
        resourceIds.forEachIndexed { index, resourceId ->
            colors[index] = resource.getDrawable(resourceId)
        }
        return colors as Array<Drawable>
    }

    fun obtainColor(context: Context, attr: Int) = obtainData(context, attr)

    fun obtainColors(context: Context, vararg attrs: Int) = obtainDatas(context, *attrs)

    fun getResourceId(context:Context?,resId: String, resName: String,packageName:String?): Int {
        if(context==null){
            return -1
        }
        return try {
            context.resources .getIdentifier(resId, resName, packageName?:context.packageName)
        } catch (e: Exception) {
            -1
        }
    }
}
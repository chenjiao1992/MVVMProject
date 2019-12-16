package com.cj.library.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.cj.library.model.DataObserver
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer

/**
 * Create by chenjiao at 2019/11/27 0027
 * 描述：
 */
internal interface IBaseFramework {
    val isActivity: Boolean
    val activity: FragmentActivity
    val fragmentManagerEx: FragmentManager
    var toolbar: Toolbar
    /**
     * 准备完毕,可以加载数据,例如从网络上加载数据
     */
    fun onPrepared()

    /**
     * 刷新
     */
    fun onRefresh()

    fun <T : View> findViewById(viewId: Int): T

    fun setRefreshThreshold(refreshThreshold: Long?)

    fun onToolbarPrepare(toolbar: Toolbar, menu: Menu)

    fun onToolbarNavigationClick()

    fun onToolbarMenuItemClick(menuItem: MenuItem): Boolean

    fun observeData(key: String, observer: Observer<Any>)

    fun observeData(observer: DataObserver)

    fun dispatchData(key: String, `object`: Any)

    fun queryData(key: String): Any

    fun observeCommand(name: String, observer: Observer<*>)

    fun observeCommand(observer: DataObserver)

    fun dispatchCommand(name: String, data: Any)

}

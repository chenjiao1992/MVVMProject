package com.cj.library.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cj.library.model.DataObserver;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
interface IBaseFramework {
    /**
     初始化,做findViewByID等控件的初始化操作
     */
    void onConfig(Bundle arguments);

    /**
     准备完毕,可以加载数据,例如从网络上加载数据
     */
    void onPrepared();

    /**
     填充的布局
     */
    View doInflate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     刷新
     */
    void onRefresh();

    boolean isActivity();

    FragmentActivity getActivity();

    FragmentManager getFragmentManagerEx();

    <T extends View> T findViewById(int viewId);

    void setRefreshThreshold(Long refreshThreshold);

    void setToolbar(Toolbar toolbar);

    Toolbar getToolbar();

    void onToolbarPrepare(Toolbar toolbar, Menu menu);

    void onToolbarNavigationClick();

    boolean onToolbarMenuItemClick(MenuItem menuItem);

    void observeData(String key, Observer<Object> observer);

    void observeData(DataObserver observer);

    void dispatchData(String key, Object object);

    Object queryData(String key);

    void observeCommand(String name, Observer observer);

    void observeCommand(DataObserver observer);

    void dispatchCommand(String name, Object data);

}

package com.cj.library.base;

import android.view.Menu;
import android.view.View;

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
     初始化
     */
    void onConfig();

    /**
     准备完毕
     */
    void onPrepared();

    /**
     刷新
     */
    void onRefresh();

    boolean isActivity();

    FragmentActivity getActivity();

    FragmentManager getFragmentManager();

    <T extends View> T findViewById(int viewId);

    void setRefreshThreshold(Long refreshThreshold);

    void setToolbar(Toolbar toolbar);

    Toolbar getToolbar();

    void onToolbarPrepare(Toolbar toolbar, Menu menu);

    void onToolbarNavigationClick();

    void onToolbarMenuItemClick();

    void observeData(String key, Observer<Object> observer);

    void observeData(DataObserver observer);

    void dispatchData(String key, Object object);

    Object queryData(String key);

    void observeCommand(String name, Observer observer);

    void observeCommand(DataObserver observer);

    void dispatchCommand(String name, Object data);

}

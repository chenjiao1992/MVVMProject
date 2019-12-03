package com.cj.library.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cj.library.R;
import com.cj.library.helper.ActivityDataHelper;
import com.cj.library.helper.FragmentFrameworkHelper;
import com.cj.library.helper.RefreshHelper;
import com.cj.library.model.DataObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public abstract class BaseFragment extends Fragment implements IBaseFramework, IFragmentData, IOtherFragmentData {
    private RefreshHelper mRefreshHelper = new RefreshHelper();
    private FragmentFrameworkHelper mFragmentFrameworkHelper = new FragmentFrameworkHelper();
    private Toolbar mToolbar = null;
    private boolean mViewCreated = false;
    private ActivityDataHelper mActivityDataHelper = new ActivityDataHelper();
    private long mLaunchDelay = 300L;
    private Runnable mLaunchRunnable = new Runnable() {
        @Override
        public void run() {
            onPrepared();
        }
    };
    private CharSequence toolbarTittle = null;

    public CharSequence getToolbarTittle() {
        return mToolbar.getTitle();
    }

    public void setToolbarTittle(CharSequence toolbarTittle) {
        this.toolbarTittle = toolbarTittle;
        mToolbar.setTitle(toolbarTittle);
    }


    boolean isInViewPager() {
        return false;
    }

    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return doInflate(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isInViewPager() && !getUserVisibleHint()) {//todo  对这里的逻辑不是很理解
            return;
        }
        onPreparedOrRefresh();
    }

    private void onPreparedOrRefresh() {
        mRefreshHelper.resume();
        if (mRefreshHelper.firstEnter()) {
            onPrepared();
        } else if (mRefreshHelper.shouldRefresh()) {
            onRefresh();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isInViewPager() || !mViewCreated)
            return;
        if (!isVisibleToUser) {
            mRefreshHelper.pause();
        } else {
            onPreparedOrRefresh();
        }
    }

    @Override
    public void onPause() {
        mRefreshHelper.pause();
        super.onPause();
    }

    @Override
    public void onReceiveDataFromActivity(String name, Object data) {

    }

    @Override
    public void sendDataToActivity(String name, Object data) {
        mFragmentFrameworkHelper.sendDataToActivity(this, name, data);
    }

    @Override
    public Object onQueryDataFromActivity(String name, Object data) {
        return null;
    }

    @Override
    public Object queryActivityData(String name, Object data) {
        return mFragmentFrameworkHelper.queryActivityData(this, name, data);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public boolean isActivity() {
        return false;
    }

    @Override
    public <T extends View> T findViewById(int viewId) {
        try {
            return getView().findViewById(viewId);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public FragmentManager getFragmentManagerEx() {
        return getChildFragmentManager();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewCreated = true;
        if (savedInstanceState != null) {
            recreateData(savedInstanceState);
        }
        Toolbar toolbar = findViewById(getToolbarId());
        if (toolbar != null) {
            setToolbar(toolbar);
        }

        onConfig(savedInstanceState);
        if (toolbar != null) {
            onToolbarPrepare(toolbar, toolbar.getMenu());
        }
    }

    @Override
    public void onDestroyView() {
        resetToolbar();
        mActivityDataHelper.destroy(this);
        mViewCreated = false;
        mRefreshHelper.destory();
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFragmentFrameworkHelper.onAttach(context);
    }

    @Override
    public void onDetach() {
        mFragmentFrameworkHelper.onDetach();
        super.onDetach();
    }

    protected void recreateData(Bundle saveInstanceState) {
    }

    @Override
    public void sendDataToOtherFragment(String receiverTag, String name, Object data) {

    }

    @Override
    public void onReceiveDataFromOtherFragment(String sendTag, String name, Object data) {

    }

    @Override
    public Object queryOtherFragmentData(String receiverTag, String name, Object data) {
        return null;
    }

    @Override
    public Object onQueryDataFromOtherFragment(String receiverTag, String name, Object data) {
        return null;
    }

    @Override
    final public void setRefreshThreshold(Long refreshThreshold) {
        mRefreshHelper.setRefreshThreshold(refreshThreshold);
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        resetToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onToolbarNavigationClick();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onToolbarMenuItemClick(item);
            }
        });

    }

    private void resetToolbar() {
        mToolbar.setNavigationOnClickListener(null);
        mToolbar.setOnMenuItemClickListener(null);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onToolbarPrepare(Toolbar toolbar, Menu menu) {

    }

    @Override
    public void onToolbarNavigationClick() {

    }

    @Override
    public boolean onToolbarMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    public void observeData(String key, Observer<Object> observer) {
        mActivityDataHelper.observeData(this, key, observer);
    }

    @Override
    public void observeData(DataObserver observer) {
        mActivityDataHelper.observeData(this, observer);
    }

    @Override
    public void dispatchData(String key, Object object) {
        mActivityDataHelper.changeData(this, key, object);
    }

    @Override
    public Object queryData(String key) {
        return mActivityDataHelper.queryData(this, key);
    }

    @Override
    public void observeCommand(String name, Observer observer) {
        mActivityDataHelper.observeCommand(this, name, observer);
    }

    @Override
    public void observeCommand(DataObserver observer) {
        mActivityDataHelper.observeCommand(this, observer);
    }

    @Override
    public void dispatchCommand(String name, Object data) {
        mActivityDataHelper.dispatchCommand(this, name, data);
    }
}

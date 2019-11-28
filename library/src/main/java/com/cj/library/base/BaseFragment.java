package com.cj.library.base;

import android.view.Menu;
import android.view.View;

import com.cj.library.helper.ActivityDataHelper;
import com.cj.library.helper.FragmentFrameworkHelper;
import com.cj.library.helper.RefreshHelper;
import com.cj.library.model.DataObserver;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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

    @Override
    public void onReceiveDataFromActivity(String name, Object data) {

    }

    @Override
    public void sendDataToActivity(String name, Object data) {

    }

    @Override
    public Object onQueryDataFromActivity(String name, Object data) {
        return null;
    }

    @Override
    public Object queryActivityData(String name, Object data) {
        return null;
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
        return null;
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
    public void setRefreshThreshold(Long refreshThreshold) {

    }

    @Override
    public void setToolbar(Toolbar toolbar) {

    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public void onToolbarPrepare(Toolbar toolbar, Menu menu) {

    }

    @Override
    public void onToolbarNavigationClick() {

    }

    @Override
    public void onToolbarMenuItemClick() {

    }

    @Override
    public void observeData(String key, Observer<Object> observer) {

    }

    @Override
    public void observeData(DataObserver observer) {

    }

    @Override
    public void dispatchData(String key, Object object) {

    }

    @Override
    public Object queryData(String key) {
        return null;
    }

    @Override
    public void observeCommand(String name, Observer observer) {

    }

    @Override
    public void observeCommand(DataObserver observer) {

    }

    @Override
    public void dispatchCommand(String name, Object data) {

    }
}

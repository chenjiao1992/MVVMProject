package com.cj.library.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.cj.library.R;
import com.cj.library.helper.ActivityDataHelper;
import com.cj.library.helper.ActivityFrameworkHelper;
import com.cj.library.helper.AndroidBug5497Workaround;
import com.cj.library.helper.RefreshHelper;
import com.cj.library.model.DataObserver;
import com.cj.library.utils.LeakUtil;

import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

/**
 Create by chenjiao at 2019/12/3 0003
 描述：
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseFramework, IActivityData, IActivityOtherFragmentData, SkinObserver {
    private AndroidBug5497Workaround mAndroidBug5497Workaround;
    private Toolbar mToolbar;
    private ActivityDataHelper mActivityDataHelper = new ActivityDataHelper();
    private ActivityFrameworkHelper mActivityFrameworkHelper = new ActivityFrameworkHelper();
    private RefreshHelper mRefreshHelper = new RefreshHelper();
    private long mLaunchDelay = 300L;

    @Override
    public void onPrepared() {

    }

    protected abstract void doInflate(BaseActivity activity, Bundle savedInstanceState);

    @Override
    public void onRefresh() {

    }

    protected void recreateData(Bundle savedInstanceState) {
    }

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }

    /**
     是否需要沉侵式状态栏
     */
    protected Boolean useInmersive() {
        return true;
    }

    protected int getToolbarId() {
        return R.id.toolbar;
    }

    protected boolean needAssistSoftInput() {
        return false;
    }

    protected boolean useLightStateBar() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //FEATURE_NO_TITLE：没有标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setUseImmersive();
        doInflate(this, savedInstanceState);
        if (useInmersive() && Build.VERSION.SDK_INT >= 19) {
            prepareImmersive();
            if (needAssistSoftInput()) {
                mAndroidBug5497Workaround = AndroidBug5497Workaround.Companion.assistActivity(this);
            }
        }
        if (savedInstanceState != null) {
            recreateData(savedInstanceState);
        }
        Toolbar toolbar = findViewById(getToolbarId());
        if (toolbar != null) {
            setToolbar(toolbar);
        }
        onConfig(getIntent());
        if (toolbar != null) {
            onToolbarPrepare(toolbar, toolbar.getMenu());
        }
    }

    /**
     做初始化相关的配置,例如findViewById相关的view相关的设置
     */
    protected abstract void onConfig(Intent arguments);

    /**
     在沉侵式状态栏时,内容以及填充到了状态栏,这个时候如果页面上要显示一个view,在状态栏之下,需要去修正margin,在view本身的topmargin上再加上状态栏的高度
     可以使用ViewUtil进行padding和margin的修正
     */
    protected void prepareImmersive() {
    }

    /**
     是否拦截Launch,如果拦截,可以在该方法中去做些例如权限请求之类的操作,权限授予成功后才执行onPrepared方法
     */
    protected boolean onInterceptLaunch() {
        return false;
    }

    @Override
    protected void onDestroy() {
        resetToolbar();
        mActivityDataHelper.destroy(this);
        mRefreshHelper.destory();
        hideInputMethod();
        LeakUtil.INSTANCE.fixInputManagerLeak(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshHelper.resume();
        if (mRefreshHelper.firstEnter()) {
            onPrepared();
        } else if (mRefreshHelper.shouldRefresh()) {
            doRefresh();
        }
    }

    protected void doRefresh() {
    }

    protected boolean isResume() {
        return mRefreshHelper.isResume();
    }

    protected void dispatchLaunch() {
        if (onInterceptLaunch()) {
            return;
        }

        if (mLaunchDelay > 0L) {
            //todo
        } else {
            onPrepared();
        }
    }

    @Override
    protected void onPause() {
        mRefreshHelper.pause();
        super.onPause();
    }

    private void hideInputMethod() {
        InputMethodManager systemService = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (systemService != null) {
            systemService.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mToolbar.setTitle(title);
    }

    private void setSubTitle(CharSequence subTitle) {
        mToolbar.setSubtitle(subTitle);
    }

    /**
     沉侵式配置
     */
    private void setUseImmersive() {
        if (useInmersive() && Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT > 22) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                getWindow().setStatusBarColor(0x60000000);
            }
        }

        if (useLightStateBar() && Build.VERSION.SDK_INT >= 23) {
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public boolean isActivity() {
        return true;
    }

    @Override
    public FragmentActivity getActivity() {
        return this;
    }

    @Override
    public FragmentManager getFragmentManagerEx() {
        return getSupportFragmentManager();
    }

    @Override
    public void setRefreshThreshold(Long refreshThreshold) {
        mRefreshHelper.setRefreshThreshold(refreshThreshold);
    }

    /**
     配置toolbar相关的设置
     */
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

        enableMenuItemIcon(toolbar.getMenu());
        mToolbar = toolbar;
    }

    /**
     在Android4.0系统中，创建菜单Menu，通过setIcon方法给菜单添加图标是无效的，图标没有显出来，2.3系统中是可以显示出来的。
     这个问题的根本原因在于4.0系统中，涉及到菜单的源码类 MenuBuilder做了改变,需要反射来实现图标显示
     */
    private void enableMenuItemIcon(Menu menu) {
        if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {

            try {
                Method setOptionalIconsVisible = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                setOptionalIconsVisible.setAccessible(true);
                setOptionalIconsVisible.invoke(menu, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetToolbar() {
        if (mToolbar == null) {
            return;
        }
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
        onBackPressed();
    }

    protected void refreshToolbarMenu() {
        if (mToolbar == null) {
            return;
        }
        Menu menu = mToolbar.getMenu();
        menu.clear();
        onToolbarPrepare(mToolbar, menu);
    }

    /***
     @param menuItem
     @return true自己处理点击事件
     */
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

    @Override
    public void onReceiveDataFromFragment(String tag, String name, Object data) {
    }

    @Override
    public void onReceiveOtherFragmentData(String senderTag, String receiverTag, String name, Object data) {
        mActivityFrameworkHelper.onReceiveOtherFragment(this, senderTag, receiverTag, name, data);
    }

    @Override
    public Object onQueryOtherFragmentData(String senderTag, String receiverTag, String name, Object data) {
        return mActivityFrameworkHelper.onQueryOtherFragmentData(this, senderTag, receiverTag, name, data);
    }

    @Override
    public void sendDataToFragment(String tag, String name, Object data) {
        mActivityFrameworkHelper.sendDataToFragment(this, tag, name, data);
    }

    @Override
    public Object queryFragmentData(String tag, String name, Object data) {
        return mActivityFrameworkHelper.queryFragmentData(this, tag, name, data);
    }

    @Override
    public Object onQueryDataFromFragment(String tag, String name, Object data) {
        return null;
    }
}

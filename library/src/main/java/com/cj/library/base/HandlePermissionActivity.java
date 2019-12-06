package com.cj.library.base;

import java.util.HashSet;

/**
 Create by chenjiao at 2019/12/5 0005
 描述：该基类Activity主要是处理权限相关的逻辑
 */
public abstract class HandlePermissionActivity extends BaseActivity {
    private HashSet<String> mNoticedDeniedPermissions = new HashSet<>();
    private int REQUEST_PERMISSION_CORE = 9999;
    private int REQUEST_PERMISSION_EXTRA = 9998;
    private int REQUEST_PERMISSION_ALERT_WINDOW_CORE = 9997;
    private int REQUEST_PERMISSION_ALERT_WINDOW_EXTRA = 9996;
    private int REQUEST_PERMISSION_WRITE_SETTINGS_CORE = 9995;
    private int REQUEST_PERMISSION_WRITE_SETTINGS_EXTRA = 9994;
    private int REQUEST_PACKAGE_PERMISSION_CORE = 9993;
    private int REQUEST_PACKAGE_PERMISSION_EXTRA = 9992;
    private String[] mPendingExtraPermissons = null;
    private boolean mCorePermissionGranted = false;
    private onPendingExtraPermissionSuccessListener mOnPendingExtraPermissionSuccessListener = null;

    /**
     获取当前Activity所需要的核心权限,核心权限是指如果用户不允许该权限则该页面无法显示的权限;
     */
    protected String[] getCorePermissions() {
        return new String[0];
    }

    /**
     核心权限被用户拒绝时关闭掉当前activity
     */
    protected void onCorePermissonDenied() {
        finish();
    }

    private void requestExtraPermissons(String[] permissions, onPendingExtraPermissionSuccessListener onSuceess) {
        requestExtraPermissonsInner(permissions, onSuceess);
    }

    protected void requestExtraPermissonsInner(String[] permissions, onPendingExtraPermissionSuccessListener onSuceess) {
        mPendingExtraPermissons = permissions;
        mOnPendingExtraPermissionSuccessListener = onSuceess;
    }

    interface onPendingExtraPermissionSuccessListener {
        void onSucess();
    }


}

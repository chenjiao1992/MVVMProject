package com.cj.library.base

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.cj.library.utils.PermissionUtil
import java.util.ArrayList
import androidx.core.app.ActivityCompat
import com.cj.library.extension.showAlertDialog

/**
 * Create by chenjiao at 2019/12/5 0005
 * 描述：该基类Activity主要是处理权限相关的逻辑
 */
abstract class BaseHandlePermissionActivity : BaseActivity() {
    private var mPendingExtraPermissons: Array<String>? = null
    private var mCorePermissionGranted = false
    private var mOnPendingExtraPermissionSuccessListener: onPendingExtraPermissionSuccessListener? = null
    /**
     * 获取当前Activity所需要的核心权限,核心权限是指如果用户不允许该权限则该页面无法显示的权限;
     */
    protected open fun getCorePermissions(): Array<String> = emptyArray()

    /**
     * 核心权限被用户拒绝时关闭掉当前activity
     */
    protected fun onCorePermissonDenied() {
        finish()
    }

    private fun requestExtraPermissons(permissions: Array<String>, onSuceess: onPendingExtraPermissionSuccessListener) {
        requestExtraPermissonsInner(permissions, onSuceess)
    }

    private fun requestExtraPermissonsInner(permissions: Array<String>, onSuceess: onPendingExtraPermissionSuccessListener) {
        mPendingExtraPermissons = permissions
        mOnPendingExtraPermissionSuccessListener = onSuceess
        PermissionUtil.requestPermissions(this, permissions, REQUEST_PERMISSION_EXTRA)
    }

    /**
     * 是否拦截Launch,如果拦截,可以在该方法中去做些例如权限请求之类的操作,权限授予成功后才执行onPrepared方法
     */
    override fun onInterceptLaunch(): Boolean {
        val deniedCorePermision = PermissionUtil.getDeniedPermissions(this, getCorePermissions())
        if (deniedCorePermision == null || deniedCorePermision.isEmpty()) {
            return false
        } else {
            if (mCorePermissionGranted) {
                showPermissionDeniedDialog(deniedCorePermision, true)
            } else {
                PermissionUtil.requestPermissions(this, deniedCorePermision, REQUEST_PERMISSION_CORE)
            }
            return true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CORE -> checkPermissionResult(permissions, grantResults, true, object : OnDeniedPermissionCallback {
                override fun deniedPermissions(deniedPermission: Array<String>) {
                    if (deniedPermission.isEmpty()) {
                        onCorePermissonDenied()
                    } else {
                        showPermissionDeniedDialog(deniedPermission, true)
                    }

                }
            })
            REQUEST_PERMISSION_EXTRA -> checkPermissionResult(permissions, grantResults, false, object : OnDeniedPermissionCallback {
                override fun deniedPermissions(deniedPermission: Array<String>) {
                    if (deniedPermission.isNotEmpty()) {
                        showPermissionDeniedDialog(deniedPermission, false)
                    }
                }

            })
            REQUEST_PACKAGE_PERMISSION_CORE -> checkPermissionResult(permissions, grantResults, true, object : OnDeniedPermissionCallback {
                override fun deniedPermissions(deniedPermission: Array<String>) {
                    showPackageForbiddenDialog(true)
                }
            })
            REQUEST_PACKAGE_PERMISSION_EXTRA -> checkPermissionResult(permissions, grantResults, false, object : OnDeniedPermissionCallback {
                override fun deniedPermissions(deniedPermission: Array<String>) {
                    showPackageForbiddenDialog(false)
                }
            })
            else -> throw IllegalStateException("Unexpected value: $requestCode")
        }
    }

    private fun showPackageForbiddenDialog(corePermission: Boolean) {
        showAlertDialog(
                "为了能正常运行，请授权这些权限",
                null,
                null,
                null,
                if (corePermission) "退出" else "关闭",
                DialogInterface.OnClickListener { _, _ ->
                    if (corePermission) {
                        onCorePermissonDenied()
                    } else {
                        resetPendingExtraPermissions(false)
                    }
                },
                false
        )
    }

    private fun showPermissionDeniedDialog(deniedPermissions: Array<String>, isCorePermission: Boolean) {
        for (deniedPermission in deniedPermissions) {
            try {
                when (deniedPermission) {
                    Manifest.permission.SYSTEM_ALERT_WINDOW -> requestAlertWindowPermission(isCorePermission)
                    Manifest.permission.WRITE_SETTINGS -> requestWriteSettingPermission(isCorePermission)
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, deniedPermission)) { //禁止权限并以后不再询问
                    showDisabledDialog(deniedPermission, isCorePermission)
                } else {
                    showForbiddenDialog(deniedPermission, isCorePermission)
                }
            } catch (e: Exception) {
                if (isCorePermission) {
                    onCorePermissonDenied()
                } else {
                    resetPendingExtraPermissions(false)
                }
            }

        }
    }

    private fun showForbiddenDialog(deniedPermission: String, corePermission: Boolean) {
        val message = when (deniedPermission) {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE ->
                "禁用存储卡读写权限后，将无法正常读写数据"
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION ->
                "禁用位置获取权限后，将无法定位地理位置"
            Manifest.permission.READ_PHONE_STATE ->
                "禁用电话权限后，将无法根据手机识别码验证身份"
            Manifest.permission.CAMERA ->
                "禁用相机权限后，将无法拍照和录制视频"
            Manifest.permission.RECORD_AUDIO ->
                "禁用麦克风权限后，将无法录制声音和通话"
            Manifest.permission.READ_CONTACTS ->
                "禁用联系人权限后，将无法根据联系人匹配好友"
            else -> "权限被禁用，请手动开启"
        }
        showAlertDialog(
                "权限被禁用",
                message,
                "开启",
                DialogInterface.OnClickListener { _, _ ->
                    if (corePermission) {
                        dispatchLaunch()
                    } else {
                        checkAndRequestPermission(corePermission)
                    }
                },
                if (corePermission) "退出" else "取消",
                DialogInterface.OnClickListener { _, _ ->
                    if (corePermission) {
                        onCorePermissonDenied()
                    } else {
                        resetPendingExtraPermissions(false)
                    }
                },
                false
        )
    }

    protected fun showDisabledDialog(deniedPermission: String, isCorePermission: Boolean) {
        var message = when (deniedPermission) {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE ->
                "禁用存储卡读写权限后，将无法正常读写数据"
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION ->
                "禁用位置获取权限后，将无法定位地理位置"
            Manifest.permission.READ_PHONE_STATE ->
                "禁用电话权限后，将无法根据手机识别码验证身份"
            Manifest.permission.CAMERA ->
                "禁用相机权限后，将无法拍照和录制视频"
            Manifest.permission.RECORD_AUDIO ->
                "禁用麦克风权限后，将无法录制声音和通话"
            Manifest.permission.READ_CONTACTS ->
                "禁用联系人权限后，将无法根据联系人匹配好友"
            else -> "权限被禁用，请手动开启"
        }
        showAlertDialog("权限被禁用", message, "开启", DialogInterface.OnClickListener { _, _ ->
            if (isCorePermission) {
                dispatchLaunch()
            } else {
                checkAndRequestPermission(isCorePermission)
            }
        })
    }

    private fun checkAndRequestPermission(corePermission: Boolean) {
        val requiredPermissions = if (corePermission) getCorePermissions() else mPendingExtraPermissons
        PermissionUtil.requestPermissions(this, requiredPermissions, if (corePermission) REQUEST_PERMISSION_CORE else REQUEST_PERMISSION_EXTRA)
    }

    private fun requestWriteSettingPermission(isCorePermission: Boolean) {
        if (Build.VERSION.SDK_INT >= 23) {
            Toast.makeText(this, "为了修改设定,请授权修改设定权限", Toast.LENGTH_SHORT).show()
            try {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:\$packageName")
                val requestCode: Int
                if (isCorePermission) {
                    requestCode = REQUEST_PERMISSION_WRITE_SETTINGS_CORE
                } else {
                    requestCode = REQUEST_PERMISSION_WRITE_SETTINGS_EXTRA
                }
                startActivityForResult(intent, requestCode)
            } catch (e: Exception) {
                Toast.makeText(this, "修改设定授权页面跳转失败！", Toast.LENGTH_SHORT).show()
            }

        }
    }

    /**
     * 悬浮窗权限申请弹框
     *
     * @param isCorePermission 是否是页面核心权限
     */
    private fun requestAlertWindowPermission(isCorePermission: Boolean) {
        if (Build.VERSION.SDK_INT >= 23) {
            Toast.makeText(this, "为了显示悬浮窗,请授权悬浮窗权限", Toast.LENGTH_SHORT).show()
            try {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.data = Uri.parse("package:\$packageName")
                val requestCode: Int
                if (isCorePermission) {
                    requestCode = REQUEST_PERMISSION_ALERT_WINDOW_CORE
                } else {
                    requestCode = REQUEST_PERMISSION_ALERT_WINDOW_EXTRA
                }
                startActivityForResult(intent, requestCode)
            } catch (e: Exception) {
                Toast.makeText(this, "悬浮窗授权页面跳转失败！", Toast.LENGTH_SHORT).show()
            }

        }

    }

    internal interface onPendingExtraPermissionSuccessListener {
        fun onSucess()
    }

    private fun checkPermissionResult(permissions: Array<String>, grantResults: IntArray, corePermission: Boolean, onDenied: OnDeniedPermissionCallback) {
        val deniedPermissions = PermissionUtil.checkPermissionResult(this, permissions, grantResults)
        if (deniedPermissions.isEmpty()) {
            if (corePermission) {
                mCorePermissionGranted = true
                dispatchLaunch()
            } else {
                resetPendingExtraPermissions(true)
            }
        } else {
            onDenied.deniedPermissions(deniedPermissions.toTypedArray())
        }

    }

    private fun resetPendingExtraPermissions(success: Boolean) {
        mPendingExtraPermissons = null
        if (success) {
            mOnPendingExtraPermissionSuccessListener!!.onSucess()
        }
        mOnPendingExtraPermissionSuccessListener = null
    }

    internal interface OnDeniedPermissionCallback {
        fun deniedPermissions(deniedPermission: Array<String>)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_PERMISSION_CORE -> dispatchLaunch()
            REQUEST_PERMISSION_EXTRA -> checkAndRequestPermission(false)
            REQUEST_PERMISSION_ALERT_WINDOW_CORE ->
                if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                    showAlertWindowDisabledDialog(true)
                } else {
                    checkAndRequestPermission(true)
                }
            REQUEST_PERMISSION_ALERT_WINDOW_EXTRA -> {
                if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                    showAlertWindowDisabledDialog(false)
                } else {
                    checkAndRequestPermission(false)
                }
            }
            REQUEST_PERMISSION_WRITE_SETTINGS_CORE ->
                if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(this)) {
                    showWriteSettingsDisabledDialog(true)
                } else {
                    checkAndRequestPermission(true)
                }
            REQUEST_PERMISSION_WRITE_SETTINGS_EXTRA -> {
                if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(this)) {
                    showWriteSettingsDisabledDialog(false)
                } else {
                    checkAndRequestPermission(false)
                }
            }
            REQUEST_PACKAGE_PERMISSION_CORE -> checkAndRequestPermission(true)
            REQUEST_PACKAGE_PERMISSION_EXTRA -> checkAndRequestPermission(false)
        }
    }

    private fun showAlertWindowDisabledDialog(corePermission: Boolean) {
        showAlertDialog(
                "权限被禁用",
                "禁用悬浮窗权限后，将无法显示悬浮窗",
                "开启",
                DialogInterface.OnClickListener { _, _ ->
                    if (corePermission) {
                        dispatchLaunch()
                    } else {
                        checkAndRequestPermission(corePermission)
                    }
                },
                if (corePermission) "退出" else "取消",
                DialogInterface.OnClickListener { _, _ ->
                    if (corePermission) {
                        onCorePermissonDenied()
                    } else {
                        resetPendingExtraPermissions(false)
                    }
                },
                false
        )
    }

    private fun showWriteSettingsDisabledDialog(corePermission: Boolean) {
        showAlertDialog(
                "权限被禁用",
                "禁用修改设定定权限后，将无法修改设定",
                "开启",
                DialogInterface.OnClickListener { _, _ ->
                    if (corePermission) {
                        dispatchLaunch()
                    } else {
                        requestWriteSettingPermission(corePermission)
                    }
                },
                if (corePermission) "退出" else "取消",
                DialogInterface.OnClickListener { _, _ ->
                    if (corePermission) {
                        onCorePermissonDenied()
                    } else {
                        resetPendingExtraPermissions(false)
                    }
                },
                false
        )
    }

    companion object {
        val REQUEST_PERMISSION_CORE = 9999
        val REQUEST_PERMISSION_EXTRA = 9998
        val REQUEST_PERMISSION_ALERT_WINDOW_CORE = 9997
        val REQUEST_PERMISSION_ALERT_WINDOW_EXTRA = 9996
        val REQUEST_PERMISSION_WRITE_SETTINGS_CORE = 9995
        val REQUEST_PERMISSION_WRITE_SETTINGS_EXTRA = 9994
        val REQUEST_PACKAGE_PERMISSION_CORE = 9993
        val REQUEST_PACKAGE_PERMISSION_EXTRA = 9992
    }

}

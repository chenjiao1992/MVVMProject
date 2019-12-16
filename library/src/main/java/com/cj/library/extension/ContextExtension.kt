@file:Suppress("DEPRECATION", "HardwareIds", "MissingPermission")

package com.cj.library.extension

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.cj.library.base.BaseActivity
import com.cj.library.helper.Starter
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.reflect.InvocationTargetException
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

fun Context?.defaultSharedPreference() = if (this == null) null else PreferenceManager.getDefaultSharedPreferences(this)

fun Context?.sharedPreference(name: String) = this?.getSharedPreferences(name, Context.MODE_PRIVATE)

fun Context?.activity(): Activity? {
    return if (this == null) {
        null
    } else {
        var target = this
        while (target is ContextWrapper) {
            if (target is Activity) {
                return target
            }
            target = target.baseContext
        }
        null
    }
}

fun Context?.fragmentManager(): FragmentManager? {
    val activity = activity()
    return if (activity == null || activity !is FragmentActivity) {
        null
    } else {
        activity.supportFragmentManager
    }
}

fun Context?.showAlertDialog(title: String?, message: CharSequence?,
        okText: String? = null, okClickListener: DialogInterface.OnClickListener? = null,
        cancelText: String? = null, cancelClickListener: DialogInterface.OnClickListener? = null,
        outTouchCancellable: Boolean = true, autoDismiss: Boolean = true): AlertDialog? {
    if (this == null) return null
    return try {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
        if (okText != null) {
            builder.setPositiveButton(okText, okClickListener)
        }
        if (cancelText != null) {
            builder.setNegativeButton(cancelText, cancelClickListener)
        }
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(outTouchCancellable)
        dialog.show()
        if (!autoDismiss) {
            if (okClickListener != null) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setOnClickListener {
                    okClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
                }
            }
            if (cancelClickListener != null) {
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE)?.setOnClickListener {
                    cancelClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE)
                }
            }
        }
        dialog
    } catch (e: Exception) {
        null
    }
}

fun Context?.showAlertItemDialog(
        items: Array<out CharSequence>, onClickListener: DialogInterface.OnClickListener,
        okText: String? = null, okClickListener: DialogInterface.OnClickListener? = null,
        cancelText: String? = null, cancelClickListener: DialogInterface.OnClickListener? = null,
        outTouchCancellable: Boolean = true): AlertDialog? {
    if (this == null) return null
    val builder = AlertDialog.Builder(this)
        .setItems(items, onClickListener)
    if (okText != null) {
        builder.setPositiveButton(okText, okClickListener)
    }
    if (cancelText != null) {
        builder.setNegativeButton(cancelText, cancelClickListener)
    }
    val dialog = builder.create()
    dialog.setCanceledOnTouchOutside(outTouchCancellable)
    return try {
        dialog.show()
        dialog
    } catch (e: Exception) {
        null
    }
}

fun Context?.showAlertSingleChoiceItemDialog(title: String?, message: String?,
        items: Array<out CharSequence>, checkedItemId: Int, onClickListener: DialogInterface.OnClickListener,
        okText: String? = null, okClickListener: DialogInterface.OnClickListener? = null,
        cancelText: String? = null, cancelClickListener: DialogInterface.OnClickListener? = null,
        outTouchCancellable: Boolean = true, cancelable: Boolean = true): AlertDialog? {
    if (this == null) return null
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setSingleChoiceItems(items, checkedItemId, onClickListener)
        .setCancelable(cancelable)
    if (okText != null) {
        builder.setPositiveButton(okText, okClickListener)
    }
    if (cancelText != null) {
        builder.setNegativeButton(cancelText, cancelClickListener)
    }
    val dialog = builder.create()
    dialog.setCanceledOnTouchOutside(outTouchCancellable)
    return try {
        dialog.show()
        dialog
    } catch (e: Exception) {
        null
    }
}

fun Context?.showAlertViewDialog(title: String?, message: String?,
        view: View,
        okText: String? = null, okClickListener: DialogInterface.OnClickListener? = null,
        cancelText: String? = null, cancelClickListener: DialogInterface.OnClickListener? = null,
        outTouchCancellable: Boolean = true): AlertDialog? {
    if (this == null) return null
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
    builder.setView(view)
    if (okText != null) {
        builder.setPositiveButton(okText, okClickListener)
    }
    if (cancelText != null) {
        builder.setNegativeButton(cancelText, cancelClickListener)
    }
    val dialog = builder.create()
    dialog.setCanceledOnTouchOutside(outTouchCancellable)
    return try {
        dialog.show()
        dialog
    } catch (e: Exception) {
        null
    }
}

fun Context?.showAlertViewDialog(title: String?, message: String?,
        layoutId: Int, viewBinder: (View) -> Unit,
        okText: String? = null, okClickListener: DialogInterface.OnClickListener? = null,
        cancelText: String? = null, cancelClickListener: DialogInterface.OnClickListener? = null,
        outTouchCancellable: Boolean = true): AlertDialog? {
    if (this == null) return null
    val view = LayoutInflater.from(this).inflate(layoutId, null, false)
    viewBinder(view)
    return showAlertViewDialog(title, message, view, okText, okClickListener, cancelText, cancelClickListener, outTouchCancellable)
}

@Suppress("DEPRECATION")
fun Context?.showProgressDialog(message: String): ProgressDialog? {
    if (this == null) return null
    val dialog = ProgressDialog(this)
    dialog.setMessage(message)
    dialog.setCancelable(false)
    return try {
        dialog.show()
        dialog
    } catch (e: Exception) {
        null
    }
}

fun Context?.deviceId() = if (this == null) {
    null
} else {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    } else try {
        (applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
    } catch (e: Exception) {
        null
    }
}

fun Context?.subscriberId() = if (this == null) null else try {
    (applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).subscriberId
} catch (e: Exception) {
    null
}

private var mImei: String? = null

fun Context?.imei(): String? {
    if (this == null) return null
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) return null
    if (mImei.isNullOrEmpty()) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mImei = (getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager)?.deviceId
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            if (telephonyManager != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    try {
                        val clazz = Class.forName("android.os.SystemProperties")
                        val method = clazz.getMethod("get", String::class.java, String::class.java)
                        val gsm = method.invoke(null, "ril.gsm.imei", "") as? String
                        val meid = method.invoke(null, "ril.cdma.meid", "") as? String
                        var IMEI1: String? = null
                        var IMEI2: String? = null
                        if (!gsm.isNullOrEmpty()) {
                            //the value of gsm like:xxxxxx,xxxxxx
                            val IMEIs = gsm.split(",");
                            if (IMEIs.isNotEmpty()) {
                                IMEI1 = IMEIs[0]
                                IMEI2 = if (IMEIs.size > 1) {
                                    IMEIs[1]
                                } else {
                                    null
                                }
                            }
                        } else {
                            IMEI1 = meid
                        }
                        //如果 IMEI1 为空则去 IMEI2
                        mImei = if (!IMEI1.isNullOrEmpty()) IMEI1 else IMEI2
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace();
                    } catch (e: NoSuchMethodException) {
                        e.printStackTrace();
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace();
                    } catch (e: InvocationTargetException) {
                        e.printStackTrace();
                    } catch (e: Exception) {
                        e.printStackTrace();
                    }
                } else {
                    val IMEI1 = telephonyManager.getDeviceId(0)
                    val IMEI2 = telephonyManager.getDeviceId(1)
                    //如果 IMEI1 为空则去 IMEI2
                    mImei = if (!IMEI1.isNullOrEmpty()) IMEI1 else IMEI2
                }
            }
        }
    }
    if (mImei == null) {
        mImei = ""
    }
    return mImei
}

fun Context?.androidId(): String? {
    if (this == null) return null
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}

fun <T : BaseActivity> Activity?.smartStart(clazz: Class<T>, fillAction: Intent.() -> Unit, requestCode: Int = -1, bundle: Bundle? = null) {
    Starter.smartStart(this, clazz, fillAction, requestCode, bundle)
}

fun <T : BaseActivity> Activity?.smartStart(clazz: Class<T>, requestCode: Int = -1, bundle: Bundle? = null) {
    Starter.smartStart(this, clazz, requestCode, bundle)
}

fun Activity?.smartStart(intent: Intent, requestCode: Int = -1, bundle: Bundle? = null) {
    Starter.smartStart(this, intent, requestCode, bundle)
}

fun <T : BaseActivity> Fragment?.smartStart(clazz: Class<T>, fillAction: Intent.() -> Unit, requestCode: Int = -1, bundle: Bundle? = null) {
    Starter.smartStart(this, clazz, fillAction, requestCode, bundle)
}

fun <T : BaseActivity> Fragment?.smartStart(clazz: Class<T>, requestCode: Int = -1, bundle: Bundle? = null) {
    Starter.smartStart(this, clazz, requestCode, bundle)
}

fun Fragment?.smartStart(intent: Intent, requestCode: Int = -1, bundle: Bundle? = null) {
    Starter.smartStart(this, intent, requestCode, bundle)
}

fun <T : BaseActivity> Context?.smartStart(clazz: Class<T>, fillAction: Intent.() -> Unit, requestCode: Int = -1, bundle: Bundle? = null) {
    Starter.smartStart(this, clazz, fillAction, requestCode, bundle)
}

fun <T : BaseActivity> Context?.smartStart(clazz: Class<T>, requestCode: Int = -1, bundle: Bundle? = null) {
    Starter.smartStart(this, clazz, requestCode, bundle)
}

fun Context?.smartStart(intent: Intent, requestCode: Int = -1, bundle: Bundle? = null) {
    Starter.smartStart(this, intent, requestCode, bundle)
}

@SuppressLint("HardwareIds")
fun Context?.mac(): String? {
    if (this == null) {
        return null
    } else {
        val sdk = Build.VERSION.SDK_INT
        when {
            sdk < Build.VERSION_CODES.M -> {
                val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager ?: return null
                val connectionInfo = try {
                    wifiManager.connectionInfo
                } catch (e: Exception) {
                    null
                } ?: return null
                val mac = connectionInfo.macAddress
                if (mac.isNullOrEmpty()) return null
                return mac.toUpperCase(Locale.ENGLISH)
            }
            sdk < Build.VERSION_CODES.N -> {
                return try {
                    BufferedReader(FileReader(File("/sys/class/net/wlan0/address"))).readLine()
                } catch (e: Exception) {
                    null
                }
            }
            else -> {
                val builder = StringBuilder()
                try {
                    val interfaces = NetworkInterface.getNetworkInterfaces()
                    while (interfaces.hasMoreElements()) {
                        val element = interfaces.nextElement()
                        if (element.name != "wlan0" && element.name != "rmnet0") continue
                        val address = element.hardwareAddress
                        if (address == null || address.isEmpty()) {
                            continue
                        }
                        if (builder.isNotEmpty()) {
                            builder.delete(0, builder.lastIndex)
                        }
                        for (b in address) {
                            builder.append(String.format("%02X:", b))
                        }
                        if (builder.isNotEmpty()) {
                            builder.deleteCharAt(builder.lastIndex)
                        }
                        return builder.toString()
                    }
                } catch (e: SocketException) {
                }
                return null
            }
        }
        //        var mac = (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo?.macAddress
        //        if (!mac.isNullOrEmpty() && mac != "02:00:00:00:00:00") return mac
        //        var str: String? = ""
        //        try {
        //            val process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ")
        //            val reader = InputStreamReader(process.inputStream)
        //            val input = LineNumberReader(reader)
        //            while (str != null) {
        //                str = input.readLine()
        //                if (str != null) {
        //                    mac = str.trim()
        //                    break
        //                }
        //            }
        //        } catch (e: Exception) {
        //            e.printStackTrace()
        //        }
        //        if (!mac.isNullOrEmpty() && mac != "02:00:00:00:00:00") return mac
        //        try {
        //            val reader = FileReader("/sys/class/net/eth0/address")
        //            val builder = StringBuilder()
        //            val buffer = CharArray(4096)
        //            var readLength = reader.read(buffer)
        //            while (readLength >= 0) {
        //                builder.append(buffer, 0, readLength)
        //                readLength = reader.read(buffer)
        //            }
        //            val text = builder.toString()
        //            reader.close()
        //            mac = text.toUpperCase().substring(0, 17)
        //        } catch (e: Exception) {
        //            e.printStackTrace()
        //        }
        //        if (!mac.isNullOrEmpty() && mac != "02:00:00:00:00:00") return mac
        //
        //        return mac
    }
}


fun Context?.safeStartService(intent: Intent) {
    if (this == null) return
    try {
        startService(intent)
    } catch (th: Throwable) {
        Log.d("service", "start service: ${intent.component}error: $th")
    }
}

fun Context?.startServiceCompat(intent: Intent) {
    if (this == null) return
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    } catch (th: Throwable) {
        Log.d("service", "start service: ${intent.component}error: $th")
    }
}
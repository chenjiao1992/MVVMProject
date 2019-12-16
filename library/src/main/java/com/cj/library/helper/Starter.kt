package com.cj.library.helper

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.cj.library.extension.activity
import com.cj.library.extension.fragmentManager


object Starter {
    private val mIntentMap: HashMap<Int, Intent> = HashMap()

    private fun checkIntent(intent: Intent): Int? {
        val component = intent.component
        val key = if (component != null) {
            "${component.packageName}_${component.className}"
        } else {
            intent.action.orEmpty()
        }.hashCode()
        if (mIntentMap.containsKey(key)) {
            return null
        }
        mIntentMap[key] = intent
        return key
    }

    private fun startPendingClean(key: Int) {
        CommonHandler.postDelayed(Runnable {
            mIntentMap.remove(key)
        }, 500L)
    }

    fun start(application: Application, intent: Intent, bundle: Bundle?) {
        val key = checkIntent(intent) ?: return
        ActivityCompat.startActivity(application, intent, bundle)
        startPendingClean(key)
    }

    fun start(fragment: Fragment, intent: Intent, requestCode: Int, bundle: Bundle?) {
        val key = checkIntent(intent) ?: return
        fragment.startActivityForResult(intent, requestCode, bundle)
        startPendingClean(key)
    }

    fun start(activity: Activity, intent: Intent, requestCode: Int, bundle: Bundle?) {
        val key = checkIntent(intent) ?: return
        ActivityCompat.startActivityForResult(activity, intent, requestCode, bundle)
        CommonHandler.postDelayed(Runnable {
            mIntentMap.remove(key)
        }, 500L)
        //        val component = intent.component
        //        if (component != null) {
        //            val intentMap = mIntentMap
        //            val key = "${component.packageName}_${component.className}".hashCode()
        //            if (intentMap.containsKey(key)) {
        //                return
        //            }
        //            intentMap[key] = intent
        //            ActivityCompat.startActivityForResult(activity, intent, requestCode, bundle)
        //            CommonHandler.postDelayed(Runnable {
        //                mIntentMap.remove(key)
        //            }, 500L)
        //        } else {
        //            ActivityCompat.startActivityForResult(activity, intent, requestCode, bundle)
        //        }
    }

    fun start(context: Context, intent: Intent, requestCode: Int, bundle: Bundle?) {
        val activity = context.activity() ?: return
        start(activity, intent, requestCode, bundle)
    }

    fun smartStart(starter: Any?, intent: Intent, requestCode: Int = -1, bundle: Bundle? = null) {
        when (starter) {
            null -> return
            is Application -> start(starter, intent, bundle)
            is Fragment -> start(starter, intent, requestCode, bundle)
            is Activity -> start(starter, intent, requestCode, bundle)
            is Context -> start(starter, intent, requestCode, bundle)
        }
    }

    fun <T : Activity> smartStart(starter: Any?, clazz: Class<T>, fillAction: Intent.() -> Unit, requestCode: Int = -1, bundle: Bundle? = null) {
        val intent = createIntent(starter, clazz) ?: return
        fillAction(intent)
        smartStart(starter, intent, requestCode, bundle)
    }

    fun <T : Activity> smartStart(starter: Any?, clazz: Class<T>, requestCode: Int = -1, bundle: Bundle? = null) {
        when (starter) {
            null -> return
            is Application -> {
                start(starter, Intent(starter, clazz), bundle)
            }
            is Fragment -> {
                val context = starter.context ?: return
                start(starter, Intent(context, clazz), requestCode, bundle)
            }
            is Activity -> {
                start(starter, Intent(starter, clazz), requestCode, bundle)
            }
            is Context -> {
                start(starter, Intent(starter, clazz), requestCode, bundle)
            }
        }
    }

    fun smartShow(starter: Any?, bundle: Bundle? = null, tag: String, target: DialogFragment) {
        when (starter) {
            null -> return
            is Application -> return
            is Fragment -> {
                target.arguments = bundle
                try {
                    target.show(starter.childFragmentManager, tag)
                } catch (e: Exception) {
                }
            }
            is FragmentActivity -> {
                target.arguments = bundle
                try {
                    target.show(starter.supportFragmentManager, tag)
                } catch (e: Exception) {
                }
            }
            is Context -> {
                val activity = starter.activity() as? FragmentActivity ?: return
                target.arguments = bundle
                try {
                    target.show(activity.supportFragmentManager, tag)
                } catch (e: Exception) {
                }
            }
        }
    }

    fun smartShow(starter: Any?, fillAction: Bundle.() -> Unit, tag: String, target: DialogFragment) {
        val bundle = Bundle()
        fillAction(bundle)
        smartShow(starter, bundle, tag, target)
    }

    fun getActivity(starter: Any?) = when (starter) {
        null -> null
        is Application -> null
        is Fragment -> starter.activity
        is Activity -> starter
        is Context -> starter.activity()
        else -> null
    }

    fun getFragmentManager(starter: Any?) = when (starter) {
        null -> null
        is Fragment -> starter.childFragmentManager
        is FragmentActivity -> starter.supportFragmentManager
        is Context -> starter.fragmentManager()
        else -> null
    }

    fun <T : Activity> createIntent(starter: Any?, clazz: Class<T>): Intent? {
        return when (starter) {
            null -> null
            is Application -> {
                Intent(starter, clazz)
            }
            is Fragment -> {
                val context = starter.context ?: return null
                Intent(context, clazz)
            }
            is Activity -> {
                Intent(starter, clazz)
            }
            is Context -> {
                Intent(starter, clazz)
            }
            else -> null
        }
    }
}
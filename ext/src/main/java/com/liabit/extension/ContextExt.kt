package com.liabit.extension

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.util.TypedValue
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * @return Returns true if the intent has been scheduled for delivery to one or more
 * broadcast receivers.  (Note tha delivery may not ultimately take place if one of those
 * receivers is unregistered before it is dispatched.)
 */
fun Context.sendLocalBroadcast(intent: Intent): Boolean {
    return LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
}

fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return when {
        resourceId > 0 -> resources.getDimensionPixelSize(resourceId)
        else -> 24.dip(this)
    }
}

fun Context.getNavigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return when {
        resourceId > 0 -> resources.getDimensionPixelSize(resourceId)
        else -> 48.dip(this)
    }
}

fun Context.color(id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getColor(id)
    } else {
        @Suppress("DEPRECATION")
        resources.getColor(id)
    }
}

fun Context.dip(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
}

fun Context.getVersionCode(): Long {
    var verCode = -1L
    try {
        verCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName, 0).longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0).versionCode.toLong()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        Log.w(TAG, "getVersionCode error: ", e)
    }
    return verCode
}

fun Context.getVersionName(): String {
    var verName = ""
    try {
        verName = packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        Log.w(TAG, "getVersionName error: ", e)
    }
    return verName
}

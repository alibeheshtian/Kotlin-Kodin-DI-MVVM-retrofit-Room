package com.shenoto.domain.extensions

import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.PermissionUtils.OnRationaleListener.ShouldRequest
import com.shenoto.domain.R

object DialogHelper {

    fun showRationaleDialog(shouldRequest: ShouldRequest) {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(topActivity.getString(R.string.need_access_to_local_storage))
            .setPositiveButton(android.R.string.ok) { _, _ -> shouldRequest.again(true) }
            .setNegativeButton(android.R.string.cancel) { _, _ -> shouldRequest.again(false) }
            .setCancelable(false)
            .create()
            .show()

    }

    fun showNeedLoginDialog(okClickedListener: () -> Unit) {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage("Need Login Message")
            .setPositiveButton(topActivity.getString(R.string.login)) { _, _ -> okClickedListener() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
            .show()
    }

    fun showOpenAppSettingDialog() {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(topActivity.getString(R.string.need_access_to_local_storage))
            .setPositiveButton(android.R.string.ok) { _, _ -> PermissionUtils.launchAppDetailsSettings() }
            .setCancelable(false)
            .create()
            .show()
    }

    fun showLogoutDialog(okClickedListener: () -> Unit) {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(topActivity.getString(R.string.logout_dialog_description))
            .setPositiveButton(android.R.string.ok) { _, _ -> okClickedListener() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
            .show()
    }

    fun showDialog(message:String) {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(message)
            .setNegativeButton(android.R.string.ok) { _, _ -> }
            .create()
            .show()
    }

    fun showShenotoPlusDialog(okClickedListener: () -> Unit) {
        val topActivity = ActivityUtils.getTopActivity() ?: return
        AlertDialog.Builder(topActivity)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(topActivity.getString(R.string.shenoto_plus_description))
            .setPositiveButton(android.R.string.ok) { _, _ -> okClickedListener() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
            .show()
    }


}
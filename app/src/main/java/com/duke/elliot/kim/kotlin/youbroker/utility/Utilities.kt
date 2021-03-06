package com.duke.elliot.kim.kotlin.youbroker.utility

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.duke.elliot.kim.kotlin.youbroker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun getProgressDialog(context: Context): AlertDialog {
    val builder = AlertDialog.Builder(context)
    builder.setCancelable(false)
    builder.setView(R.layout.progress_dialog)

    val alertDialog = builder.create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    return alertDialog
}

fun printHashKey(context: Context) {
    try {
        val packageInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
        for (signature in packageInfo.signatures) {
            val messageDigest: MessageDigest = MessageDigest.getInstance("SHA")
            messageDigest.update(signature.toByteArray())
            val hashKey = String(Base64.encode(messageDigest.digest(), 0))
            println("printHashKey() Hash Key: $hashKey")
        }
    } catch (e: NoSuchAlgorithmException) {
        println("printHashKey() $e")
    } catch (e: Exception) {
        println("printHashKey() $e")
    }
}

fun showToast(context: Context, text: String, duration: Int = Toast.LENGTH_SHORT) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, text, duration).show()
    }
}

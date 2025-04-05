package com.time2.superid.utils

import android.content.Context
import android.provider.Settings

fun getDeviceID(context: Context) : String
{
    // Return device IMEI
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}
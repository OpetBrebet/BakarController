package com.brebet.bakarcontroller.util

import android.os.Build

object BuildUtil {
    fun deviceAtLeastS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
}
package pl.mobite.sample.security.utils

import android.os.Build


fun hasMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

fun hasPie() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
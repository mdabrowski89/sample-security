package pl.mobite.sample.security.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty


abstract class SharedPrefs(
    private val context: Context,
    private val sharedPrefsName: String
) {

    protected val sharedPrefs: SharedPreferences by lazy {
        context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE)
    }

    fun clear() {
        sharedPrefs.edit().clear().apply()
    }
}

fun SharedPreferences.boolean(key: String, defValue: Boolean) =
    SharedPrefsDelegate(
        this,
        key,
        defValue,
        SharedPreferences::getBoolean,
        SharedPreferences.Editor::putBoolean
    )

fun SharedPreferences.long(key: String, defValue: Long) =
    SharedPrefsDelegate(
        this,
        key,
        defValue,
        SharedPreferences::getLong,
        SharedPreferences.Editor::putLong
    )

fun SharedPreferences.int(key: String, defValue: Int) =
    SharedPrefsDelegate(
        this,
        key,
        defValue,
        SharedPreferences::getInt,
        SharedPreferences.Editor::putInt
    )

fun SharedPreferences.string(key: String, defValue: String?) =
    SharedPrefsDelegate(
        this,
        key,
        defValue,
        SharedPreferences::getString,
        SharedPreferences.Editor::putString
    )

open class SharedPrefsDelegate<T>(
    private val prefs: SharedPreferences,
    private val key: String,
    private val defValue: T,
    private val getFunction: (SharedPreferences, String, T) -> T,
    private val setFunction: (SharedPreferences.Editor, String, T) -> SharedPreferences.Editor
) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getFunction(prefs, key, defValue)
    }

    @SuppressLint("CommitPrefEdits")
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        setFunction(prefs.edit(), key, value).apply()
    }
}


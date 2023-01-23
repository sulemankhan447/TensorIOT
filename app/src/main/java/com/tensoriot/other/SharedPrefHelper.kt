package com.tensoriot.other

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefHelper @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = prefs.edit()

    fun saveInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    fun saveLong(key: String, value: Long) {
        editor.putLong(key, value).apply()
    }

    fun saveString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return prefs.getString(key?:"","")
    }

    fun saveBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun getBoolean(key:String):Boolean{
        return prefs.getBoolean(key,false)
    }

    fun clearData() {
        val edit = prefs.edit()
        edit.clear()
        edit.apply()
    }

}
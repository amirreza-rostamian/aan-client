package me.himanshusoni.chatmessageview.Vasni.Core

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.preference.PreferenceManager


object MSharePk {
    fun getBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        return settings.getBoolean(key, defValue)
    }

    fun putBoolean(context: Context, key: String, value: Boolean) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getInt(context: Context, key: String, defValue: Int): Int {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        return settings.getInt(key, defValue)
    }

    fun putInt(context: Context, key: String, value: Int) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun getLong(context: Context, key: String, defValue: Long): Long {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        return settings.getLong(key, defValue)
    }

    fun putLong(context: Context, key: String, value: Long) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putLong(key, value)
        editor.commit()
    }

    fun getString(context: Context, key: String, defValue: String): String? {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        return settings.getString(key, defValue)
    }

    fun putString(context: Context, key: String, value: String) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun clearAll(context: Context) {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        settings.edit().clear().commit()

    }

    fun remove(context: Context, key: String) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.remove(key)
        editor.commit()
    }

    fun registerOnPrefChangeListener(context: Context, listener: OnSharedPreferenceChangeListener) {
        try {
            PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener)
        } catch (ignored: Exception) {
        }

    }

    fun unregisterOnPrefChangeListener(context: Context, listener: OnSharedPreferenceChangeListener) {
        try {
            PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener)
        } catch (ignored: Exception) {
        }

    }


}

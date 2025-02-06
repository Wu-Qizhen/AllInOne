package com.wqz.allinone.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * 用于管理加密存储的密码的类
 * Created by Wu Qizhen on 2024.10.3
 */
class PasswordPreferencesManager(context: Context) {
    companion object {
        // 布局类型常量
        private const val PREF_KEY = "password"
        private const val PREF_NAME = "PasswordPrefs" // 自定义 SharedPreferences 名称
        const val DEFAULT_PASSWORD = "123456"
    }

    // 使用 Context.getSharedPreferences 替代 PreferenceManager
    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    init {
        initializeDefaults()
    }

    private fun initializeDefaults() {
        // 如果 PREF_KEY 不存在，设置默认值为 LAYOUT_TYPE_LIST
        if (!sharedPreferences.contains(PREF_KEY)) {
            sharedPreferences.edit { putString(PREF_KEY, DEFAULT_PASSWORD) }
        }
    }

    // 设置布局类型
    fun savePassword(password: String) {
        sharedPreferences.edit { putString(PREF_KEY, password) }
    }

    // 获取布局类型
    fun getPassword(): String =
        sharedPreferences.getString(PREF_KEY, DEFAULT_PASSWORD) ?: DEFAULT_PASSWORD
}

/*
class PasswordPreferencesManager(context: Context) {
    private val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        "PasswordPrefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    init {
        initializeDefaults()
    }

    private fun initializeDefaults() {
        val editor = sharedPreferences.edit()
        if (sharedPreferences.getString("password", null) == null) {
            editor.putString("password", "123456").apply()
        }
    }

    fun savePassword(password: String) {
        sharedPreferences.edit().putString("password", password).apply()
    }

    fun getPassword(): String? {
        return sharedPreferences.getString("password", null)
    }

    *//*fun removePassword() {
        sharedPreferences.edit().remove("password").apply()
    }*//*
}*/
package com.wqz.allinone.secure

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

/**
 * 用于管理加密存储的密码的类
 * Created by Wu Qizhen on 2024.10.3
 */
class SecurePreferencesManager(context: Context) {
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

    fun removePassword() {
        sharedPreferences.edit().remove("password").apply()
    }
}
package com.wqz.allinone.preference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

/**
 * 布局类型管理器
 * Created by Wu Qizhen on 2024.12.31
 */
@Suppress("DEPRECATION")
class LayoutPreferencesManager(context: Context) {
    companion object {
        // 布局类型常量
        private const val PREF_KEY = "layout"
        const val LAYOUT_TYPE_GRID = "GRID"
        const val LAYOUT_TYPE_LIST = "LIST"
    }

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    init {
        initializeDefaults()
    }

    private fun initializeDefaults() {
        val editor = sharedPreferences.edit()
        if (sharedPreferences.getString(PREF_KEY, null) == null) {
            editor.putString(PREF_KEY, LAYOUT_TYPE_LIST).apply()
        }
    }

    // 设置布局类型
    fun setLayoutType(layoutType: String) {
        sharedPreferences.edit { putString(PREF_KEY, layoutType) }
    }

    // 获取布局类型
    private fun getLayoutType(): String =
        sharedPreferences.getString(PREF_KEY, LAYOUT_TYPE_LIST) ?: LAYOUT_TYPE_LIST

    // 检查当前布局是否为网格布局
    fun isGridLayout(): Boolean = getLayoutType() == LAYOUT_TYPE_GRID
}
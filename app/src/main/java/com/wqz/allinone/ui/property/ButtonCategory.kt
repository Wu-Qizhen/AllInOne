package com.wqz.allinone.ui.property

import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.ContentColor

/**
 * 按钮类型
 * Created by Wu Qizhen on 2024.12.22
 */
object ButtonCategory {
    val WARNING_BUTTON = listOf(
        BackgroundColor.DEFAULT_RED,
        BackgroundColor.PRESSED_RED,
        ContentColor.DEFAULT_RED
    )
    val SAFE_BUTTON = listOf(
        BackgroundColor.DEFAULT_GREEN,
        BackgroundColor.PRESSED_GREEN,
        ContentColor.DEFAULT_GREEN
    )
}
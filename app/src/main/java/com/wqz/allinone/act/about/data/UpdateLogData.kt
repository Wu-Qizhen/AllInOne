package com.wqz.allinone.act.about.data

import com.wqz.allinone.entity.UpdateLog

object UpdateLogData {
    val updateLogs = listOf(
        UpdateLog(
            "Dorado",
            "剑鱼座，力量和速度",
            "2024.10.02",
            """
                |» 新增
                | - ⌈纪念日⌋ 跳转今日
                | - ⌈待办箱⌋ 修改事件
                |» 优化
                | - 使用 Aethex Matrix ® 构建框架
                | - 主页界面重构
                | - ⌈待办箱⌋ 界面重构
                | - ⌈纪念日⌋ 卡片样式
                | - ⌈纪念日⌋ 日期计算
                | - ⌈随手记⌋ 列表逻辑
            """.trimMargin()
        ),
        UpdateLog(
            "Cygnus",
            "天鹅座，优雅和远见",
            "2024.08.20",
            "» 新增\n - ⌈纪念日⌋"
        ),
        UpdateLog(
            "Bismuth",
            "独特和坚韧",
            "2024.07.01",
            "» 新增\n - ⌈随手记⌋"
        ),
        UpdateLog(
            "Aurora",
            "新的开始，像极光一样引人注目",
            "2024.06.22",
            "» 新增\n - ⌈待办箱⌋\n - ⌈书签宝⌋"
        ),
    )
}
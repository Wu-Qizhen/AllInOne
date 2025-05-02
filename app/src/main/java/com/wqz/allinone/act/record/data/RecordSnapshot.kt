package com.wqz.allinone.act.record.data

/**
 * 溯影棱镜
 * Created by Wu Qizhen on 2025.5.2
 */
data class RecordSnapshot(
    val type: String,
    val title: String,
    val count: Int,
    val subtitle: String,
    val progress: Float // 0-1 的进度值
)
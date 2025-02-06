package com.wqz.allinone.util

import android.util.Log

/**
 * 日志打印工具类
 * Created by Wu Qizhen on 2025.2.6
 */
class LogPrinter {
    fun e(tag: String?, message: String?) {
        var msg = message
        if (tag.isNullOrEmpty() || msg.isNullOrEmpty()) return
        val segmentSize = 3 * 1024
        val length = msg.length.toLong()
        if (length <= segmentSize) { // 长度小于等于限制直接打印
            Log.e(tag, msg)
        } else {
            while (msg!!.length > segmentSize) { // 循环分段打印日志
                val logContent = msg.substring(0, segmentSize)
                msg = msg.replace(logContent, "")
                Log.e(tag, logContent)
            }
            Log.e(tag, msg) // 打印剩余日志
        }
    }
}
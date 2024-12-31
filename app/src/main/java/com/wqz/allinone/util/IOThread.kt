package com.wqz.allinone.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 协程相关工具类
 * Created by Wu Qizhen on 2024.12.31
 */
fun ioThread(f: suspend () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch { f() }
}
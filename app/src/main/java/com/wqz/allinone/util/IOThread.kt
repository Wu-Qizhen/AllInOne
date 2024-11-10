package com.wqz.allinone.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ioThread(f: suspend () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch { f() }
}
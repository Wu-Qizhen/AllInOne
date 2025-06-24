package com.wqz.allinone.ui

import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * 全局模糊层组件
 * Created by Wu Qizhen on 2025.6.24
 */
@Composable
fun BlurOverlay(radius: Float = 20f) {
    AndroidView(
        factory = { context ->
            View(context).apply {
                setBackgroundColor(Color.TRANSPARENT)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { view ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                view.setRenderEffect(
                    RenderEffect.createBlurEffect(
                        radius, radius,
                        Shader.TileMode.MIRROR
                    )
                )
            }
        }
    )
}
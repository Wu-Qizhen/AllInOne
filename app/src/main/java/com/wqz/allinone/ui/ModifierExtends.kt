package com.wqz.allinone.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput

/**
 * 按压缩放效果
 * Created by Wu Qizhen on 2024.6.16
 */
object ModifierExtends {
    @Composable
    fun Modifier.clickVfx(
        interactionSource: MutableInteractionSource = rememberMutableInteractionSource(),
        enabled: Boolean = true,
        onClick: () -> Unit,
    ): Modifier = composed {
        if (enabled) {
            val isPressed by interactionSource.collectIsPressedAsState()
            val sizePercent by animateFloatAsState(
                targetValue = if (isPressed) 0.95f else 1f,
                animationSpec = tween(durationMillis = 100), label = ""
            )
            scale(sizePercent).clickable(
                indication = null, interactionSource = interactionSource, onClick = onClick
            )
        } else {
            Modifier
        }
    }

    @Composable
    fun Modifier.clickVfx(
        interactionSource: MutableInteractionSource = rememberMutableInteractionSource(),
        enabled: Boolean = true,
        onClick: () -> Unit = {},
        onLongClick: () -> Unit = {}
    ): Modifier = composed {
        if (enabled) {
            val isPressed by interactionSource.collectIsPressedAsState()
            val sizePercent by animateFloatAsState(
                targetValue = if (isPressed) 0.95f else 1f,
                animationSpec = tween(durationMillis = 100), label = ""
            )
            scale(sizePercent).pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = {
                        onLongClick()
                    },
                    onPress = {
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    })
            }
        } else {
            Modifier
        }
    }

    @Composable
    fun rememberMutableInteractionSource() = remember {
        MutableInteractionSource()
    }
}
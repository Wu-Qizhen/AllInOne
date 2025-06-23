package com.wqz.allinone.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wqz.allinone.R
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor
import kotlinx.coroutines.delay

/**
 * 通用背景
 * Created by Wu Qizhen on 2024.6.16
 * Refactored by Wu Qizhen on 2024.11.30
 * Updated by Wu Qizhen on 2025.6.16
 */
object XBackground {
    @Composable
    fun titleBackgroundHorizontalPadding() = if (isRound()) 25.dp else 12.dp

    /**
     * 圆形背景
     */
    @Composable
    fun CirclesBackground(content: @Composable () -> Unit) {
        AllInOneTheme {
            var bottomWidth by remember { mutableStateOf(0.dp) }
            var topWidth by remember { mutableStateOf(0.dp) }
            val localDensity = LocalDensity.current

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Row(
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bg_circle_left),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(50f)
                            .align(Alignment.Bottom)
                            .height(bottomWidth)
                            .fillMaxWidth()
                            .onGloballyPositioned {
                                bottomWidth = with(localDensity) { it.size.width.toDp() }
                            },
                        contentScale = ContentScale.FillWidth
                    )

                    Spacer(Modifier.weight(50f))
                }

                Row(
                    Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxSize()
                ) {
                    Spacer(Modifier.weight(50f))

                    Image(
                        painter = painterResource(id = R.drawable.bg_circle_right),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(50f)
                            .align(Alignment.Top)
                            .height(topWidth)
                            .fillMaxWidth()
                            .onGloballyPositioned {
                                topWidth = with(localDensity) { it.size.width.toDp() }
                            },
                        contentScale = ContentScale.Fit
                    )
                }

                Column(
                    Modifier
                        .fillMaxSize()
                ) {
                    content()
                }
            }
        }
    }

    /**
     * 呼吸背景
     */
    @Composable
    fun BreathingBackground(
        isBreathing: Boolean = false,
        content: @Composable () -> Unit
    ) {
        AllInOneTheme {
            val localDensity = LocalDensity.current
            val ambientAlpha = 0.6f
            val infiniteTransition = rememberInfiniteTransition(label = "")
            // 呼吸动画状态控制
            val alpha by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0.3f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1000
                        0.7f at 500
                    },
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )

            /*// 控制呼吸动画的启停
        LaunchedEffect(isBreathing) {
            if (isBreathing) {
                while (true) {
                    // 呼吸效果：1.0 → 0.3 → 1.0
                    alpha.animateTo(0.3f, animationSpec = tween(750))
                    alpha.animateTo(1f, animationSpec = tween(750))
                }
            } else {
                // 停止动画，回到完全不透明状态
                alpha.snapTo(1f)
            }
        }*/

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                /*Image(
                painter = painterResource(id = R.drawable.bg_breathing_medium),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillWidth
            )*/

                var circleHeight by remember {
                    mutableStateOf(0.dp)
                }
                // 呼吸效果背景层
                Box(
                    modifier = Modifier
                        .offset(y = circleHeight * -0.5f)
                        .fillMaxWidth()
                        .scale(scaleX = 1.1f, scaleY = 1.1f)
                        .aspectRatio(1f)
                        .alpha(if (isBreathing) ambientAlpha * alpha * 0.9f else ambientAlpha * 0.9f)
                        .background(
                            shape = CircleShape, brush = Brush.radialGradient(
                                listOf(
                                    ThemeColor,
                                    Color.Transparent
                                )
                            )
                        )
                        .onSizeChanged {
                            circleHeight = with(localDensity) { it.height.toDp() }
                        }
                )

                content()

                // 提示系统
                // 1. Toast 提示
                AnimatedVisibility(
                    visible = XToast.toastContent.isNotEmpty(),
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    XToast.ToastContent(content = XToast.toastContent)
                }

                // 2. SnackBar 提示
                AnimatedVisibility(
                    visible = XToast.snackBarObject != null,
                    enter = fadeIn() + slideInVertically { it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    XToast.snackBarObject?.let {
                        XToast.SnackBarContent(it)
                    }
                }

                // 3. 自动隐藏逻辑
                LaunchedEffect(key1 = XToast.toastContent) {
                    if (XToast.toastContent.isNotEmpty()) {
                        delay(2000)
                        XToast.toastContent = ""
                    }
                }

                LaunchedEffect(key1 = XToast.snackBarObject) {
                    if (XToast.snackBarObject != null) {
                        delay(2000)
                        XToast.snackBarObject = null
                    }
                }
            }
        }
    }

    /**
     * 呼吸背景
     */
    @Composable
    fun BreathingBackground(
        title: String,
        isBreathing: Boolean = false,
        content: @Composable () -> Unit
    ) {
        /*Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_breathing_large),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillWidth
            )*/

        BreathingBackground(isBreathing = isBreathing) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                XTitleBar.TextTitleBar(title = title)

                content()
            }
        }

        /*// 提示系统
        // 1. Toast 提示
        AnimatedVisibility(
            visible = XToast.toastContent.isNotEmpty(),
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            XToast.ToastContent(content = XToast.toastContent)
        }

        // 2. SnackBar 提示
        AnimatedVisibility(
            visible = XToast.snackBarObject != null,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            XToast.snackBarObject?.let {
                XToast.SnackBarContent(it)
            }
        }

        // 3. 自动隐藏逻辑
        LaunchedEffect(key1 = XToast.toastContent) {
            if (XToast.toastContent.isNotEmpty()) {
                delay(2000)
                XToast.toastContent = ""
            }
        }

        LaunchedEffect(key1 = XToast.snackBarObject) {
            if (XToast.snackBarObject != null) {
                delay(2000)
                XToast.snackBarObject = null
            }
        }*/
        /*}*/
    }

    /**
     * 呼吸背景
     */
    @Composable
    fun BreathingBackground(
        titleId: Int,
        isBreathing: Boolean = false,
        content: @Composable () -> Unit
    ) {
        /*Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_breathing_large),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillWidth
            )

            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                XTitleBar.TextTitleBar(title = stringResource(id = title))

                content()
            }

            // 提示系统
            // 1. Toast 提示
            AnimatedVisibility(
                visible = XToast.toastContent.isNotEmpty(),
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                XToast.ToastContent(content = XToast.toastContent)
            }

            // 2. SnackBar 提示
            AnimatedVisibility(
                visible = XToast.snackBarObject != null,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                XToast.snackBarObject?.let {
                    XToast.SnackBarContent(it)
                }
            }

            // 3. 自动隐藏逻辑
            LaunchedEffect(key1 = XToast.toastContent) {
                if (XToast.toastContent.isNotEmpty()) {
                    delay(2000)
                    XToast.toastContent = ""
                }
            }

            LaunchedEffect(key1 = XToast.snackBarObject) {
                if (XToast.snackBarObject != null) {
                    delay(2000)
                    XToast.snackBarObject = null
                }
            }
        }*/

        BreathingBackground(title = stringResource(id = titleId), isBreathing = isBreathing) {
            content()
        }
    }
}

@Preview
@Composable
fun CirclesBackgroundPreview() {
    XBackground.BreathingBackground {}
}

@Preview
@Composable
fun BreathingBackgroundPreview() {
    XBackground.BreathingBackground("主页") {}
}
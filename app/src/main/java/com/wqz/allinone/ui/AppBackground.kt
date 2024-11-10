package com.wqz.allinone.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wqz.allinone.R
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 通用背景
 * Created by Wu Qizhen on 2024.6.16
 */
object AppBackground {
    @Composable
    fun titleBackgroundHorizontalPadding() = if (isRound()) 24.dp else 12.dp

    @Composable
    fun CirclesBackground(content: @Composable () -> Unit) {
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
                /*.padding(horizontal = 20.dp)*/
            ) {
                // Spacer(Modifier.width(8.dp))
                content()
                // Spacer(Modifier.width(8.dp))
            }
        }
    }

    @Composable
    fun BreathingBackground(content: @Composable () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_breathing_medium),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillWidth
            )
            content()
        }
    }

    @Composable
    fun BreathingBackground(title: String, content: @Composable () -> Unit) {
        Box(
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
            TitleBar.TextTitleBar(
                title = title,
                color = Color.White,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            Column(
                Modifier
                    .fillMaxSize()
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun CirclesBackgroundPreview() {
    AppBackground.BreathingBackground {}
}

@Preview
@Composable
fun BreathingBackgroundPreview() {
    AllInOneTheme {
        AppBackground.BreathingBackground("主页") {}
    }
}
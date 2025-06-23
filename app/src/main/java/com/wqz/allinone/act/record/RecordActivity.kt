package com.wqz.allinone.act.record

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.record.data.RecordSnapshot
import com.wqz.allinone.act.record.viewmodel.RecordViewModel
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.theme.ThemeColor

/**
 * 溯影棱镜
 * Created by Wu Qizhen on 2025.5.2
 */
class RecordActivity : ComponentActivity() {
    private lateinit var viewModel: RecordViewModel
    // private lateinit var mockRecords: List<RecordSnapshot>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = RecordViewModel(application)
        // mockRecords = viewModel.getRecordSnapshots()

        setContent {
            XBackground.BreathingBackground {
                MemoryPrismScreen()
            }
        }
    }

    @Composable
    fun MemoryPrismScreen() {
        var currentIndex by remember { mutableIntStateOf(0) }
        // val mockRecords by viewModel.recordSnapshots.collectAsState()
        val mockRecords by viewModel.recordSnapshots.collectAsState(initial = emptyList())
        // val currentRecord = mockRecords[currentIndex]

        if (mockRecords.isNotEmpty()) {
            val currentRecord = mockRecords[currentIndex]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        enabled = true,
                        onClick = {
                            currentIndex = (currentIndex + 1) % mockRecords.size
                        }
                    )
                /*.pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -20) { // 向下滑动阈值
                        currentIndex = (currentIndex + 1) % mockRecords.size
                    }
                }
            }*/
            ) {
                // 进度环形图
                CircularProgressIndicator(
                    progress = { currentRecord.progress },
                    modifier = Modifier.fillMaxSize(),
                    // strokeWidth = 8.dp,
                    color = ThemeColor,
                    trackColor = Color.White.copy(alpha = 0.1f)
                )

                AnimatedContent(
                    targetState = currentRecord,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith
                                fadeOut(animationSpec = tween(300))
                    },
                    label = "RecordTransition"
                ) { record ->
                    RecordCard(record = record)
                }
            }
        }
    }

    @Composable
    private fun RecordCard(record: RecordSnapshot) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // verticalArrangement = Arrangement.Center
        ) {
            // 类型标签
            Text(
                text = record.type.uppercase(),
                color = ThemeColor,
                fontSize = 20.sp,
                fontFamily = FontFamily(
                    Font(R.font.fzfengrusongti_regular, FontWeight.Normal)
                ),
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 主标题
            Text(
                text = record.title,
                // style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center
                // modifier = Modifier.alpha(0.9f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 副文本
            Text(
                text = record.subtitle,
                // style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
                // lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "👇",
                // style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
                // lineHeight = 28.sp
            )

            /*Spacer(modifier = Modifier.height(10.dp))

            // 计数徽章
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${record.count} 次记录",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }*/
        }
    }

    // 自定义环形进度指示器
    @Composable
    private fun CircularProgressIndicator(
        progress: () -> Float,
        modifier: Modifier = Modifier,
        color: Color = Color.Blue,
        trackColor: Color = Color.Gray,
        // strokeWidth: Dp = 8.dp // 添加 strokeWidth 参数
    ) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
                .aspectRatio(1f) // 确保画布为正方形
        ) {
            val strokeWidth = 8.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2

            // 绘制背景轨道
            drawCircle(
                color = trackColor,
                radius = radius,
                style = Stroke(strokeWidth)
            )

            // 将坐标系原点移至画布中心
            translate(size.width / 2, size.height / 2) {
                // 绘制进度弧
                drawArc(
                    topLeft = Offset(-radius, -radius),
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360 * progress(),
                    useCenter = false,
                    style = Stroke(strokeWidth, cap = StrokeCap.Round),
                    size = Size(radius * 2, radius * 2)
                )
            }
        }
    }
}
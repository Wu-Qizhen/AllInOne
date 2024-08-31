package com.wqz.allinone.act.anniversary

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.anniversary.viewmodel.AnniversaryViewModel
import com.wqz.allinone.entity.Anniversary
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.ItemX
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.theme.AllInOneTheme
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.math.absoluteValue

/**
 * 纪念日详情
 * Created by Wu Qizhen on 2024.8.31
 */
class AnniversaryDetailsActivity : ComponentActivity() {
    private lateinit var viewModel: AnniversaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AnniversaryViewModel(application)
        val anniversaryId = intent.getIntExtra("ANNIVERSARY_ID", -1)
        // val dao = AnniversaryDatabase.getInstance(application).anniversaryDao()
        // val anniversary = dao.getById(anniversaryId)

        setContent {
            AllInOneTheme {
                AppBackground.CirclesBackground {
                    AnniversaryDetailsScreen(
                        anniversaryId = anniversaryId,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    @Composable
    fun AnniversaryDetailsScreen(
        anniversaryId: Int,
        viewModel: AnniversaryViewModel
    ) {
        val context = LocalContext.current
        val scrollState = rememberScrollState()
        // 状态来存储获取到的周年纪念信息
        var anniversary by remember { mutableStateOf<Anniversary?>(null) }
        var deleteConfirm by remember { mutableStateOf(false) }

        // 使用 LaunchedEffect 在 Composable 的重组中执行协程
        LaunchedEffect(anniversaryId) {
            try {
                // 调用挂起函数获取周年纪念信息
                anniversary = viewModel.getAnniversary(anniversaryId)
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        // 根据状态显示不同的内容
        if (anniversary != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 20.dp, end = 20.dp, bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TitleBar.TextTitleBar(title = R.string.anniversary)
                Spacer(modifier = Modifier.height(15.dp))
                IconButton(
                    onClick = {
                        finish()
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    },
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                AnniversaryCard(anniversary = anniversary!!)
                Spacer(modifier = Modifier.height(15.dp))
                ItemX.Button(
                    icon = R.drawable.ic_delete,
                    text = if (deleteConfirm) "确认删除" else stringResource(id = R.string.delete)
                ) {
                    if (deleteConfirm) {
                        viewModel.deleteAnniversary(anniversaryId)
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(context, "再次点击即可删除", Toast.LENGTH_SHORT).show()
                        deleteConfirm = true
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun AnniversaryCard(
        anniversary: Anniversary
    ) {
        val currentDate = Calendar.getInstance()
        val days = ChronoUnit.DAYS.between(
            anniversary.date,
            currentDate.time.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate()
        ).toInt().plus(1)
        val isAnniversary =
            anniversary.date.month == currentDate.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().month &&
                    anniversary.date.dayOfMonth == currentDate.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().dayOfMonth && days > 1
        val anniversaryCount = ChronoUnit.YEARS.between(
            anniversary.date,
            currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        ).toInt()

        val interactionSource = remember { MutableInteractionSource() }
        Column(
            modifier = Modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = {}
                )
                .wrapContentHeight()
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(240, 175, 57),
                            Color(241, 126, 45),
                        )
                    ), shape = RoundedCornerShape(10.dp)
                )
                .padding(20.dp)
        ) {
            val fontFamily = FontFamily(Font(R.font.din_bold))
            Text(
                text = anniversary.date.year.toString(),
                fontSize = 22.sp,
                fontFamily = fontFamily,
                maxLines = 1
            )
            Text(
                text = "${anniversary.date.month.value} / ${anniversary.date.dayOfMonth}",
                fontSize = 18.sp,
                fontFamily = fontFamily,
                maxLines = 1
            )
            Text(
                text = when (anniversary.date.dayOfWeek.value) {
                    1 -> "周一"
                    2 -> "周二"
                    3 -> "周三"
                    4 -> "周四"
                    5 -> "周五"
                    6 -> "周六"
                    7 -> "周日"
                    else -> "周一"
                },
                fontSize = 16.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(30.dp))
            if (isAnniversary) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$anniversaryCount",
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "周年",
                        fontSize = 30.sp,
                        maxLines = 1
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${days.absoluteValue}",
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = "天",
                        fontSize = 20.sp,
                        maxLines = 1
                    )
                }
            }
            // Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(),
                text = anniversary.content,
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                maxLines = 1
            )
        }
    }
}
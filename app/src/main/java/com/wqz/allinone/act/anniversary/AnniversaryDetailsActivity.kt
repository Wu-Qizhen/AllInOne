package com.wqz.allinone.act.anniversary

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.anniversary.viewmodel.AnniversaryViewModel
import com.wqz.allinone.entity.Anniversary
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.XToast
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.math.absoluteValue

/**
 * 纪念日详情
 * Created by Wu Qizhen on 2024.8.31
 * Refactored by Wu Qizhen on 2024.11.30
 */
class AnniversaryDetailsActivity : ComponentActivity() {
    private lateinit var viewModel: AnniversaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AnniversaryViewModel(application)
        val anniversaryId = intent.getIntExtra("ANNIVERSARY_ID", -1)
        val year = intent.getIntExtra("YEAR", 2020)
        val month = intent.getIntExtra("MONTH", 1)
        val day = intent.getIntExtra("DAY", 1)
        val date = Calendar.getInstance().apply {
            set(year, month - 1, day)
        }

        // val dao = AnniversaryDatabase.getInstance(application).anniversaryDao()
        // val anniversary = dao.getById(anniversaryId)

        setContent {
            XBackground.BreathingBackground {
                AnniversaryDetailsScreen(
                    anniversaryId = anniversaryId,
                    date = date
                )
            }
        }
    }

    @Composable
    fun AnniversaryDetailsScreen(
        anniversaryId: Int,
        date: Calendar
    ) {
        // val context = LocalContext.current
        val scrollState = rememberScrollState()
        // 状态来存储获取到的周年纪念信息
        var anniversary by remember { mutableStateOf<Anniversary?>(null) }
        // var deleteConfirm by remember { mutableStateOf(false) }

        // 使用 LaunchedEffect 在 Composable 的重组中执行协程
        LaunchedEffect(anniversaryId) {
            try {
                // 调用挂起函数获取周年纪念信息
                anniversary = viewModel.getAnniversary(anniversaryId)
            } catch (e: Exception) {
                XToast.showText(e.toString())
            }
        }

        // 根据状态显示不同的内容
        if (anniversary != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 20.dp, end = 20.dp, bottom = 70.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // XTitleBar.TextTitleBar(title = R.string.anniversary)
                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Center
                ) {
                    Row(modifier = Modifier.wrapContentSize()) {
                        IconButton(
                            onClick = {
                                finish()
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    contentDescription = "返回",
                                    tint = Color.White
                                )
                            },
                            modifier = Modifier.size(25.dp)
                        )

                        /*Spacer(modifier = Modifier.width(10.dp))

                        IconButton(
                            onClick = {
                                if (deleteConfirm) {
                                    viewModel.deleteAnniversary(anniversaryId)
                                    XToast.showText(context, R.string.deleted, Toast.LENGTH_SHORT)
                                        .show()
                                    finish()
                                } else {
                                    XToast.showText(context, "再次点击即可删除", Toast.LENGTH_SHORT)
                                        .show()
                                    deleteConfirm = true
                                }
                            },
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_delete),
                                    contentDescription = "删除",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            modifier = Modifier.size(25.dp)
                        )*/

                        Spacer(modifier = Modifier.width(10.dp))

                        IconButton(
                            onClick = {
                                val intent =
                                    Intent(
                                        this@AnniversaryDetailsActivity,
                                        AnniversaryEditActivity::class.java
                                    )
                                intent.putExtra("ANNIVERSARY_ID", anniversaryId)
                                startActivity(intent)
                                finish()
                            },
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_edit),
                                    contentDescription = "编辑",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }

                /*IconButton(
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
                )*/

                Spacer(modifier = Modifier.height(15.dp))

                AnniversaryCard(
                    anniversary = anniversary!!,
                    date
                )

                /*XItem.Button(
                    icon = R.drawable.ic_delete,
                    text = if (deleteConfirm) "确认删除" else stringResource(id = R.string.delete)
                ) {
                    if (deleteConfirm) {
                        viewModel.deleteAnniversary(anniversaryId)
                        XToast.showText(context, "删除成功")
                        finish()
                    } else {
                        XToast.showText(context, "再次点击即可删除")
                        deleteConfirm = true
                    }
                }*/
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun AnniversaryCard(
        anniversary: Anniversary,
        currentDate: Calendar
    ) {
        // val currentDate = Calendar.getInstance()
        val days = ChronoUnit.DAYS.between(
            anniversary.date,
            currentDate.time.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate()
        ).toInt() // .plus(1)
        val isAnniversary =
            anniversary.date.month == currentDate.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().month &&
                    anniversary.date.dayOfMonth == currentDate.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().dayOfMonth && days > 1
        val anniversaryCount = ChronoUnit.YEARS.between(
            anniversary.date,
            currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        ).toInt()

        val textColors = listOf(
            Color(255, 251, 221),
            Color.White
        )
        val textColorId: Int
        val backgroundColors = listOf(
            Brush.horizontalGradient(
                listOf(
                    // Color(253, 7, 35),
                    Color(236, 74, 49),
                    Color(236, 74, 49),
                    Color(248, 71, 97)
                    // Color(254, 223, 124)
                )
            ),
            Brush.horizontalGradient(
                listOf(
                    Color(240, 175, 57),
                    Color(241, 126, 45)
                )
            ),
            Brush.horizontalGradient(
                listOf(
                    Color(141, 147, 250),
                    Color(96, 103, 228)
                )
            )
        )
        val backgroundColorId: Int
        val yearText = anniversary.date.year.toString()
        val monthDayText = "${anniversary.date.month.value} / ${anniversary.date.dayOfMonth}"
        val weekDayText = when (anniversary.date.dayOfWeek.value) {
            1 -> "周一"
            2 -> "周二"
            3 -> "周三"
            4 -> "周四"
            5 -> "周五"
            6 -> "周六"
            7 -> "周日"
            else -> "周一"
        }
        val countText: String
        val labelText: String
        if (isAnniversary) {
            countText = "$anniversaryCount"
            labelText = "周年"
            backgroundColorId = 0
            textColorId = 0
        } else {
            countText = if (days > 0) "${days.plus(1)}" else "${days.absoluteValue}"
            labelText = if (days > 0) "天" else "天后"
            backgroundColorId = if (days > 0) 1 else 2
            textColorId = 1
        }

        val textColor = textColors[textColorId]

        val interactionSource = remember { MutableInteractionSource() }

        Column(
            modifier = Modifier
                .clickVfx(
                    interactionSource = interactionSource,
                    enabled = true,
                    onClick = {
                        val intent =
                            Intent(
                                this@AnniversaryDetailsActivity,
                                AnniversaryFullDisplayActivity::class.java
                            )
                        intent.putExtra("YEAR", yearText)
                        intent.putExtra("MONTH_DAY", monthDayText)
                        intent.putExtra("WEEK_DAY", weekDayText)
                        intent.putExtra("COUNT", countText)
                        intent.putExtra("LABEL", labelText)
                        intent.putExtra("CONTENT", anniversary.content)
                        intent.putExtra("TEXT_COLOR", textColorId)
                        intent.putExtra("BACKGROUND_COLOR", backgroundColorId)
                        startActivity(intent)
                    }
                )
                .wrapContentHeight()
                .fillMaxWidth()
                .background(
                    brush = backgroundColors[backgroundColorId],
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(20.dp)
        ) {
            val fontFamily = FontFamily(Font(R.font.din_bold))

            Text(
                text = yearText,
                fontSize = 22.sp,
                fontFamily = fontFamily,
                color = textColor,
                maxLines = 1
            )

            Text(
                text = monthDayText,
                fontSize = 18.sp,
                fontFamily = fontFamily,
                color = textColor,
                maxLines = 1
            )

            Text(
                text = weekDayText,
                fontSize = 16.sp,
                color = textColor,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (isAnniversary) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = countText,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        color = textColor,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = labelText,
                        fontSize = 30.sp,
                        color = textColor,
                        maxLines = 1
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = countText,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        color = textColor,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = labelText,
                        fontSize = 20.sp,
                        color = textColor,
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
                color = textColor,
                maxLines = 1
            )
        }
    }
}
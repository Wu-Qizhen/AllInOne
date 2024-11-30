package com.wqz.allinone.act.anniversary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.anniversary.data.AnniversaryDisplay
import com.wqz.allinone.ui.theme.AllInOneTheme

/**
 * 纪念日全屏显示
 * Created by Wu Qizhen on 2024.11.30
 */
class AnniversaryFullDisplayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val yearText = intent.getStringExtra("YEAR")!!
        val monthDayText = intent.getStringExtra("MONTH_DAY")!!
        val weekDayText = intent.getStringExtra("WEEK_DAY")!!
        val countText = intent.getStringExtra("COUNT")!!
        val labelText = intent.getStringExtra("LABEL")!!
        val contentText = intent.getStringExtra("CONTENT")!!
        val textColorId = intent.getIntExtra("TEXT_COLOR", 0)
        val backgroundColorId = intent.getIntExtra("BACKGROUND_COLOR", 0)

        setContent {
            AllInOneTheme {
                AnniversaryDisplayScreen(
                    anniversaryDisplay = AnniversaryDisplay(
                        yearText,
                        monthDayText,
                        weekDayText,
                        countText,
                        labelText,
                        contentText,
                        textColorId,
                        backgroundColorId
                    )
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun AnniversaryDisplayScreen(
        anniversaryDisplay: AnniversaryDisplay
    ) {
        val textColors = listOf(
            Color(255, 251, 221),
            Color.White
        )
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
        val fontFamily = FontFamily(Font(R.font.din_bold))
        val textColor = textColors[anniversaryDisplay.textColorId]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = backgroundColors[anniversaryDisplay.backgroundColorId],
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
            ) {
                Column(
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "${anniversaryDisplay.yearText} / ${anniversaryDisplay.monthDayText}",
                        fontSize = 22.sp,
                        fontFamily = fontFamily,
                        color = textColor,
                        maxLines = 1
                    )

                    Text(
                        text = anniversaryDisplay.weekDayText,
                        fontSize = 16.sp,
                        color = textColor,
                        maxLines = 1
                    )
                }

                Column(
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    if (anniversaryDisplay.backgroundColorId == 0) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = anniversaryDisplay.countText,
                                fontSize = 80.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = fontFamily,
                                color = textColor,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 10.dp),
                                text = anniversaryDisplay.labelText,
                                fontSize = 30.sp,
                                color = textColor,
                                maxLines = 1
                            )
                        }
                    } else {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = anniversaryDisplay.countText,
                                fontSize = 50.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = fontFamily,
                                color = textColor,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 5.dp),
                                text = anniversaryDisplay.labelText,
                                fontSize = 20.sp,
                                color = textColor,
                                maxLines = 1
                            )
                        }
                    }

                    Text(
                        modifier = Modifier.basicMarquee(),
                        text = anniversaryDisplay.contentText,
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        color = textColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
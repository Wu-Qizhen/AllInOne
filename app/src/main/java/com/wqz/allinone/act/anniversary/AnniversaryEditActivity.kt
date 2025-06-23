package com.wqz.allinone.act.anniversary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.R
import com.wqz.allinone.act.anniversary.viewmodel.AnniversaryViewModel
import com.wqz.allinone.entity.Anniversary
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.XToast
import com.wqz.allinone.ui.color.TextFieldColor
import com.wqz.allinone.ui.property.ButtonCategory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 编辑纪念日
 * Created by Wu Qizhen on 2025.6.12
 */
class AnniversaryEditActivity : ComponentActivity() {
    private lateinit var viewModel: AnniversaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AnniversaryViewModel(application)

        val anniversaryId = intent.getIntExtra("ANNIVERSARY_ID", -1)

        setContent {
            XBackground.BreathingBackground(titleId = R.string.edit_anniversary) {
                AnniversaryEditScreen(anniversaryId)
            }
        }
    }

    @Composable
    fun AnniversaryEditScreen(anniversaryId: Int) {
        val context = LocalContext.current
        var content by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var deleteConfirm by remember { mutableIntStateOf(0) }
        var anniversary by remember { mutableStateOf<Anniversary?>(null) }

        // 使用 LaunchedEffect 在 Composable 的重组中执行协程
        LaunchedEffect(anniversaryId) {
            try {
                // 调用挂起函数获取周年纪念信息
                anniversary = viewModel.getAnniversary(anniversaryId)
            } catch (e: Exception) {
                XToast.showText(e.toString())
            }
        }

        if (anniversary != null) {
            content = anniversary!!.content
            date = anniversary!!.date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }

        XCard.SurfaceCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = content,
                onValueChange = { content = it },
                colors = TextFieldColor.colors(),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.misans_regular))
                ),
                label = {
                    Text(
                        text = "纪念内容",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        XCard.SurfaceCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = date,
                onValueChange = { date = it },
                colors = TextFieldColor.colors(),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.misans_regular))
                ),
                maxLines = 1,
                label = {
                    Text(
                        text = "纪念日期",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            XItem.Button(
                icon = R.drawable.ic_delete,
                text = stringResource(id = R.string.delete),
                color = ButtonCategory.WARNING_BUTTON
            ) {
                deleteConfirm++
                if (deleteConfirm > 2) {
                    viewModel.deleteAnniversary(anniversaryId)
                    XToast.showText(
                        this@AnniversaryEditActivity,
                        R.string.deleted
                    )
                    finish()
                } else {
                    XToast.showText("再按 ${3 - deleteConfirm} 次即可删除")
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            XItem.Button(
                icon = R.drawable.ic_todo,
                text = stringResource(id = R.string.save),
                color = ButtonCategory.SAFE_BUTTON
            ) {
                if (content.trim().isNotEmpty() && date.matches(Regex("\\d{4}.\\d{2}.\\d{2}"))) {
                    val modifiedAnniversary = Anniversary(
                        id = anniversaryId,
                        content = content.trim(),
                        date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                    )
                    viewModel.viewModelScope.launch {
                        viewModel.updateAnniversary(modifiedAnniversary)
                    }
                    XToast.showText(
                        this@AnniversaryEditActivity,
                        R.string.saved
                    )
                    finish()
                } else {
                    XToast.showText(context, R.string.input_anniversary_empty)
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}
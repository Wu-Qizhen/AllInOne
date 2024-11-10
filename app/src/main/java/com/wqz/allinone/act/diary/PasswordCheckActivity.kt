package com.wqz.allinone.act.diary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.secure.SecurePreferencesManager
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.ItemX
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.TitleBar
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor

/**
 * 密码校验页面
 * Created by Wu Qizhen on 2024.10.3
 */
class PasswordCheckActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground {
                    PasswordCheckScreen()
                }
            }
        }
    }

    @Composable
    fun PasswordCheckScreen() {
        val context = LocalContext.current
        val backgroundColor = BackgroundColor.DEFAULT_GRAY
        val borderColors = BorderColor.DEFAULT_GRAY
        val borderWidth = 0.5f.dp
        val scrollState = rememberScrollState()
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        val passwordPrefs = remember { SecurePreferencesManager(context = context) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 50.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleBar.TextTitleBar(title = R.string.password_check)

            Column(
                modifier = Modifier
                    .clickVfx { }
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .background(backgroundColor, RoundedCornerShape(10.dp))
                    .border(
                        width = borderWidth,
                        shape = RoundedCornerShape(10.dp),
                        brush = Brush.linearGradient(
                            borderColors,
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent, // 背景颜色
                        focusedContainerColor = Color.Transparent, // 背景颜色
                        unfocusedIndicatorColor = Color.Transparent, // 下划线颜色
                        focusedIndicatorColor = Color.Transparent, // 下划线颜色
                        cursorColor = ThemeColor, // 光标颜色
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray
                    ),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.misans_regular))
                    ),
                    singleLine = true,
                    label = {
                        Text(
                            text = "密码",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                tint = Color.White,
                                contentDescription = if (passwordVisible) "隐藏密码" else "显示密码"
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            ItemX.Button(icon = R.drawable.ic_todo, text = "进入") {
                if (passwordPrefs.getPassword() == password) {
                    startActivity(Intent(context, DiaryPreviewActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
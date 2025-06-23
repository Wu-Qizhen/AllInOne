package com.wqz.allinone.act.password

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.backup.BackupActivity
import com.wqz.allinone.act.diary.DiaryPreviewActivity
import com.wqz.allinone.act.recovery.RecoveryActivity
import com.wqz.allinone.preference.PasswordPreferencesManager
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.XToast
import com.wqz.allinone.ui.color.TextFieldColor

/**
 * 密码校验
 * Created by Wu Qizhen on 2024.10.3
 */
class PasswordCheckActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestCode = intent.getIntExtra("REQUEST_CODE", 0)

        setContent {
            XBackground.BreathingBackground(titleId = R.string.password_check) {
                PasswordCheckScreen(
                    requestCode = requestCode
                )
            }
        }
    }

    @Composable
    fun PasswordCheckScreen(
        requestCode: Int
    ) {
        val context = LocalContext.current
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        val passwordPrefs = remember { PasswordPreferencesManager(context = context) }
        val focusRequester = remember { FocusRequester() }
        // val focusManager = LocalFocusManager.current

        // 在 Composable 第一次被绘制时请求焦点
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        XCard.SurfaceCard {
            TextField(
                // 将 focusRequester 关联到 TextField
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = password,
                onValueChange = { password = it },
                colors = TextFieldColor.colors(),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

        XItem.Button(icon = R.drawable.ic_todo, text = "进入") {
            if (passwordPrefs.getPassword() == password) {
                when (requestCode) {
                    0 -> {
                        startActivity(Intent(context, PasswordChangeActivity::class.java))
                        finish()
                    }

                    1 -> {
                        startActivity(Intent(context, DiaryPreviewActivity::class.java))
                        finish()
                    }

                    2 -> {
                        startActivity(Intent(context, BackupActivity::class.java))
                        finish()
                    }

                    3 -> {
                        startActivity(Intent(context, RecoveryActivity::class.java))
                        finish()
                    }

                    else -> {
                        XToast.showText(context, R.string.password_error)
                    }
                }
            } else {
                XToast.showText(context, R.string.password_error)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}
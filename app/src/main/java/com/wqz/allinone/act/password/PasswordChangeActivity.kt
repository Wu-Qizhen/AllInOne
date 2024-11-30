package com.wqz.allinone.act.password

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Modifier
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
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor

class PasswordChangeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground(title = R.string.password_change) {
                    PasswordChangeScreen()
                }
            }
        }
    }

    @Composable
    fun PasswordChangeScreen() {
        val context = LocalContext.current
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        val passwordPrefs = remember { SecurePreferencesManager(context = context) }

        XCard.SurfaceCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = newPassword,
                onValueChange = { newPassword = it },
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
                        text = "新密码",
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

        XCard.SurfaceCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
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
                        text = "确认密码",
                        fontWeight = FontWeight.Bold
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        XItem.Button(icon = R.drawable.ic_todo, text = "修改") {
            if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (newPassword == confirmPassword) {
                    passwordPrefs.savePassword(newPassword)
                    Toast.makeText(context, R.string.modified, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(context, R.string.password_not_match, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(context, R.string.password_empty, Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}
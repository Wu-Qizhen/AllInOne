package com.wqz.allinone.act.bookmark

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.act.bookmark.viewmodel.BookmarkViewModel
import com.wqz.allinone.entity.Link
import com.wqz.allinone.ui.AppBackground
import com.wqz.allinone.ui.ItemX
import com.wqz.allinone.ui.ModifierExtends.clickVfx
import com.wqz.allinone.ui.TitleBar
import com.wqz.allinone.ui.color.BackgroundColor
import com.wqz.allinone.ui.color.BorderColor
import com.wqz.allinone.ui.theme.AllInOneTheme
import com.wqz.allinone.ui.theme.ThemeColor
import java.util.regex.Pattern
import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.ui.platform.LocalContext

/**
 * 链接详情
 * Created by Wu Qizhen on 2024.11.3
 */
class LinkDetailsActivity : ComponentActivity() {
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linkId = intent.getIntExtra("LINK_ID", -1)
        val linkTitle = intent.getStringExtra("LINK_TITLE")
        val linkUrl = intent.getStringExtra("LINK_URL")
        val folderId = intent.getIntExtra("FOLDER_ID", -1)

        viewModel = BookmarkViewModel(application)

        setContent {
            AllInOneTheme {
                AppBackground.BreathingBackground {
                    LinkDetailsScreen(
                        link = Link(
                            id = linkId,
                            title = linkTitle ?: "",
                            url = linkUrl ?: "",
                            folder = folderId
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun LinkDetailsScreen(
        link: Link
    ) {
        val context = LocalContext.current
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val scrollState = rememberScrollState()
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val backgroundColor =
            if (isPressed.value) BackgroundColor.PRESSED_GRAY else BackgroundColor.DEFAULT_GRAY
        val borderColors = BorderColor.DEFAULT_GRAY
        val borderWidth = 0.4f.dp
        var name by remember { mutableStateOf(link.title) }
        var url by remember { mutableStateOf(link.url) }
        var deleteConfirm by remember { mutableIntStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleBar.TextTitleBar(title = R.string.link_details)

            Column(
                modifier = Modifier
                    .clickVfx(
                        interactionSource = interactionSource,
                        enabled = true,
                        onClick = { }
                    )
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
                    value = name,
                    onValueChange = { name = it },
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
                    label = {
                        Text(
                            text = "书签名",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 0.5f.dp,
                    color = Color(54, 54, 54)
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = url,
                    onValueChange = { url = it },
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
                    label = {
                        Text(
                            text = "URL",
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
                ItemX.Button(
                    icon = R.drawable.ic_copy,
                    text = stringResource(id = R.string.copy)
                ) {
                    val clip = ClipData.newPlainText("URL", url)
                    clipboardManager.setPrimaryClip(clip)
                    Toast.makeText(context, "链接已复制", Toast.LENGTH_SHORT).show()
                }

                Spacer(modifier = Modifier.width(10.dp))

                ItemX.Button(
                    icon = R.drawable.ic_visit,
                    text = stringResource(id = R.string.visit)
                ) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(link.url)
                    try {
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "无法打开链接",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ItemX.Button(
                    icon = R.drawable.ic_delete,
                    text = stringResource(id = R.string.delete)
                ) {
                    deleteConfirm++
                    if (deleteConfirm > 2) {
                        viewModel.deleteLink(link)
                        Toast.makeText(
                            context,
                            "已删除",
                            Toast.LENGTH_SHORT
                        ).show()
                        /*val result = Intent().putExtra("delete", true)
                        setResult(Activity.RESULT_OK, result)*/
                        finish()
                    } else {
                        Toast.makeText(
                            context,
                            "再按 ${3 - deleteConfirm} 次即可删除",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                ItemX.Button(
                    icon = R.drawable.ic_todo,
                    text = stringResource(id = R.string.modify)
                ) {
                    val title = name.trim().replace("\n", "")
                    val site = url.trim().replace("\n", "")
                    if (title.isNotEmpty() && site.isNotEmpty()) {
                        if (isValidUrl(site)) {
                            val newLink = Link(
                                id = link.id,
                                title = title,
                                url = url,
                                folder = link.folder
                            )
                            viewModel.updateLink(newLink)
                            Toast.makeText(
                                context,
                                "修改成功",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                context,
                                "请输入正确的链接",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "名称或链接不能为空",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }

    private fun isValidUrl(url: String): Boolean {
        val regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(url).matches()
    }
}
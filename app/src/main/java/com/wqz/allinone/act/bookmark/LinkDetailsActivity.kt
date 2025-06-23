package com.wqz.allinone.act.bookmark

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import com.wqz.allinone.R
import com.wqz.allinone.act.bookmark.viewmodel.BookmarkViewModel
import com.wqz.allinone.entity.Link
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.XToast
import com.wqz.allinone.ui.color.TextFieldColor
import com.wqz.allinone.ui.property.BorderWidth
import com.wqz.allinone.ui.property.ButtonCategory
import java.util.regex.Pattern

/**
 * 链接详情
 * Created by Wu Qizhen on 2024.11.3
 * Refactored by Wu Qizhen on 2024.11.30
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
            XBackground.BreathingBackground(titleId = R.string.link_details) {
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

    @Composable
    fun LinkDetailsScreen(
        link: Link
    ) {
        val context = LocalContext.current
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        var name by remember { mutableStateOf(link.title) }
        var url by remember { mutableStateOf(link.url) }
        var deleteConfirm by remember { mutableIntStateOf(0) }

        XCard.SurfaceCard {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                colors = TextFieldColor.colors(),
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
                thickness = BorderWidth.DEFAULT_WIDTH,
                color = Color(54, 54, 54)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = url,
                onValueChange = { url = it },
                colors = TextFieldColor.colors(),
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
            XItem.Button(
                icon = R.drawable.ic_copy,
                text = stringResource(id = R.string.copy)
            ) {
                val clip = ClipData.newPlainText("URL", url)
                clipboardManager.setPrimaryClip(clip)
                XToast.showText(this@LinkDetailsActivity, R.string.copied)
            }

            Spacer(modifier = Modifier.width(10.dp))

            XItem.Button(
                icon = R.drawable.ic_visit,
                text = stringResource(id = R.string.visit)
            ) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(link.url)
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    XToast.showText(
                        context,
                        R.string.open_error
                    )
                }
            }
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
                    viewModel.deleteLink(link)
                    XToast.showText(
                        context,
                        R.string.deleted
                    )
                    /*val result = Intent().putExtra("delete", true)
                    setResult(Activity.RESULT_OK, result)*/
                    finish()
                } else {
                    XToast.showText("再按 ${3 - deleteConfirm} 次即可删除")
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            XItem.Button(
                icon = R.drawable.ic_todo,
                text = stringResource(id = R.string.modify),
                color = ButtonCategory.SAFE_BUTTON
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
                        XToast.showText(
                            context,
                            R.string.modified
                        )
                    } else {
                        XToast.showText(
                            context,
                            R.string.invalid_url
                        )
                    }
                } else {
                    XToast.showText(
                        context,
                        R.string.input_link_empty
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }

    private fun isValidUrl(url: String): Boolean {
        val regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(url).matches()
    }
}
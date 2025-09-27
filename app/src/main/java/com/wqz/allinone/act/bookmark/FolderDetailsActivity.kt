package com.wqz.allinone.act.bookmark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.R
import com.wqz.allinone.act.bookmark.viewmodel.BookmarkViewModel
import com.wqz.allinone.entity.Folder
import com.wqz.allinone.ui.XBackground
import com.wqz.allinone.ui.XCard
import com.wqz.allinone.ui.XItem
import com.wqz.allinone.ui.XToast
import com.wqz.allinone.ui.color.TextFieldColor
import com.wqz.allinone.ui.property.BorderWidth
import com.wqz.allinone.ui.property.ButtonCategory
import kotlinx.coroutines.launch

/**
 * 文件夹详情
 * Created by Wu Qizhen on 2024.11.3
 * Refactored by Wu Qizhen on 2024.11.30
 */
class FolderDetailsActivity : ComponentActivity() {
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val folderId = intent.getIntExtra("FOLDER_ID", -1)
        val folderName = intent.getStringExtra("FOLDER_NAME")

        viewModel = BookmarkViewModel(application)

        viewModel.viewModelScope.launch {
            val linkCount = viewModel.getLinkCount(folderId)

            setContent {
                XBackground.BreathingBackground(titleId = R.string.folder_details) {
                    FolderDetailsScreen(
                        folder = Folder(
                            id = folderId,
                            name = folderName ?: ""
                        ),
                        linkCount = linkCount
                    )
                }
            }
        }
    }

    @Composable
    fun FolderDetailsScreen(
        folder: Folder,
        linkCount: Int
    ) {
        var name by remember { mutableStateOf(folder.name) }
        var deleteConfirm by remember { mutableIntStateOf(0) }

        XCard.LivelyCard {
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
                        text = "名称",
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = BorderWidth.DEFAULT_WIDTH,
                color = Color(54, 54, 54)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)
            ) {
                Text(
                    text = "书签数：",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = linkCount.toString(),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
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
                    viewModel.deleteLinks(folderId = folder.id)
                    viewModel.deleteFolder(folder)
                    XToast.showText(
                        this@FolderDetailsActivity,
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
                text = stringResource(id = R.string.modify),
                color = ButtonCategory.SAFE_BUTTON
            ) {
                val title = name.trim().replace("\n", "")
                if (title.isNotEmpty()) {
                    viewModel.updateFolder(
                        folder.copy(
                            name = title
                        )
                    )
                    XToast.showText(
                        this@FolderDetailsActivity,
                        R.string.modified
                    )
                    finish()
                } else {
                    XToast.showText(
                        this@FolderDetailsActivity,
                        R.string.input_folder_name_empty
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}
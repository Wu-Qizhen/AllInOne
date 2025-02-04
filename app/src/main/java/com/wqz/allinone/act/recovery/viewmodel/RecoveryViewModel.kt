package com.wqz.allinone.act.recovery.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.dao.FolderDao
import com.wqz.allinone.dao.LinkDao
import com.wqz.allinone.database.BookmarkDatabase
import com.wqz.allinone.entity.Folder
import com.wqz.allinone.entity.Link
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * 数据导入视图模型
 * Created by Wu Qizhen on 2025.2.4
 */
class RecoveryViewModel(application: Application) : AndroidViewModel(application) {
    val importStatus = MutableStateFlow(listOf("* 导入结果："))

    private val folderDao: FolderDao
    private val linkDao: LinkDao

    init {
        val bookmarkDatabase = BookmarkDatabase.getDatabase(application)
        folderDao = bookmarkDatabase.folderDao()
        linkDao = bookmarkDatabase.linkDao()
    }

    fun importDatabase(context: Context, databaseName: String, label: String) {
        // 目标文件路径（应用数据库目录）
        val targetFile = context.getDatabasePath("$databaseName.db")
        // 源文件路径（固定路径，例如下载目录）
        val sourceFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "${databaseName.uppercase()}.db"
        )

        /*// 打印路径以调试
        Log.d("ImportDatabase", "源文件路径: ${sourceFile.absolutePath}")
        Log.d("ImportDatabase", "目标文件路径: ${targetFile.absolutePath}")*/

        try {
            // 检查源文件是否存在
            if (!sourceFile.exists()) {
                viewModelScope.launch {
                    importStatus.emit(importStatus.value.plus("  ${label}导入失败：未找到文件"))
                }
                return
            }

            // 确保目标文件的父目录存在
            targetFile.parentFile?.takeIf { !it.exists() }?.mkdirs()

            // 执行文件复制
            sourceFile.inputStream().use { inputStream ->
                FileOutputStream(targetFile).use { outputStream ->
                    copyFile(inputStream, outputStream)
                }
            }

            // 更新状态：导入成功
            viewModelScope.launch {
                importStatus.emit(importStatus.value.plus("  ${label}导入成功"))
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            viewModelScope.launch {
                importStatus.emit(importStatus.value.plus("  ${label}导入失败：权限不足"))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ImportDatabase", "IO 异常", e)
            viewModelScope.launch {
                importStatus.emit(importStatus.value.plus("  ${label}导入失败：IO 异常"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            viewModelScope.launch {
                importStatus.emit(importStatus.value.plus("  ${label}导入失败：未知错误"))
            }
        }
    }

    // 复用相同的文件复制方法
    private fun copyFile(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (inputStream.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
    }

    private fun findBackupFile(context: Context, fileName: String): Uri? {
        val contentResolver: ContentResolver = context.contentResolver
        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)

        val projection = arrayOf(MediaStore.Downloads._ID, MediaStore.Downloads.DISPLAY_NAME)
        val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(fileName)

        val cursor = contentResolver.query(collection, projection, selection, selectionArgs, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Downloads._ID))
                return ContentUris.withAppendedId(collection, id)
            }
        }
        return null
    }

    suspend fun importBookmarks() {
        // 固定文件名格式：BOOKMARK.txt
        val backupFileName = "BOOKMARK.txt"
        val uri = findBackupFile(getApplication(), backupFileName)

        uri?.let { targetUri ->
            try {
                getApplication<Application>().contentResolver.openInputStream(targetUri)
                    ?.use { inputStream ->
                        val bookmarks = inputStream.bufferedReader().use { it.readText() }
                        parseAndSaveBookmarks(bookmarks)
                    }
                importStatus.emit(importStatus.value.plus("  书签宝导入成功"))
            } catch (e: Exception) {
                e.printStackTrace()
                importStatus.emit(importStatus.value.plus("  书签宝导入失败"))
            }
        } ?: run {
            importStatus.emit(importStatus.value.plus("  书签宝导入失败：未找到备份文件"))
        }
    }

    private suspend fun parseAndSaveBookmarks(bookmarks: String) = withContext(Dispatchers.IO) {
        val lines = bookmarks.lines()
        var currentFolderId: Long? = null

        for (line in lines) {
            if (line.isBlank()) continue

            if (!line.contains("@")) {
                // This is a folder name
                currentFolderId = folderDao.insertFolder(Folder(null, line))
            } else {
                // This is a link
                val parts = line.split("@")
                if (parts.size == 2) {
                    val title = parts[0]
                    val url = parts[1]
                    currentFolderId?.let { folderId ->
                        linkDao.insertLink(Link(null, title, url, folderId.toInt()))
                    }
                }
            }
        }
    }
}
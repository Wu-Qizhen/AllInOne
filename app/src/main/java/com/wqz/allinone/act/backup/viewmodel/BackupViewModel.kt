package com.wqz.allinone.act.backup.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.dao.FolderDao
import com.wqz.allinone.dao.LinkDao
import com.wqz.allinone.database.BookmarkDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 数据导出视图模型
 * Created by Wu Qizhen on 2024.11.29
 */
class BackupViewModel(application: Application) : AndroidViewModel(application) {
    // private val _exportStatus = MutableLiveData<List<String>>(emptyList())
    // val exportStatus: LiveData<List<String>> = _exportStatus
    val exportStatus = MutableStateFlow<List<String>>(emptyList())

    private val folderDao: FolderDao
    private val linkDao: LinkDao

    init {
        val bookmarkDatabase = BookmarkDatabase.getDatabase(application)
        folderDao = bookmarkDatabase.folderDao()
        linkDao = bookmarkDatabase.linkDao()
    }

    fun exportDatabase(context: Context, databaseName: String, label: String) {
        val sourceFile = context.getDatabasePath(databaseName)
        val now = Date()
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val timestamp = dateFormat.format(now)
        val backupFileName = "${databaseName.uppercase()}_$timestamp.db"

        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, backupFileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/x-sqlite3")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val contentResolver = context.contentResolver
        val uri: Uri? = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        uri?.let { targetUri ->
            try {
                contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                    sourceFile.inputStream().use { inputStream ->
                        copyFile(inputStream, outputStream)
                    }
                }
                // _exportStatus.postValue(_exportStatus.value?.plus("  ${label}导出成功"))
                viewModelScope.launch {
                    exportStatus.emit(exportStatus.value.plus("  ${label}导出成功"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // _exportStatus.postValue(_exportStatus.value?.plus("  ${label}导出失败"))
                viewModelScope.launch {
                    exportStatus.emit(exportStatus.value.plus("  ${label}导出失败"))
                }
            }
        }
    }

    private fun copyFile(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (inputStream.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
    }

    suspend fun exportBookmarks() {
        val bookmarks = getBookmarks()
        val now = Date()
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val timestamp = dateFormat.format(now)
        val backupFileName = "BOOKMARKS_$timestamp.txt"

        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, backupFileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/plain")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val contentResolver: ContentResolver = getApplication<Application>().contentResolver
        val uri: Uri? = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        uri?.let { targetUri ->
            try {
                contentResolver.openOutputStream(targetUri).use { outputStream ->
                    outputStream?.write(bookmarks.toByteArray())
                }
                // _exportStatus.postValue(_exportStatus.value?.plus("  书签宝导出成功"))
                exportStatus.emit(exportStatus.value.plus("  书签宝导出成功"))
            } catch (e: Exception) {
                e.printStackTrace()
                // _exportStatus.postValue(_exportStatus.value?.plus("  书签宝导出失败"))
                exportStatus.emit(exportStatus.value.plus("  书签宝导出失败"))
            }
        }
    }

    private suspend fun getBookmarks(): String = withContext(Dispatchers.IO) {
        val stringBuilder = StringBuilder()
        // 获取所有文件夹
        val allFolders = folderDao.getAllFoldersForExport()
        if (allFolders.isNotEmpty()) {
            for (folder in allFolders) {
                stringBuilder.append(folder.name).append("\n")
                // 获取当前文件夹下的所有链接
                val linksInFolder = linkDao.getLinksForFolder(folder.id!!)
                for (link in linksInFolder) {
                    stringBuilder.append("${link.title}@${link.url}\n")
                }
                // 在每个文件夹之后添加两个换行符
                stringBuilder.append("\n")
            }
        } else {
            stringBuilder.append("No bookmarks found.")
        }
        return@withContext stringBuilder.toString()
    }
}
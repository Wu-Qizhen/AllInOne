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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wqz.allinone.dao.DiaryDao
import com.wqz.allinone.dao.FolderDao
import com.wqz.allinone.dao.LinkDao
import com.wqz.allinone.dao.NoteDao
import com.wqz.allinone.dao.TodoDao
import com.wqz.allinone.database.BookmarkDatabase
import com.wqz.allinone.database.DiaryDatabase
import com.wqz.allinone.database.NoteDatabase
import com.wqz.allinone.database.TodoDatabase
import com.wqz.allinone.util.LocalDateDeserializer
import com.wqz.allinone.util.LocalDateSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

/**
 * 数据导出视图模型
 * Created by Wu Qizhen on 2024.11.29
 */
class BackupViewModel(application: Application) : AndroidViewModel(application) {
    val exportStatus = MutableStateFlow(listOf("* 导出结果："))

    private val folderDao: FolderDao
    private val linkDao: LinkDao
    private val todoDao: TodoDao
    private val noteDao: NoteDao
    private val diaryDao: DiaryDao

    init {
        val bookmarkDatabase = BookmarkDatabase.getDatabase(application)
        folderDao = bookmarkDatabase.folderDao()
        linkDao = bookmarkDatabase.linkDao()

        val todoDatabase = TodoDatabase.getDatabase(application)
        todoDao = todoDatabase.todoDao()

        val noteDatabase = NoteDatabase.getDatabase(application)
        noteDao = noteDatabase.noteDao()

        val diaryDatabase = DiaryDatabase.getDatabase(application)
        diaryDao = diaryDatabase.diaryDao()
    }

    fun exportJson(context: Context, databaseName: String, label: String) {
        when (databaseName) {
            "Todo" -> exportTodosToJson(context, label)
            "Note" -> exportNotesToJson(context, label)
            "Diary" -> exportDiariesToJson(context, label)
        }
    }

    private fun exportTodosToJson(context: Context, label: String) {
        viewModelScope.launch {
            try {
                val todos = withContext(Dispatchers.IO) { todoDao.getAll() }
                val gson = createGsonInstance()
                val json = gson.toJson(todos)

                saveJsonToFile(context, "Todo.json", json, label)
            } catch (e: Exception) {
                updateExportStatus(label, " JSON 导出失败")
            }
        }
    }

    private fun exportNotesToJson(context: Context, label: String) {
        viewModelScope.launch {
            try {
                val notes = withContext(Dispatchers.IO) { noteDao.getAll() }
                val gson = createGsonInstance()
                val json = gson.toJson(notes)

                saveJsonToFile(context, "Note.json", json, label)
            } catch (e: Exception) {
                updateExportStatus(label, " JSON 导出失败")
            }
        }
    }

    private fun exportDiariesToJson(context: Context, label: String) {
        viewModelScope.launch {
            try {
                val diaries = withContext(Dispatchers.IO) { diaryDao.getAll() }
                val gson = createGsonInstance()
                val json = gson.toJson(diaries)

                saveJsonToFile(context, "Diary.json", json, label)
            } catch (e: Exception) {
                updateExportStatus(label, " JSON 导出失败")
            }
        }
    }

    private suspend fun saveJsonToFile(
        context: Context,
        fileName: String,
        jsonData: String,
        label: String
    ) {
        val backupFileName = "${fileName.substringBefore(".json")}_${getTimestamp()}.json"

        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, backupFileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/json")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val contentResolver = context.contentResolver
        val uri: Uri? = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        uri?.let { targetUri ->
            try {
                contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                    OutputStreamWriter(outputStream).use { writer ->
                        writer.write(jsonData)
                    }
                }
                updateExportStatus(label, " JSON 导出成功")
            } catch (e: Exception) {
                // e.printStackTrace()
                updateExportStatus(label, " JSON 导出失败")
            }
        }
    }

    private fun createGsonInstance(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .create()

    }

    fun exportDatabase(context: Context, databaseName: String, label: String) {
        val sourceFile = context.getDatabasePath(databaseName)
        val backupFileName = "${databaseName}_${getTimestamp()}.db"

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
                        saveDatabaseToFile(inputStream, outputStream)
                    }
                }
                viewModelScope.launch {
                    updateExportStatus(label, " DB 导出成功")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelScope.launch {
                    updateExportStatus(label, " DB 导出失败")
                }
            }
        }
    }

    private fun saveDatabaseToFile(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (inputStream.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
    }

    fun exportBookmarksToTxt() {
        viewModelScope.launch {
            val bookmarks = getBookmarks()
            val backupFileName = "Bookmark_${getTimestamp()}.txt"

            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, backupFileName)
                put(MediaStore.Downloads.MIME_TYPE, "text/plain")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val contentResolver: ContentResolver = getApplication<Application>().contentResolver
            val uri: Uri? =
                contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            uri?.let { targetUri ->
                try {
                    contentResolver.openOutputStream(targetUri).use { outputStream ->
                        outputStream?.write(bookmarks.toByteArray())
                    }
                    updateExportStatus("书签宝", " TXT 导出成功")
                } catch (e: Exception) {
                    e.printStackTrace()
                    updateExportStatus("书签宝", " TXT 导出失败")
                }
            }
        }
    }

    private fun getTimestamp(): String {
        val now = Date()
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return dateFormat.format(now)
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
            stringBuilder.append("未找到书签")
        }
        return@withContext stringBuilder.toString()
    }

    private suspend fun updateExportStatus(label: String, status: String) {
        exportStatus.emit(exportStatus.value.plus("  $label$status"))
    }
}
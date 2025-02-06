package com.wqz.allinone.act.recovery.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.wqz.allinone.dao.DiaryDao
import com.wqz.allinone.dao.FolderDao
import com.wqz.allinone.dao.LinkDao
import com.wqz.allinone.dao.NoteDao
import com.wqz.allinone.dao.TodoDao
import com.wqz.allinone.database.BookmarkDatabase
import com.wqz.allinone.database.DiaryDatabase
import com.wqz.allinone.database.NoteDatabase
import com.wqz.allinone.database.TodoDatabase
import com.wqz.allinone.entity.Diary
import com.wqz.allinone.entity.DiaryTransfer
import com.wqz.allinone.entity.Folder
import com.wqz.allinone.entity.Link
import com.wqz.allinone.entity.Note
import com.wqz.allinone.entity.NoteTransfer
import com.wqz.allinone.entity.Todo
import com.wqz.allinone.entity.TodoTransfer
import com.wqz.allinone.util.LocalDateTypeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

/**
 * 数据导入视图模型
 * Created by Wu Qizhen on 2025.2.4
 */
class RecoveryViewModel(application: Application) : AndroidViewModel(application) {
    val importStatus = MutableStateFlow(listOf("* 导入结果："))

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

    fun importTodos(importContent: String) {
        viewModelScope.launch {
            try {
                val gson = Gson()
                val todos = gson.fromJson<List<TodoTransfer>>(
                    importContent,
                    object : TypeToken<List<TodoTransfer>>() {}.type
                )
                var result = 0
                todos.forEach {
                    it.title?.let { title ->
                        it.completed?.let { completed ->
                            todoDao.insert(Todo(null, title, completed))
                            result++
                        }
                    }
                }
                updateImportStatus("待办箱", "成功导入 $result 项")
            } catch (e: JsonSyntaxException) {
                Log.e("RecoveryViewModel", e.toString())
                updateImportStatus("待办箱", "导入失败：数据格式不正确")
            } catch (e: Exception) {
                Log.e("RecoveryViewModel", e.toString())
                updateImportStatus("待办箱", "导入失败：未知错误")
            }
        }
    }

    fun importNotes(importContent: String) {
        viewModelScope.launch {
            try {
                val gson = Gson()
                val notes = gson.fromJson<List<NoteTransfer>>(
                    importContent,
                    object : TypeToken<List<NoteTransfer>>() {}.type
                )
                var result = 0
                notes.forEach {
                    it.updateTime?.let { updateTime ->
                        it.title?.let { title ->
                            it.content?.let { content ->
                                it.isLocked?.let { isLocked ->
                                    if (title.isNotEmpty() || content.isNotEmpty()) {
                                        noteDao.insert(
                                            Note(
                                                null,
                                                title,
                                                content,
                                                it.createTime,
                                                updateTime,
                                                isLocked
                                            )
                                        )
                                        result++
                                    }
                                }
                            }
                        }
                    }
                }
                updateImportStatus("随手记", "成功导入 $result 项")
            } catch (e: JsonSyntaxException) {
                Log.e("RecoveryViewModel", e.toString())
                updateImportStatus("随手记", "导入失败：数据格式不正确")
            } catch (e: Exception) {
                Log.e("RecoveryViewModel", e.toString())
                updateImportStatus("随手记", "导入失败：未知错误")
            }
        }
    }

    fun importDiaries(importContent: String) {
        viewModelScope.launch {
            try {
                val gson: Gson = GsonBuilder()
                    .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
                    .create()
                val diaries = gson.fromJson<List<DiaryTransfer>>(
                    importContent,
                    object : TypeToken<List<DiaryTransfer>>() {}.type
                )
                var result = 0
                diaries.forEach {
                    it.content?.let { content ->
                        it.date?.let { date ->
                            it.weather?.let { weather ->
                                it.mood?.let { mood ->
                                    it.isFavorite?.let { isFavorite ->
                                        if (content.isNotEmpty()) {
                                            it.id = 0
                                            diaryDao.insert(
                                                Diary(
                                                    0,
                                                    content,
                                                    date,
                                                    weather,
                                                    mood,
                                                    isFavorite
                                                )
                                            )
                                            result++
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                updateImportStatus("生活书", "成功导入 $result 项")
            } catch (e: JsonSyntaxException) {
                Log.e("RecoveryViewModel", e.toString())
                updateImportStatus("生活书", "导入失败：数据格式不正确")
            } catch (e: Exception) {
                Log.e("RecoveryViewModel", e.toString())
                updateImportStatus("生活书", "导入失败：未知错误")
            }
        }
    }

    suspend fun importBookmarks(importContent: String) = withContext(Dispatchers.IO) {
        val lines = importContent.lines()
        var currentFolderId: Long? = null
        var folderNumber = 0
        var linkNumber = 0

        for (line in lines) {
            if (line.isBlank()) continue

            if (!line.contains("@")) {
                // This is a folder name
                currentFolderId = folderDao.insertFolder(Folder(null, line))
                folderNumber++
            } else {
                // This is a link
                val parts = line.split("@")
                if (parts.size == 2) {
                    val title = parts[0]
                    val url = parts[1]
                    currentFolderId?.let { folderId ->
                        linkDao.insertLink(Link(null, title, url, folderId.toInt()))
                        linkNumber++
                    }
                }
            }
        }

        updateImportStatus("书签宝", "成功导入 $folderNumber 个文件夹和 $linkNumber 个书签")
    }

    private suspend fun updateImportStatus(label: String, status: String) {
        importStatus.emit(importStatus.value.plus("  $label$status"))
    }
}
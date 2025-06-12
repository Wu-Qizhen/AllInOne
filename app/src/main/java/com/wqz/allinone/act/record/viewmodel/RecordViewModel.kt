package com.wqz.allinone.act.record.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.act.record.data.RecordSnapshot
import com.wqz.allinone.database.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 回顾编辑视图模型
 * Created by Wu Qizhen on 2025.5.2
 */
class RecordViewModel(application: Application) : AndroidViewModel(application) {
    private val _recordSnapshots = MutableStateFlow<List<RecordSnapshot>>(emptyList())
    val recordSnapshots = _recordSnapshots.asStateFlow()

    // private val todoDao: TodoDao = TodoDatabase.getDatabase(application).todoDao()
    private val todoDatabase by lazy { TodoDatabase.getDatabase(application) }
    private val todoDao by lazy { todoDatabase.todoDao() }

    init {
        viewModelScope.launch {
            loadRecordSnapshots()
        }
    }

    private suspend fun loadRecordSnapshots() {
        withContext(Dispatchers.IO) {
            val todoNumber = getNextTodoId()
            val completedTodoNumber = getCompletedTodoCount(todoNumber)
            val noteNumber = getNextNoteId()
            val anniversaryNumber = getNextAnniversaryId()
            val diaryNumber = getNextDiaryId()
            val linkNumber = getNextLinkId()

            val mockRecords = listOf(
                RecordSnapshot(
                    type = "溯影棱镜",
                    title = "✨\n\n棱镜溯影，等待折射你的第一束光",
                    count = 0,
                    subtitle = "「点击下方按钮，让未来的自己看到此刻的痕迹」",
                    progress = 0.1f
                ),
                RecordSnapshot(
                    type = "序",
                    title = "🌟\n\n光在折痕处显影，风翻开未写的序章",
                    count = 0,
                    subtitle = "「静止是最缓慢的流动——当灰尘成为年轮，锈迹便长出年轮的纹」",
                    progress = 0.2f
                ),
                RecordSnapshot(
                    type = "待办箱",
                    title = "⚡️\n\n你在待办箱中留下了 $todoNumber 个足迹\n你已征服了 $completedTodoNumber 个待办任务！",
                    count = todoNumber,
                    subtitle = "「从前到今天的每一刻努力，都值得致敬」",
                    progress = 0.35f
                ),
                RecordSnapshot(
                    type = "随手记",
                    title = "✍️\n\n$noteNumber 次灵光乍现，万字思想火花",
                    count = noteNumber,
                    subtitle = "「灵感不会消失，它们只是藏在了你的工作笔记里」",
                    progress = 0.5f
                ),
                RecordSnapshot(
                    type = "纪念日",
                    title = "⏳\n\n你守护了 $anniversaryNumber 个重要时刻",
                    count = anniversaryNumber,
                    subtitle = "「纪念日是生活 Never Forget 的利器」",
                    progress = 0.65f
                ),
                RecordSnapshot(
                    type = "生活书",
                    title = "📕\n\n写满 $diaryNumber 篇人生章节，$diaryNumber 个情绪关键词",
                    count = diaryNumber,
                    subtitle = "「在独处时光里，你与自己的对话最真诚」",
                    progress = 0.8f
                ),
                RecordSnapshot(
                    type = "书签宝",
                    title = "🔗\n\n收藏了 $linkNumber 个平行世界入口",
                    count = linkNumber,
                    subtitle = "「当年的那个链接还记得吗？」",
                    progress = 0.95f
                ),
                RecordSnapshot(
                    type = "终",
                    title = "🔗\n\n数据是冰冷的，但你的每一次记录都在赋予它温度",
                    count = linkNumber,
                    subtitle = "「每一段痕迹，都是时间的礼物」",
                    progress = 1.0f
                )
            )

            _recordSnapshots.value = mockRecords
        }
    }

    private suspend fun getNextTodoId(): Int {
        return withContext(Dispatchers.IO) {
            getCurrentSeq("Todo", "Todo")
        }
    }

    private suspend fun getCompletedTodoCount(allTodoCount: Int): Int {
        return withContext(Dispatchers.IO) {
            allTodoCount - todoDao.getUncompletedTodoCount()
        }
    }

    private suspend fun getNextNoteId(): Int {
        return withContext(Dispatchers.IO) {
            getCurrentSeq("Note", "Note")
        }
    }

    private suspend fun getNextAnniversaryId(): Int {
        return withContext(Dispatchers.IO) {
            getCurrentSeq("Anniversary", "Anniversary")
        }
    }

    private suspend fun getNextDiaryId(): Int {
        return withContext(Dispatchers.IO) {
            getCurrentSeq("Diary", "Diary")
        }
    }

    private suspend fun getNextLinkId(): Int {
        return withContext(Dispatchers.IO) {
            getCurrentSeq("Link", "Bookmark.db")
        }
    }

    private fun getCurrentSeq(tableName: String, dbName: String): Int {
        return try {
            val dbFile = getApplication<Application>().getDatabasePath(dbName)
            SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY).use { db ->
                db.rawQuery(
                    "SELECT seq FROM sqlite_sequence WHERE name = ?",
                    arrayOf(tableName)
                ).use { cursor ->
                    if (cursor.moveToFirst()) cursor.getInt(0) else 0
                }
            }
        } catch (e: Exception) {
            0 // 表不存在或从未插入过数据时返回 0
        }
    }
}

/*
class RecordViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao: TodoDao = TodoDatabase.getDatabase(application).todoDao()

    fun getRecordSnapshots(): List<RecordSnapshot> {
        val todoNumber = getNextTodoId()
        val completedTodoNumber = getCompletedTodoCount(todoNumber)
        val noteNumber = getNextNoteId()
        val anniversaryNumber = getNextAnniversaryId()
        val diaryNumber = getNextDiaryId()
        val linkNumber = getNextLinkId()

        val mockRecords = mutableListOf(
            RecordSnapshot(
                type = "溯影棱镜",
                title = "✨\n\n棱镜溯影，等待折射你的第一束光",
                count = 0,
                subtitle = "「点击下方按钮，让未来的自己看到此刻的痕迹」",
                progress = 0.1f
            ),
            RecordSnapshot(
                type = "序",
                title = "🌟\n\n光在折痕处显影，风翻开未写的序章",
                count = 0,
                subtitle = "「静止是最缓慢的流动——当灰尘成为年轮，锈迹便长出年轮的纹」",
                progress = 0.2f
            ),
            RecordSnapshot(
                type = "待办箱",
                title = "⚡️\n\n你在待办箱中留下了 $todoNumber 个足迹\n你已征服了 $completedTodoNumber 个待办任务！",
                count = todoNumber,
                subtitle = "「从前到今天的每一刻努力，都值得致敬」",
                progress = 0.35f
            ),
            RecordSnapshot(
                type = "随手记",
                title = "✍️\n\n$noteNumber 次灵光乍现，万字思想火花",
                count = noteNumber,
                subtitle = "「灵感不会消失，它们只是藏在了你的工作笔记里」",
                progress = 0.5f
            ),
            RecordSnapshot(
                type = "纪念日",
                title = "⏳\n\n你守护了 $anniversaryNumber 个重要时刻",
                count = anniversaryNumber,
                subtitle = "「纪念日是生活 Never Forget 的利器」",
                progress = 0.65f
            ),
            RecordSnapshot(
                type = "生活书",
                title = "📕\n\n写满 $diaryNumber 篇人生章节，$diaryNumber 个情绪关键词",
                count = diaryNumber,
                subtitle = "「在独处时光里，你与自己的对话最真诚」",
                progress = 0.8f
            ),
            RecordSnapshot(
                type = "书签宝",
                title = "🔗\n\n收藏了 $linkNumber 个平行世界入口",
                count = linkNumber,
                subtitle = "「当年的那个链接还记得吗？」",
                progress = 0.95f
            ),
            RecordSnapshot(
                type = "终",
                title = "🔗\n\n数据是冰冷的，但你的每一次记录都在赋予它温度",
                count = linkNumber,
                subtitle = "「每一段痕迹，都是时间的礼物」",
                progress = 1.0f
            )
        )

        return mockRecords
    }

    private fun getNextTodoId(): Int {
        return getCurrentSeq("Todo", "Todo")
    }

    private fun getCompletedTodoCount(allTodoCount: Int): Int {
        return allTodoCount - todoDao.getUncompletedTodoCount()
    }

    private fun getNextNoteId(): Int {
        return getCurrentSeq("Note", "Note")
    }

    private fun getNextAnniversaryId(): Int {
        return getCurrentSeq("Anniversary", "Anniversary")
    }

    private fun getNextDiaryId(): Int {
        return getCurrentSeq("Diary", "Diary")
    }

    private fun getNextLinkId(): Int {
        return getCurrentSeq("Link", "Bookmark.db")
    }

    private fun getCurrentSeq(tableName: String, dbName: String): Int {
        return try {
            val dbFile = getApplication<Application>().getDatabasePath(dbName)
            SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY).use { db ->
                db.rawQuery(
                    "SELECT seq FROM sqlite_sequence WHERE name = ?",
                    arrayOf(tableName)
                ).use { cursor ->
                    if (cursor.moveToFirst()) cursor.getInt(0) else 0
                }
            }
        } catch (e: Exception) {
            0 // 表不存在或从未插入过数据时返回 0
        }
    }
}*/

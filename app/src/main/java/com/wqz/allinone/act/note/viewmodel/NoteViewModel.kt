package com.wqz.allinone.act.note.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.wqz.allinone.dao.NoteDao
import com.wqz.allinone.database.NoteDatabase
import com.wqz.allinone.entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*class NoteEditViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao: NoteDao

    init {
        val noteDatabase = Room.databaseBuilder(
            application,
            NoteDatabase::class.java,
            "Note"
        ).build()
        noteDao = noteDatabase.noteDao()
    }

    fun saveNote(noteId: Int, title: String, content: String) {
        viewModelScope.launch {
            val formattedUpdateTime = getDateTime()
            val note = Note(
                id = if (noteId != -1) noteId else null,
                title = title,
                content = content,
                createTime = if (noteId == -1) formattedUpdateTime else null,
                updateTime = formattedUpdateTime
            )

            if (noteId == -1) {
                // 插入新笔记
                noteDao.insert(note)
            } else {
                // 更新现有笔记
                noteDao.update(note)
            }
        }
    }

    fun getNote(noteId: Int): Note {
        return noteDao.getById(noteId)!!
        // .flowOn(Dispatchers.IO)
    }

    fun getDateTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")
        return currentTime.format(formatter)
    }
}*/

/**
 * 笔记编辑视图模型
 * Created by Wu Qizhen on 2024.7.1
 */
class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao: NoteDao
    val notes: LiveData<List<Note>>

    init {
        val noteDatabase = NoteDatabase.getInstance(application)
        /*Room.databaseBuilder(
        application,
        NoteDatabase::class.java,
        "Note"
    ).build()*/
        noteDao = noteDatabase.noteDao()
        notes = noteDao.getAllAsFlow().asLiveData()
    }

    suspend fun saveNote(note: Note): Note {
        withContext(Dispatchers.IO) {
            note.updateTime = getDateTime()
            if (note.id == -1) {
                note.id = null
                note.id = noteDao.insert(note).toInt()
            } else {
                noteDao.update(note)
            }
        }
        return note
    }

    suspend fun deleteNote(id: Int?) {
        withContext(Dispatchers.IO) {
            if (id != null) {
                noteDao.deleteById(id)
            }
        }
    }

    /*suspend fun getNote(noteId: Int): Note? {
        return withContext(Dispatchers.IO) {
            noteDao.getById(noteId)
        }
    }*/

    fun getDateTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")
        return currentTime.format(formatter)
    }
}
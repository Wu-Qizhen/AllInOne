package com.wqz.allinone.act.note.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.dao.NoteDao
import com.wqz.allinone.database.NoteDatabase
import com.wqz.allinone.entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 笔记编辑视图模型
 * Created by Wu Qizhen on 2024.7.1
 */
class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao: NoteDao
    val notes: LiveData<List<Note>>

    init {
        val noteDatabase = NoteDatabase.getDatabase(application)
        noteDao = noteDatabase.noteDao()
        notes = noteDao.getAllAsLiveData()
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

    suspend fun getNote(id: Int): Note {
        return withContext(Dispatchers.IO) {
            noteDao.getById(id)
        }
    }

    fun deleteNoteWithDelay(id: Int?) = viewModelScope.launch {
        delay(1000)
        if (id != null) {
            noteDao.deleteById(id)
        }
    }

    // 锁定状态转换
    fun updateLockStatus(noteId: Int, isLocked: Boolean) = viewModelScope.launch {
        noteDao.updateLockStatus(noteId, isLocked)
    }

    fun getDateTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")
        return currentTime.format(formatter)
    }
}
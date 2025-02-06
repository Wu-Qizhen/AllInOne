package com.wqz.allinone.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wqz.allinone.entity.Note

/**
 * 笔记数据访问对象
 * Created by Wu Qizhen on 2024.6.30
 */
@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notes: List<Note>): List<Long>

    @Query("SELECT * FROM Note ORDER BY update_time DESC")
    fun getAllAsLiveData(): LiveData<List<Note>>

    @Query("SELECT * FROM Note")
    fun getAll(): List<Note>

    @Query("SELECT * FROM Note WHERE id = :noteId")
    fun getById(noteId: Int): Note

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM Note WHERE id = :noteId")
    suspend fun deleteById(noteId: Int)

    @Query("UPDATE Note SET is_locked = :isLocked WHERE id = :noteId")
    suspend fun updateLockStatus(noteId: Int, isLocked: Boolean)
}
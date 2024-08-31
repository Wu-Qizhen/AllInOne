package com.wqz.allinone.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wqz.allinone.entity.Note

/**
 * 笔记数据访问对象
 * Created by Wu Qizhen on 2024.6.30
 */
@Dao
interface NoteDao {
    // 插入单个笔记
    @Insert
    suspend fun insert(note: Note): Long // 返回插入记录的 ID

    // 插入多个笔记
    @Insert
    suspend fun insert(vararg notes: Note): List<Long> // 返回插入记录的 ID 列表

    // 查询所有笔记
    @Query("SELECT * FROM Note ORDER BY update_time DESC")
    fun getAll(): LiveData<List<Note>>

    // 根据 ID 查询笔记
    @Query("SELECT * FROM Note WHERE id = :noteId")
    fun getById(noteId: Int): LiveData<Note>

    // 更新笔记
    @Update
    suspend fun update(note: Note)

    // 删除笔记
    @Delete
    suspend fun delete(note: Note)

    // 根据 ID 删除笔记
    @Query("DELETE FROM Note WHERE id = :noteId")
    suspend fun deleteById(noteId: Int)
}
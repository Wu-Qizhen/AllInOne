package com.wqz.allinone.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wqz.allinone.entity.Diary
import java.time.LocalDate

/**
 * 日记数据访问对象
 * Created by Wu Qizhen on 2024.10.3
 */
@Dao
interface DiaryDao {
    @Query("SELECT * FROM Diary ORDER BY date DESC")
    fun getAll(): List<Diary>

    @Query("SELECT * FROM Diary WHERE date BETWEEN :startOfMonth AND :endOfMonth ORDER BY date DESC")
    fun getByMonth(startOfMonth: LocalDate, endOfMonth: LocalDate): LiveData<List<Diary>>

    @Query("SELECT * FROM Diary WHERE id = :id")
    suspend fun getById(id: Long): Diary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: Diary): Long

    @Update
    suspend fun update(entry: Diary)

    // 根据 ID 删除
    @Query("DELETE FROM Diary WHERE id = :diaryId")
    suspend fun deleteById(diaryId: Long)
}
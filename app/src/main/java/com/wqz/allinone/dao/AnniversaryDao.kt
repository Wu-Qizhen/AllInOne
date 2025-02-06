package com.wqz.allinone.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wqz.allinone.entity.Anniversary
import java.time.LocalDate

/**
 * 纪念日数据访问对象
 * Created by Wu Qizhen on 2024.8.20
 */
@Dao
interface AnniversaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anniversary: Anniversary): Long

    @Update
    suspend fun update(anniversary: Anniversary)

    @Query("DELETE FROM Anniversary WHERE id = :anniversaryId")
    suspend fun deleteById(anniversaryId: Int)

    @Query("SELECT * FROM Anniversary WHERE id = :anniversaryId")
    fun getById(anniversaryId: Int): Anniversary

    @Query("SELECT * FROM Anniversary")
    fun getAll(): LiveData<List<Anniversary>>

    // @TypeConverters(TypeConverters::class)
    @Query("SELECT * FROM anniversary ORDER BY date ASC")
    fun getAllSortedByDate(): LiveData<List<Anniversary>>

    @Query("SELECT * FROM Anniversary WHERE date = :anniversaryDate")
    fun getByDate(anniversaryDate: LocalDate): LiveData<List<Anniversary>>
}
package com.wqz.allinone.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
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
    // 插入新的纪念日记录
    @Insert
    suspend fun insert(anniversary: Anniversary): Long

    // 更新纪念日记录
    @Update
    suspend fun update(anniversary: Anniversary)

    // 根据 ID 删除纪念日记录
    @Query("DELETE FROM Anniversary WHERE id = :anniversaryId")
    suspend fun deleteById(anniversaryId: Int)

    // 根据 ID 查询纪念日记录
    @Query("SELECT * FROM Anniversary WHERE id = :anniversaryId")
    fun getById(anniversaryId: Int): Anniversary

    // 获取所有纪念日记录
    @Query("SELECT * FROM Anniversary")
    fun getAll(): LiveData<List<Anniversary>>

    // 获取所有纪念日记录，根据日期由小到大排序
    // @TypeConverters(TypeConverters::class)
    @Query("SELECT * FROM anniversary ORDER BY date ASC")
    fun getAllSortedByDate(): LiveData<List<Anniversary>>

    // 根据日期查询纪念日记录
    @Query("SELECT * FROM Anniversary WHERE date = :anniversaryDate")
    fun getByDate(anniversaryDate: LocalDate): LiveData<List<Anniversary>>
}
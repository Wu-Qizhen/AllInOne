package com.wqz.allinone.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.wqz.allinone.entity.Todo

/**
 * 待办数据访问对象
 * Created by Wu Qizhen on 2024.10.1
 */
@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todos: List<Todo>): List<Long>

    @Query("INSERT INTO Todo (title, completed) VALUES (:title, :completed)")
    suspend fun insert(title: String, completed: Boolean = false)

    @Query("SELECT * FROM Todo")
    @Transaction
    fun getAllAsLiveData(): LiveData<List<Todo>>

    @Query("SELECT * FROM Todo")
    @Transaction
    fun getAll(): List<Todo>

    @Query("SELECT * FROM Todo WHERE completed = 1")
    @Transaction
    fun getCompletedTodos(): LiveData<List<Todo>>

    @Query("SELECT COUNT(*) FROM Todo WHERE completed = 0")
    @Transaction
    fun getUncompletedTodoCount(): Int

    @Query("SELECT * FROM Todo WHERE completed = 0")
    @Transaction
    fun getUncompletedTodos(): LiveData<List<Todo>>

    @Update
    suspend fun update(todo: Todo)

    @Query("UPDATE Todo SET completed = :completed WHERE id = :id")
    suspend fun update(id: Int, completed: Boolean)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("DELETE FROM Todo WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM Todo WHERE completed = 1")
    suspend fun deleteCompletedTodos()

    @Query("SELECT * FROM Todo WHERE id = :id")
    @Transaction
    fun getById(id: Int): Todo
}
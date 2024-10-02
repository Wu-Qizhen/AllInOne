package com.wqz.allinone.act.todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.dao.TodoDao
import com.wqz.allinone.database.TodoDatabase
import com.wqz.allinone.entity.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 待办编辑视图模型
 * Created by Wu Qizhen on 2024.10.1
 */
class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao: TodoDao
    val unCompletedTodos: LiveData<List<Todo>>
    val completedTodos: LiveData<List<Todo>>

    init {
        val todoDatabase = TodoDatabase.getDatabase(application)
        todoDao = todoDatabase.todoDao()
        unCompletedTodos = todoDao.getUncompletedTodos()
        completedTodos = todoDao.getCompletedTodos()
    }

    fun insertTodo(todo: Todo) = viewModelScope.launch {
        todo.id = todoDao.insert(todo).toInt()
    }

    suspend fun updateTodo(todo: Todo) {
        // delay(1000)
        withContext(Dispatchers.IO) {
            todoDao.update(todo)
        }
    }

    fun deleteTodo(id: Int?) = viewModelScope.launch {
        if (id != null) {
            todoDao.deleteById(id)
        }
    }

    fun clearCompleted() = viewModelScope.launch {
        todoDao.deleteCompletedTodos()
    }
}
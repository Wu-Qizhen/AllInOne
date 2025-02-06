package com.wqz.allinone.act.anniversary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.dao.AnniversaryDao
import com.wqz.allinone.database.AnniversaryDatabase
import com.wqz.allinone.entity.Anniversary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 纪念日视图模型
 * Created by Wu Qizhen on 2024.8.20
 */
class AnniversaryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: AnniversaryDao
    val anniversaries: LiveData<List<Anniversary>>

    init {
        val database = AnniversaryDatabase.getDatabase(application)
        dao = database.anniversaryDao()
        anniversaries = dao.getAllSortedByDate()
    }

    fun insertAnniversary(anniversary: Anniversary) = viewModelScope.launch {
        anniversary.id = dao.insert(anniversary).toInt()
    }

    fun deleteAnniversary(id: Int?) = viewModelScope.launch {
        if (id != null) {
            dao.deleteById(id)
        }
    }

    suspend fun getAnniversary(id: Int): Anniversary {
        val anniversary: Anniversary
        withContext(Dispatchers.IO) {
            anniversary = dao.getById(id)
        }
        return anniversary
    }
}
package com.wqz.allinone.act.anniversary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.wqz.allinone.dao.AnniversaryDao
import com.wqz.allinone.database.AnniversaryDatabase
import com.wqz.allinone.entity.Anniversary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 视图模型
 * Created by Wu Qizhen on 2024.8.20
 */
class AnniversaryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: AnniversaryDao
    val anniversaries: LiveData<List<Anniversary>>

    init {
        val database = AnniversaryDatabase.getInstance(application)
        dao = database.anniversaryDao()
        anniversaries = dao.getAll().asLiveData()
    }

    suspend fun insert(anniversary: Anniversary) {
        withContext(Dispatchers.IO) {
            anniversary.id = dao.insert(anniversary).toInt()
        }
    }
}
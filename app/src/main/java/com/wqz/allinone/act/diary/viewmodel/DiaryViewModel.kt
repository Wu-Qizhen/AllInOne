package com.wqz.allinone.act.diary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.act.diary.data.Option
import com.wqz.allinone.dao.DiaryDao
import com.wqz.allinone.database.DiaryDatabase
import com.wqz.allinone.entity.Diary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.YearMonth

/**
 * 日记视图模型
 * Created by Wu Qizhen on 2024.10.3
 */
class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: DiaryDao
    private val _selectedWeather = MutableStateFlow<Option?>(null)
    private val _selectedMood = MutableStateFlow<Option?>(null)
    val selectedWeather: StateFlow<Option?> = _selectedWeather.asStateFlow()
    val selectedMood: StateFlow<Option?> = _selectedMood.asStateFlow()

    // private var currentMonth: YearMonth = YearMonth.now()
    private var currentDate: LocalDate = LocalDate.now()
    private var _diaries: LiveData<List<Diary>> = MutableLiveData()
    val diaries: LiveData<List<Diary>> get() = _diaries

    init {
        val database = DiaryDatabase.getInstance(application)
        dao = database.diaryDao()
        updateDiariesForCurrentMonth()
    }

    fun setSelectedWeather(option: Option?) {
        _selectedWeather.value = option
    }

    fun setSelectedMood(option: Option?) {
        _selectedMood.value = option
    }

    fun insertDiary(diary: Diary) = viewModelScope.launch {
        dao.insert(diary)
        updateDiariesForCurrentMonth()
    }

    fun updateDiary(diary: Diary) = viewModelScope.launch {
        dao.update(diary)
        updateDiariesForCurrentMonth()
    }

    /*fun deleteDiary(diary: Diary) = viewModelScope.launch {
        dao.delete(diary)
    }*/

    fun deleteDiaryWithDelay(id: Long?) = viewModelScope.launch {
        delay(1000)
        if (id != null) {
            dao.deleteById(id)
            updateDiariesForCurrentMonth()
        }
    }

    suspend fun getDiary(id: Long): Diary {
        return withContext(Dispatchers.IO) {
            dao.getById(id)!!
        }
    }

    private fun updateDiariesForCurrentMonth() {
        val yearMonth = YearMonth.from(currentDate)
        val startOfMonth = yearMonth.atDay(1)
        val endOfMonth = yearMonth.atEndOfMonth()
        _diaries = dao.getByMonth(startOfMonth, endOfMonth)
    }

    fun setDate(date: LocalDate) {
        currentDate = date
        updateDiariesForCurrentMonth()
    }

    /*private fun updateDiariesForCurrentMonth() {
        val startOfMonth = currentMonth.atDay(1)
        val endOfMonth = currentMonth.atEndOfMonth()
        _diaries = dao.getByMonth(startOfMonth, endOfMonth)
    }

    fun setMonth(year: Int, month: Int) {
        currentMonth = YearMonth.of(year, month)
        updateDiariesForCurrentMonth()
    }*/

    /*private fun getStartOfMonth(calendar: Calendar): Date {
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private fun getEndOfMonth(calendar: Calendar): Date {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }*/
}
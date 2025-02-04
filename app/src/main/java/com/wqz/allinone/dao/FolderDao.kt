package com.wqz.allinone.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wqz.allinone.entity.Folder

/**
 * 文件夹数据访问对象
 * Created by Wu Qizhen on 2024.11.3
 */
@Dao
interface FolderDao {
    @Query("SELECT * FROM Folder")
    fun getAllFolders(): LiveData<List<Folder>>

    @Query("SELECT * FROM Folder")
    fun getAllFoldersForExport(): List<Folder>

    @Query("SELECT * FROM Folder WHERE id = :folderId")
    fun getFolder(folderId: Int): Folder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFolder(folder: Folder): Long

    @Update
    fun updateFolder(folder: Folder)

    @Delete
    fun deleteFolder(folder: Folder)

    @Query("DELETE FROM Folder WHERE id = :folderId")
    fun deleteFolderById(folderId: Int)
}
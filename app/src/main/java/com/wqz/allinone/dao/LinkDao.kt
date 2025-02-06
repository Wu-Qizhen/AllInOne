package com.wqz.allinone.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wqz.allinone.entity.Link

/**
 * 链接数据访问对象
 * Created by Wu Qizhen on 2024.11.3
 */
@Dao
interface LinkDao {
    @Query("SELECT * FROM Link")
    fun getAllLinks(): LiveData<List<Link>>

    @Query("SELECT * FROM Link WHERE folder = :folderId")
    fun getLinksForFolder(folderId: Int): List<Link>

    @Query("SELECT * FROM Link WHERE id = :id")
    fun getLink(id: Int): Link?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLink(link: Link)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLinks(links: List<Link>)

    @Update
    fun updateLink(link: Link)

    @Delete
    fun deleteLink(link: Link)

    @Query("DELETE FROM Link WHERE folder = :folderId")
    fun deleteLinks(folderId: Int)

    @Query("SELECT COUNT(*) FROM Link WHERE folder = :folderId")
    fun getLinkCount(folderId: Int): Int
}
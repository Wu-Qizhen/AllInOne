package com.wqz.allinone.act.bookmark.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.wqz.allinone.dao.FolderDao
import com.wqz.allinone.dao.LinkDao
import com.wqz.allinone.database.BookmarkDatabase
import com.wqz.allinone.entity.Folder
import com.wqz.allinone.entity.Link
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 书签视图模型
 * Created by Wu Qizhen on 2024.11.3
 */
class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    private val folderDao: FolderDao
    private val linkDao: LinkDao
    val folders: LiveData<List<Folder>>
    val links: LiveData<List<Link>>

    init {
        val bookmarkDatabase = BookmarkDatabase.getDatabase(application)
        folderDao = bookmarkDatabase.folderDao()
        linkDao = bookmarkDatabase.linkDao()
        folders = folderDao.getAllFolders()
        links = linkDao.getAllLinks()
    }

    /*fun insertFolder(folder: Folder) = viewModelScope.launch {
        folderDao.insertFolder(folder)
    }*/

    fun insertFolder(folder: Folder) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                folderDao.insertFolder(folder)
            }
        }
    }

    fun updateFolder(folder: Folder) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                folderDao.updateFolder(folder)
            }
        }
    }

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                folderDao.deleteFolder(folder)
            }
        }
    }

    suspend fun getFolder(id: Int): Folder {
        return withContext(Dispatchers.IO) {
            folderDao.getFolder(id)!!
        }
    }

    suspend fun getLinkCount(folderId: Int): Int {
        return withContext(Dispatchers.IO) {
            linkDao.getLinkCount(folderId)
        }
    }

    fun insertLink(link: Link) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                linkDao.insertLink(link)
            }
        }
    }

    fun insertLinks(links: List<Link>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                linkDao.insertLinks(links)
            }
        }
    }

    fun updateLink(link: Link) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                linkDao.updateLink(link)
            }
        }
    }

    fun deleteLink(link: Link) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                linkDao.deleteLink(link)
            }
        }
    }

    fun deleteLinks(folderId: Int?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (folderId != null) {
                    linkDao.deleteLinks(folderId)
                }
            }
        }
    }

    suspend fun getLink(id: Int): Link {
        return withContext(Dispatchers.IO) {
            linkDao.getLink(id)!!
        }
    }
}
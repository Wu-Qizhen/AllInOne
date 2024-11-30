package com.wqz.allinone.act.backup.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.wqz.allinone.R
import com.wqz.allinone.dao.FolderDao
import com.wqz.allinone.dao.LinkDao
import com.wqz.allinone.database.BookmarkDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 数据导出视图模型
 * Created by Wu Qizhen on 2024.11.29
 */
class BackupViewModel(application: Application) : AndroidViewModel(application) {
    private val folderDao: FolderDao
    private val linkDao: LinkDao
    /*val folders: LiveData<List<Folder>>
    val links: LiveData<List<Link>>*/

    init {
        val bookmarkDatabase = BookmarkDatabase.getDatabase(application)
        folderDao = bookmarkDatabase.folderDao()
        linkDao = bookmarkDatabase.linkDao()
        /*folders = folderDao.getAllFolders()
        links = linkDao.getAllLinks()*/
    }

    suspend fun exportBookmarks() {
        val bookmarks = getBookmarks()
        val backupFileName = "Bookmarks.txt"

        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, backupFileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/plain")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val contentResolver: ContentResolver = getApplication<Application>().contentResolver
        val uri: Uri? = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        uri?.let { targetUri ->
            try {
                contentResolver.openOutputStream(targetUri).use { outputStream ->
                    outputStream?.write(bookmarks.toByteArray())
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        getApplication(),
                        R.string.backup_bookmark_success,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        getApplication(),
                        R.string.backup_bookmark_failed,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private suspend fun getBookmarks(): String = withContext(Dispatchers.IO) {
        val stringBuilder = StringBuilder()
        // 获取所有文件夹
        val allFolders = folderDao.getAllFoldersForExport()
        if (allFolders.isNotEmpty()) {
            for (folder in allFolders) {
                stringBuilder.append(folder.name).append("\n")
                // 获取当前文件夹下的所有链接
                val linksInFolder = linkDao.getLinksForFolder(folder.id!!)
                for (link in linksInFolder) {
                    stringBuilder.append("${link.title}@${link.url}\n")
                }
                // 在每个文件夹之后添加两个换行符
                stringBuilder.append("\n")
            }
        } else {
            stringBuilder.append("No bookmarks found.")
        }
        return@withContext stringBuilder.toString()
    }

    /*fun exportDatabase(databaseName: String, activity: BackupActivity) {
        if (ContextCompat.checkSelfPermission(
                getApplication<Application>().applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_STORAGE
            )
        } else {
            // Permission has already been granted
            doBackup(databaseName)
        }
    }

    private fun doBackup(databaseName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            val sourceDatabasePath = context.getDatabasePath(databaseName).absolutePath
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            if (downloadsDir != null && downloadsDir.exists()) {
                val destinationDatabasePath = File(downloadsDir, databaseName)
                try {
                    copyFile(File(sourceDatabasePath), destinationDatabasePath)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun copyFile(source: File, destination: File) {
        FileInputStream(source).use { input ->
            FileOutputStream(destination).use { output ->
                val buffer = ByteArray(4 * 1024) // Buffer size
                while (input.read(buffer) != -1) {
                    output.write(buffer)
                }
                output.flush()
            }
        }
    }

    companion object {
        const val REQUEST_CODE_WRITE_STORAGE = 100
    }*/
}

/*class BackupViewModel(private val diaryDao: DiaryDao) : ViewModel() {
    // 状态管理
    private val _exportResult = MutableLiveData<Result<Unit>>()
    val exportResult: LiveData<Result<Unit>> = _exportResult

    fun exportJson(context: Context) {
        viewModelScope.launch {
            val result = runCatching {
                val filename = "backup_${
                    SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(Date())
                }.json"
                val downloadsDirectory =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsDirectory, filename)
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )

                val json = Json.encodeToString(diaryDao.getAll().toSet())
                BufferedOutputStream(context.contentResolver.openOutputStream(uri)).use { out: BufferedOutputStream ->
                    out.write(json.toByteArray())
                }
            }
            _exportResult.postValue(result)
        }
    }
}*/

/*class BackupViewModel(application: Application) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private val context: Context = application.applicationContext

    fun exportDatabase(databaseName: String) {
        viewModelScope.launch {
            val databaseFile = context.getDatabasePath(databaseName)
            if (databaseFile.exists()) {
                val backupFile = File(context.getExternalFilesDir(null), "$databaseName.backup")
                copyFile(databaseFile, backupFile)
                shareFile(backupFile)
            } else {
                // Handle the case where the database file does not exist
            }
        }
    }

    private suspend fun copyFile(source: File, destination: File) {
        val inputStream: InputStream = source.inputStream()
        val outputStream: FileOutputStream =
            withContext(Dispatchers.IO) {
                FileOutputStream(destination)
            }

        inputStream.use { input ->
            outputStream.use { output ->
                val buffer = ByteArray(1024)
                var length: Int
                while (input.read(buffer).also { length = it } > 0) {
                    output.write(buffer, 0, length)
                }
            }
        }
    }

    private fun shareFile(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileProvider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/octet-stream"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        context.startActivity(intent)
    }
}*/
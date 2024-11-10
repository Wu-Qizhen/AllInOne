package com.wqz.allinone.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 文件夹实体类
 * Created by Wu Qizhen on 2024.11.3
 */
@Entity(tableName = "Folder")
data class Folder(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val name: String
)
package com.wqz.allinone.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 链接实体类
 * Created by Wu Qizhen on 2024.11.3
 */
@Entity(
    tableName = "Link"
    /*foreignKeys = [
        ForeignKey(
            entity = Folder::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("folder"),
            onDelete = ForeignKey.CASCADE
        )
    ]*/
)
data class Link(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val title: String,
    val url: String,
    val folder: Int
)
package com.example.halifaxtransit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// 本地数据库中的“路线”表
@Entity(tableName = "routes")
data class Route(
    @PrimaryKey val id: Int,           // 1, 2, 3 …
    val name: String,                  // "1 Spring Garden"
    val isFavorite: Boolean = false    // 用户是否选中
)

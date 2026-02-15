package com.example.halifaxtransit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {

    // 所有路线（Phase 3 用这个在 Routes 页面显示）
    @Query("SELECT * FROM routes ORDER BY id")
    fun getAllRoutes(): Flow<List<Route>>

    // 统计表里有多少条记录，用来判断需不需要 seed
    @Query("SELECT COUNT(*) FROM routes")
    suspend fun getCount(): Int

    // 初次 seed 的插入
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<Route>)

    // 点选/取消收藏时更新 isFavorite
    @Update
    suspend fun updateRoute(route: Route)
}

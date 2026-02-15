package com.example.halifaxtransit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// App 的本地数据库，只包含一张 routes 表
@Database(
    entities = [Route::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun routeDao(): RouteDao

    companion object {
        //@Volatile：保证多线程访问这个变量时，读到的值是“最新的”，避免线程缓存旧值。
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 拿到单例数据库实例
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "halifax_transit_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }

        // 第一次使用时，如果 routes 表为空，就插入默认路线
        suspend fun seedRoutesIfNeeded(routeDao: RouteDao) {
            val count = routeDao.getCount()
            if (count == 0) {
                routeDao.insertRoutes(defaultHalifaxRoutes())
            }
        }
    }
}

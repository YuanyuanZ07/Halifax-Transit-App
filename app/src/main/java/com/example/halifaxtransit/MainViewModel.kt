package com.example.halifaxtransit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.halifaxtransit.data.AppDatabase
import com.example.halifaxtransit.data.Route
import com.google.transit.realtime.GtfsRealtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // ---------------- GTFS 实时公交数据 ----------------
    private val _gtfs = MutableStateFlow<GtfsRealtime.FeedMessage?>(null)
    val gtfs: StateFlow<GtfsRealtime.FeedMessage?> = _gtfs.asStateFlow()

    fun loadGtfsBusPositions() {
        viewModelScope.launch {
            try {
                val url = URL("https://gtfs.halifax.ca/realtime/Vehicle/VehiclePositions.pb")

                val feed = withContext(Dispatchers.IO) {
                    GtfsRealtime.FeedMessage.parseFrom(url.openStream())
                }

                _gtfs.value = feed
            } catch (e: Exception) {
                Log.e("GTFS", e.toString())
            }
        }
    }

    // ---------------- Room 本地数据库：所有路线 + 收藏路线 ----------------

    private val routeDao = AppDatabase.getDatabase(application).routeDao()

    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> = _routes.asStateFlow()

    // 被用户收藏的路线 id（Int）
    private val _selectedRouteIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedRouteIds: StateFlow<Set<Int>> = _selectedRouteIds.asStateFlow()

    init {
        viewModelScope.launch {
            AppDatabase.seedRoutesIfNeeded(routeDao)

            // 监听数据库
            routeDao.getAllRoutes().collect { list ->
                _routes.value = list

                _selectedRouteIds.value = list
                    .filter { it.isFavorite }
                    .map { it.id }
                    .toSet()
            }
        }
    }

    /** 用户点击某一条路线，切换收藏状态 */
    fun toggleRouteSelection(routeId: Int) {
        viewModelScope.launch {
            val route = _routes.value.firstOrNull { it.id == routeId } ?: return@launch

            val updated = route.copy(isFavorite = !route.isFavorite)
            routeDao.updateRoute(updated)
        }
    }
}

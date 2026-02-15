package com.example.halifaxtransit.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.halifaxtransit.data.AppDatabase
import com.example.halifaxtransit.data.Route
import com.example.halifaxtransit.data.RouteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutesViewModel(application: Application) : AndroidViewModel(application) {

    private val routeDao = AppDatabase.getDatabase(application).routeDao()
    private val repository = RouteRepository(routeDao)

    // 所有路线
    val routes = repository.getAllRoutes()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        // 第一次初始化时自动 seed（仅当 routes 表为空）
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.seedRoutesIfNeeded(routeDao)
        }
    }

    fun toggleFavorite(route: Route) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRoute(
                route.copy(isFavorite = !route.isFavorite)
            )
        }
    }
}

package com.example.halifaxtransit.data

import kotlinx.coroutines.flow.Flow

class RouteRepository(private val routeDao: RouteDao) {

    fun getAllRoutes(): Flow<List<Route>> = routeDao.getAllRoutes()

    suspend fun insertRoutes(routes: List<Route>) {
        routeDao.insertRoutes(routes)
    }

    suspend fun updateRoute(route: Route) {
        routeDao.updateRoute(route)
    }
}

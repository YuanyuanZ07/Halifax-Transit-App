package com.example.halifaxtransit.data

// 这里定义一组“默认路线”，第一次运行时灌进数据库
fun defaultHalifaxRoutes(): List<Route> = listOf(
        Route(1, "1 Spring Garden"),
        Route(2, "2 Fairview"),
        Route(3, "3 Crosstown"),
        Route(4, "4 Universities"),
        Route(5, "5 Mumford"),
        Route(6, "6 Portland Hills"),
        Route(7, "7 Robie"),
        Route(8, "8 Sackville"),
        Route(9, "9 Barrington")
        // 想加更多，可继续往下写 Route(id, "名字")
    )

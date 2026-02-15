package com.example.halifaxtransit.ui.theme

import androidx.compose.ui.graphics.Color

// Coolors5 个品牌色
val BrandBrown  = Color(0xFF502419) // 深棕
val BrandGreen  = Color(0xFF7EA172) // 绿色
val BrandOlive  = Color(0xFFC7CB85) // 橄榄黄绿
val BrandOrange = Color(0xFFE7A977) // 橙杏色
val BrandPeach  = Color(0xFFEBBE9B) // 浅杏色

// ---------------- Light theme ----------------
val primaryLight = BrandGreen
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = BrandOlive
val onPrimaryContainerLight = BrandBrown

val secondaryLight = BrandOrange
val onSecondaryLight = BrandBrown
val secondaryContainerLight = BrandPeach
val onSecondaryContainerLight = BrandBrown

val tertiaryLight = BrandBrown
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFF7A3A27)
val onTertiaryContainerLight = Color(0xFFFFFFFF)

val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)

// 用偏杏色的背景 / 表面
val backgroundLight = Color(0xFFFFF1E6)           // 比 BrandPeach 再浅一点
val onBackgroundLight = BrandBrown
val surfaceLight = backgroundLight
val onSurfaceLight = BrandBrown

val surfaceVariantLight = BrandOlive
val onSurfaceVariantLight = BrandBrown
val outlineLight = BrandBrown.copy(alpha = 0.4f)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = BrandBrown
val inverseOnSurfaceLight = Color(0xFFFFF1E6)
val inversePrimaryLight = BrandOrange

// ---------------- Dark theme ----------------
val primaryDark = BrandGreen
val onPrimaryDark = BrandBrown
val primaryContainerDark = BrandBrown
val onPrimaryContainerDark = Color(0xFFFFFFFF)

val secondaryDark = BrandOrange
val onSecondaryDark = BrandBrown
val secondaryContainerDark = BrandBrown
val onSecondaryContainerDark = Color(0xFFFFFFFF)

val tertiaryDark = BrandPeach
val onTertiaryDark = BrandBrown
val tertiaryContainerDark = BrandBrown
val onTertiaryContainerDark = Color(0xFFFFFFFF)

val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)

val backgroundDark = Color(0xFF201A17)
val onBackgroundDark = Color(0xFFEADFDA)
val surfaceDark = backgroundDark
val onSurfaceDark = onBackgroundDark

val surfaceVariantDark = Color(0xFF51453D)
val onSurfaceVariantDark = Color(0xFFE4D6CC)
val outlineDark = Color(0xFF9C8F86)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFEADFDA)
val inverseOnSurfaceDark = Color(0xFF201A17)
val inversePrimaryDark = BrandGreen

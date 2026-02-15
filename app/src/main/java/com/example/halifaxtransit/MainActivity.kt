package com.example.halifaxtransit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.halifaxtransit.data.Route
import com.example.halifaxtransit.ui.RoutesViewModel
import com.example.halifaxtransit.ui.theme.HalifaxTransitTheme
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.delay

/**
 * 底部导航两个 tab：Map / Routes
 */
enum class BottomNavDestination(
    val label: String,
    val icon: ImageVector
) {
    Map("Map", Icons.Filled.Home),
    Routes("Routes", Icons.Filled.DirectionsBus)
}

class MainActivity : ComponentActivity() {

    // ViewModel used for map (GTFS real-time data)
    private val mainViewModel: MainViewModel by viewModels()

    // ViewModel used by Routes + Room database
    private val routesViewModel: RoutesViewModel by viewModels()

    // Request permission to get location.
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.i("TESTING", "New permission granted by user, proceed...")
            } else {
                Log.i("TESTING", "Permission DENIED by user! Display toast...")
                Toast.makeText(
                    this,
                    "Please enable location permission in Settings to use this feature.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Load GTFS real-time bus location
        mainViewModel.loadGtfsBusPositions()

        setContent {
            val context = LocalContext.current

            // Check / Request location permission
            LaunchedEffect(Unit) {
                if (
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i("TESTING", "Permission previously granted, proceed...")
                } else {
                    Log.i("TESTING", "Permission not yet granted, launching permission request...")
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            HalifaxTransitTheme {
                //Control whether to display Splash
                var showSplash by remember { mutableStateOf(true) }

                // Switch to the actual app content after 2 seconds.
                LaunchedEffect(Unit) {
                    delay(2000)
                    showSplash = false
                }

                if (showSplash) {
                    SplashScreen()
                } else {
                    HalifaxTransitApp(
                        mainViewModel = mainViewModel,
                        routesViewModel = routesViewModel
                    )
                }
            }
        }
    }
}

/**
 * The overall framework of the top bar and bottom navigation (Scaffold)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalifaxTransitApp(
    mainViewModel: MainViewModel,
    routesViewModel: RoutesViewModel
) {
    var currentDestination by rememberSaveable {
        mutableStateOf(BottomNavDestination.Map)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Halifax Transit") }
            )
        },
        bottomBar = {
            NavigationBar {
                BottomNavDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination == destination,
                        onClick = { currentDestination = destination },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(text = destination.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                BottomNavDestination.Map ->
                    MapScreen(mainViewModel, routesViewModel)

                BottomNavDestination.Routes ->
                    RoutesScreen(routesViewModel)
            }
        }
    }
}

/**
 * Map tab 对应的屏幕：
 * 需要 GTFS 实时数据 + 用户在 Routes 里收藏的路线
 */
@Composable
fun MapScreen(
    mainViewModel: MainViewModel,
    routesViewModel: RoutesViewModel
) {
    // 从数据库拿所有 routes，然后取 isFavorite = true 的 id
    val routes by routesViewModel.routes.collectAsState(initial = emptyList())
    val selectedIds: Set<Int> = routes
        .filter { it.isFavorite }
        .map { it.id }
        .toSet()

    DisplayMap(
        mainViewModel = mainViewModel,
        selectedRouteIds = selectedIds
    )
}

/**
 * Routes tab：
 * 展示数据库中的所有 routes，并允许选中/取消选中，
 * 这些选中的 route 会在地图上高亮。
 */
@Composable
fun RoutesScreen(routesViewModel: RoutesViewModel) {
    val scrollState = rememberScrollState()

    // All routes from database
    val routes by routesViewModel.routes.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // 标题
        Text(
            text = "Routes",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 简短说明
        Text(
            text = "Tap a route to highlight it on the map. " +
                    "Selected routes are stored locally using Room.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Button
        Button(
            onClick = { /* 搜索功能可留到之后 Phase */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Search Routes")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ====  routes list ====
        routes.forEach { route: Route ->
            RouteCard(
                id = route.id,
                name = route.name,
                selected = route.isFavorite,
                onClick = { routesViewModel.toggleFavorite(route) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==== Theme preview =====
        Text(
            text = "Theme preview:",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        RowSwatches()
    }
}

/**
 * 单个 Route 的卡片。
 * 选中时用你的 tertiary 颜色高亮。
 */
@Composable
private fun RouteCard(
    id: Int,
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor =
        if (selected) MaterialTheme.colorScheme.tertiaryContainer
        else MaterialTheme.colorScheme.secondaryContainer

    val textColor =
        if (selected) MaterialTheme.colorScheme.onTertiaryContainer
        else MaterialTheme.colorScheme.onSecondaryContainer

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Route $id",
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}

@Composable
private fun RowSwatches() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Primary
        ColorSwatchRow(
            label = "Primary",
            color = MaterialTheme.colorScheme.primary,
            onColor = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Secondary
        ColorSwatchRow(
            label = "Secondary",
            color = MaterialTheme.colorScheme.secondary,
            onColor = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Background
        ColorSwatchRow(
            label = "Background",
            color = MaterialTheme.colorScheme.background,
            onColor = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun ColorSwatchRow(
    label: String,
    color: Color,
    onColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(8.dp),
            color = onColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * 启动时的动画 Splash
 */
@Composable
fun SplashScreen() {
    // 呼吸动画：logo 微微放大缩小
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_ht_logo),
                contentDescription = "HT Transit logo",
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "HT Transit",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Your eco-friendly transit companion",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

/**
 * 显示 Mapbox 地图 + 用户位置 + 实时公交车辆图标。
 * Phase 3：Routes 屏幕中 isFavorite = true 的路线，用蓝色图标高亮。
 */
@Composable
fun DisplayMap(
    mainViewModel: MainViewModel,
    selectedRouteIds: Set<Int>   // 来自数据库中 isFavorite = true 的路线 id
) {
    // Get entities (bus positions) from ViewModel
    val gtfsFeed by mainViewModel.gtfs.collectAsState()
    val busPositions = gtfsFeed?.entityList

    val mapViewportState = rememberMapViewportState {
        // set default camera zoom on Halifax centre
        setCameraOptions {
            zoom(12.0)
            center(Point.fromLngLat(-63.5826, 44.6510))
            pitch(0.0)
            bearing(0.0)
        }
    }

    MapboxMap(
        mapViewportState = mapViewportState,
    ) {
        // Map effect will take effect once permission is granted to display user's location.
        MapEffect(Unit) { mapView ->
            mapView.location.updateSettings {
                locationPuck = createDefault2DPuck(withBearing = true)
                enabled = true
                puckBearing = PuckBearing.COURSE
                puckBearingEnabled = true
            }
            mapViewportState.transitionToFollowPuckState()
        }

        // Display bus locations
        if (busPositions != null) {
            for (bus in busPositions) {
                val routeIdString = bus.vehicle.trip.routeId
                val routeIdInt = routeIdString.toIntOrNull()

                // 只有当 GTFS 中的 routeId 能转换成 Int，并且在收藏列表里时，才高亮
                val isHighlighted =
                    routeIdInt != null && selectedRouteIds.contains(routeIdInt)

                // Insert a ViewAnnotation at specific geo coordinate.
                ViewAnnotation(
                    options = viewAnnotationOptions {
                        geometry(
                            Point.fromLngLat(
                                bus.vehicle.position.longitude.toDouble(),
                                bus.vehicle.position.latitude.toDouble()
                            )
                        )
                    }
                ) {
                    // ViewAnnotation content
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (isHighlighted) R.drawable.busblue
                                else R.drawable.bus
                            ),
                            contentDescription = "Route $routeIdString",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Text(
                            text = routeIdString,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

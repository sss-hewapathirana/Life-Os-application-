package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LifeOSDatabase
import com.example.data.repository.LifeOSRepository
import com.example.ui.LifeOSTab
import com.example.ui.LifeOSViewModel
import com.example.ui.LifeOSViewModelFactory
import com.example.ui.components.LifeOSTopBar
import com.example.ui.components.QuickAddDialog
import com.example.ui.components.SearchDialog
import com.example.ui.screens.AssetsScreen
import com.example.ui.screens.HabitsScreen
import com.example.ui.screens.MaintenanceScreen
import com.example.ui.screens.MoneyScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.PlanScreen
import com.example.ui.screens.ReflectionScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SubscriptionsScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.VaultScreen
import com.example.ui.theme.BorderLine
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.LifeOSTheme
import com.example.ui.theme.MutedText
import com.example.ui.theme.SoftSage
import kotlinx.coroutines.launch

enum class SubScreen {
    HABITS,
    NOTES,
    REFLECTION,
    SETTINGS,
    SUBSCRIPTIONS,
    ASSETS,
    MAINTENANCE,
    REMINDERS
}

class MainActivity : ComponentActivity() {

    private val viewModel: LifeOSViewModel by viewModels {
        val database = LifeOSDatabase.getDatabase(applicationContext)
        val repository = LifeOSRepository(database)
        LifeOSViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIncomingIntent(intent)
        enableEdgeToEdge()
        setContent {
            val settings by viewModel.settingsMap.collectAsState()
            val themeMode = settings["themeMode"] ?: "system"
            val themePalette = settings["themePalette"] ?: "forest"
            val selectedTab by viewModel.selectedTab.collectAsState()
            val pageAccentKey = "pageAccent_${selectedTab.name.lowercase()}"
            val pageAccent = settings[pageAccentKey]

            val darkTheme = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            LifeOSTheme(
                darkTheme = darkTheme,
                themePalette = themePalette,
                pageAccent = pageAccent
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LifeOSApp(viewModel = viewModel)
                }
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: android.content.Intent?) {
        if (intent == null) return
        val action = intent.action
        val data = intent.data

        if (action == android.content.Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(android.content.Intent.EXTRA_TEXT)
            if (!sharedText.isNullOrBlank()) {
                val today = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    java.time.LocalDate.now().toString()
                } else {
                    "2026-10-16"
                }
                viewModel.saveTask(
                    com.example.data.model.TaskEntity(
                        title = sharedText,
                        date = today,
                        time = "12:00",
                        priority = "High",
                        cost = 0.0,
                        isDone = false
                    )
                )
                viewModel.selectTab(LifeOSTab.TASKS)
            }
        } else if (action == android.content.Intent.ACTION_VIEW && data?.scheme == "lifeos") {
            val host = data.host
            val title = data.getQueryParameter("title") ?: "New Item from AI"
            val fallbackDate = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                java.time.LocalDate.now().toString()
            } else {
                "2026-10-16"
            }
            val date = data.getQueryParameter("date") ?: fallbackDate
            val priority = data.getQueryParameter("priority") ?: "High"

            when (host) {
                "task" -> {
                    viewModel.saveTask(
                        com.example.data.model.TaskEntity(
                            title = title,
                            date = date,
                            time = "12:00",
                            priority = priority,
                            cost = 0.0,
                            isDone = false
                        )
                    )
                    viewModel.selectTab(LifeOSTab.TASKS)
                }
                "event" -> {
                    val startTime = data.getQueryParameter("startTime") ?: "09:00"
                    val endTime = data.getQueryParameter("endTime") ?: "10:00"
                    val category = data.getQueryParameter("category") ?: "Work"
                    viewModel.saveEvent(
                        com.example.data.model.EventEntity(
                            title = title,
                            date = date,
                            startTime = startTime,
                            endTime = endTime,
                            category = category,
                            description = "Added via Deep Link",
                            location = "",
                            isAllDay = false,
                            plannedCost = 0.0
                        )
                    )
                    viewModel.selectTab(LifeOSTab.PLAN)
                }
                "note" -> {
                    val content = data.getQueryParameter("content") ?: ""
                    viewModel.saveNote(
                        com.example.data.model.NoteEntity(
                            title = title,
                            date = date,
                            body = content,
                            tags = listOf("AI")
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LifeOSApp(viewModel: LifeOSViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val showQuickAddDialog by viewModel.showQuickAddDialog.collectAsState()
    val showSearchDialog by viewModel.showSearchDialog.collectAsState()

    val events by viewModel.events.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val bills by viewModel.bills.collectAsState()
    val vaultRecords by viewModel.vaultRecords.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val assets by viewModel.assets.collectAsState()
    val subscriptions by viewModel.subscriptions.collectAsState()
    val maintenance by viewModel.maintenance.collectAsState()

    var currentSubScreen by remember { mutableStateOf<SubScreen?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // BackHandler: pop sub-screens or return to Plan tab
    BackHandler(enabled = currentSubScreen != null || selectedTab != LifeOSTab.PLAN) {
        if (currentSubScreen != null) {
            currentSubScreen = null
        } else if (selectedTab != LifeOSTab.PLAN) {
            viewModel.selectTab(LifeOSTab.PLAN)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .width(310.dp)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Drawer Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ForestGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "l.",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Italic,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "life",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "OS",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Normal,
                                    color = MutedText
                                )
                            }
                            Text(
                                text = "Personal Space",
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedText,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Text(
                        text = "YOUR SPACE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedText,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                    )

                    DrawerItem(
                        icon = Icons.Default.CalendarMonth,
                        label = "Plan & Calendar",
                        isSelected = currentSubScreen == null && selectedTab == LifeOSTab.PLAN,
                        count = "${events.size}",
                        onClick = {
                            currentSubScreen = null
                            viewModel.selectTab(LifeOSTab.PLAN)
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.CheckCircleOutline,
                        label = "Tasks & To-Dos",
                        isSelected = currentSubScreen == null && selectedTab == LifeOSTab.TASKS,
                        count = "${tasks.count { !it.isDone }}",
                        onClick = {
                            currentSubScreen = null
                            viewModel.selectTab(LifeOSTab.TASKS)
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.AccountBalanceWallet,
                        label = "Money & Costs",
                        isSelected = currentSubScreen == null && selectedTab == LifeOSTab.MONEY,
                        onClick = {
                            currentSubScreen = null
                            viewModel.selectTab(LifeOSTab.MONEY)
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Repeat,
                        label = "Subscriptions",
                        isSelected = currentSubScreen == SubScreen.SUBSCRIPTIONS,
                        count = "${subscriptions.count { it.status == "active" }}",
                        onClick = {
                            currentSubScreen = SubScreen.SUBSCRIPTIONS
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.FolderSpecial,
                        label = "Life Records (Vault)",
                        isSelected = currentSubScreen == null && selectedTab == LifeOSTab.VAULT,
                        count = "${vaultRecords.size}",
                        onClick = {
                            currentSubScreen = null
                            viewModel.selectTab(LifeOSTab.VAULT)
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Devices,
                        label = "Assets & Equipment",
                        isSelected = currentSubScreen == SubScreen.ASSETS,
                        count = "${assets.size}",
                        onClick = {
                            currentSubScreen = SubScreen.ASSETS
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Build,
                        label = "Maintenance & Service",
                        isSelected = currentSubScreen == SubScreen.MAINTENANCE,
                        count = "${maintenance.size}",
                        onClick = {
                            currentSubScreen = SubScreen.MAINTENANCE
                            scope.launch { drawerState.close() }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = BorderLine)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "MAKE SPACE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedText,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                    )

                    DrawerItem(
                        icon = Icons.Default.LocalFireDepartment,
                        label = "Habits & Routines",
                        isSelected = currentSubScreen == SubScreen.HABITS,
                        count = "${habits.size}",
                        onClick = {
                            currentSubScreen = SubScreen.HABITS
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Description,
                        label = "Notes & Ideas",
                        isSelected = currentSubScreen == SubScreen.NOTES,
                        count = "${notes.size}",
                        onClick = {
                            currentSubScreen = SubScreen.NOTES
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Notifications,
                        label = "Reminders & Nudges",
                        isSelected = currentSubScreen == SubScreen.REMINDERS,
                        onClick = {
                            currentSubScreen = SubScreen.REMINDERS
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.RateReview,
                        label = "Weekly Reflection",
                        isSelected = currentSubScreen == SubScreen.REFLECTION,
                        onClick = {
                            currentSubScreen = SubScreen.REFLECTION
                            scope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Settings,
                        label = "Settings & Config",
                        isSelected = currentSubScreen == SubScreen.SETTINGS,
                        onClick = {
                            currentSubScreen = SubScreen.SETTINGS
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        if (currentSubScreen != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (currentSubScreen) {
                    SubScreen.HABITS -> HabitsScreen(viewModel = viewModel, onBack = { currentSubScreen = null })
                    SubScreen.NOTES -> NotesScreen(viewModel = viewModel, onBack = { currentSubScreen = null })
                    SubScreen.REFLECTION -> ReflectionScreen(viewModel = viewModel, onBack = { currentSubScreen = null })
                    SubScreen.SETTINGS -> SettingsScreen(viewModel = viewModel, onBack = { currentSubScreen = null })
                    SubScreen.SUBSCRIPTIONS -> SubscriptionsScreen(viewModel = viewModel, onBack = { currentSubScreen = null })
                    SubScreen.ASSETS -> AssetsScreen(viewModel = viewModel, onBack = { currentSubScreen = null })
                    SubScreen.MAINTENANCE -> MaintenanceScreen(viewModel = viewModel, onBack = { currentSubScreen = null })
                    SubScreen.REMINDERS -> RemindersScreen(viewModel = viewModel, onBack = { currentSubScreen = null })
                    null -> {}
                }
            }
        } else {
            Scaffold(
                topBar = {
                    LifeOSTopBar(
                        currentTab = selectedTab,
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onTodayClick = { viewModel.selectToday() },
                        onSearchClick = { viewModel.showSearchDialog.value = true },
                        onQuickAddClick = { viewModel.showQuickAddDialog.value = true }
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        modifier = Modifier.testTag("bottom_nav_bar")
                    ) {
                        LifeOSTab.values().forEach { tab ->
                            val isSelected = selectedTab == tab
                            val (icon, label) = when (tab) {
                                LifeOSTab.PLAN -> Icons.Default.CalendarMonth to "Plan"
                                LifeOSTab.TASKS -> Icons.Default.CheckCircleOutline to "Tasks"
                                LifeOSTab.MONEY -> Icons.Default.AccountBalanceWallet to "Money"
                                LifeOSTab.VAULT -> Icons.Default.FolderSpecial to "Vault"
                            }

                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.selectTab(tab) },
                                icon = { Icon(imageVector = icon, contentDescription = label) },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ForestGreen,
                                    selectedTextColor = ForestGreen,
                                    indicatorColor = SoftSage,
                                    unselectedIconColor = MutedText,
                                    unselectedTextColor = MutedText
                                ),
                                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
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
                    when (selectedTab) {
                        LifeOSTab.PLAN -> PlanScreen(viewModel = viewModel)
                        LifeOSTab.TASKS -> TasksScreen(viewModel = viewModel)
                        LifeOSTab.MONEY -> MoneyScreen(viewModel = viewModel)
                        LifeOSTab.VAULT -> VaultScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }

    // Quick Add Modal
    if (showQuickAddDialog) {
        QuickAddDialog(
            onDismiss = { viewModel.showQuickAddDialog.value = false },
            onAddEvent = {
                viewModel.editingEvent.value = null
                viewModel.showEventDialog.value = true
            },
            onAddTask = {
                viewModel.editingTask.value = null
                viewModel.showTaskDialog.value = true
            },
            onAddExpense = {
                viewModel.showExpenseDialog.value = true
            },
            onAddBill = {
                viewModel.showBillDialog.value = true
            },
            onAddVault = {
                viewModel.editingVault.value = null
                viewModel.showVaultDialog.value = true
            }
        )
    }

    // Global Search Modal
    if (showSearchDialog) {
        SearchDialog(
            events = events,
            tasks = tasks,
            bills = bills,
            vaultRecords = vaultRecords,
            onDismiss = { viewModel.showSearchDialog.value = false },
            onSelectEvent = { ev ->
                currentSubScreen = null
                viewModel.selectDate(ev.date)
                viewModel.selectTab(LifeOSTab.PLAN)
                viewModel.editingEvent.value = ev
                viewModel.showEventDialog.value = true
            },
            onSelectTask = { t ->
                currentSubScreen = null
                viewModel.selectTab(LifeOSTab.TASKS)
                viewModel.editingTask.value = t
                viewModel.showTaskDialog.value = true
            },
            onSelectBill = { b ->
                currentSubScreen = null
                viewModel.selectTab(LifeOSTab.MONEY)
            },
            onSelectVault = { v ->
                currentSubScreen = null
                viewModel.selectTab(LifeOSTab.VAULT)
                viewModel.editingVault.value = v
                viewModel.showVaultDialog.value = true
            }
        )
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    count: String? = null,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp)) },
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
                if (count != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) ForestGreen.copy(alpha = 0.2f) else SoftSage)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = count,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                    }
                }
            }
        },
        selected = isSelected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = SoftSage,
            selectedIconColor = ForestGreen,
            selectedTextColor = ForestGreen,
            unselectedIconColor = MutedText,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

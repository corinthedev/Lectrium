// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.corin.lectrium.R
import org.corin.lectrium.components.IndicatorStyle
import org.corin.lectrium.components.PageIndicator
import org.corin.lectrium.components.dialogs.CustomDurationDialog
import org.corin.lectrium.model.ColorSchemeMode
import org.corin.lectrium.model.CustomReminder
import org.corin.lectrium.model.NavBarStyle
import org.corin.lectrium.model.ThemeMode
import org.corin.lectrium.model.TimeUnit

private data class FeatureHighlight(
    val title: String,
    val description: String,
    @param:DrawableRes val iconRes: Int
)

private data class OnBoardingPageData(
    val title: String,
    val subtitle: String,
    @param:DrawableRes val pageIconRes: Int,
    val highlights: List<FeatureHighlight>,
    val buttonLabel: String
)

private data class SampleClassItem(
    val title: String,
    val time: String,
    val room: String,
    val status: String,
    val isCurrent: Boolean = false
)

private val sampleScheduleList = listOf(
    SampleClassItem("MATH101 — Calculus I", "08:00 AM - 09:00 AM", "Math Bldg • Room 101", "Completed"),
    SampleClassItem("CS101 — Data Structures", "09:00 AM - 10:30 AM", "Science Hall • Room 302", "In Progress", true),
    SampleClassItem("MATH201 — Linear Algebra", "11:00 AM - 12:30 PM", "Academic Bldg • Room 105", "Upcoming"),
    SampleClassItem("HIST102 — World History", "01:30 PM - 02:30 PM", "Humanities • Room 204", "Upcoming"),
    SampleClassItem("PHY102 — General Physics", "02:30 PM - 04:00 PM", "Physics Lab • Room 201", "Upcoming"),
    SampleClassItem("ENG105 — Technical Writing", "04:15 PM - 05:30 PM", "Liberal Arts • Room 112", "Upcoming"),
    SampleClassItem("CS202 — Database Systems", "06:00 PM - 07:30 PM", "IT Complex • Room 408", "Upcoming")
)

private val onboardingPages = listOf(
    OnBoardingPageData(
        title = "Ready to start your journey?",
        subtitle = "Lectrium helps you organize class schedules, track attendance, calculate free time, and set custom reminders so you never miss a lecture.",
        pageIconRes = R.drawable.rocket_launch_icon,
        highlights = listOf(
            FeatureHighlight("Smart Timetables", "Organize weekly lectures & schedules with ease.", R.drawable.alarm_icon),
            FeatureHighlight("Custom Reminders", "Never miss a class with customizable alerts.", R.drawable.notifications_icon),
            FeatureHighlight("Attendance Goals", "Monitor presence and maintain attendance targets.", R.drawable.dock_icon)
        ),
        buttonLabel = "Continue"
    ),
    OnBoardingPageData(
        title = "Create Your Profile",
        subtitle = "Tell us about yourself so Lectrium can personalize your academic dashboard.",
        pageIconRes = R.drawable.person_icon,
        highlights = emptyList(),
        buttonLabel = "Continue"
    ),
    OnBoardingPageData(
        title = "Configure Class Alerts",
        subtitle = "Choose when and how Lectrium notifies you before your classes start.",
        pageIconRes = R.drawable.alarm_icon,
        highlights = emptyList(),
        buttonLabel = "Continue"
    ),
    OnBoardingPageData(
        title = "Set Your Attendance Target",
        subtitle = "Define your minimum required attendance percentage to receive timely warnings.",
        pageIconRes = R.drawable.bar_chart_icon,
        highlights = emptyList(),
        buttonLabel = "Continue"
    ),
    OnBoardingPageData(
        title = "Make Lectrium Yours",
        subtitle = "Personalize your app with dynamic themes, accent colors, and navigation layout.",
        pageIconRes = R.drawable.palette_icon,
        highlights = emptyList(),
        buttonLabel = "Get Started"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    onSaveProfile: (firstName: String, middleName: String, lastName: String, email: String, age: String, university: String, program: String, yearLevel: String, studentStatus: String) -> Unit = { _, _, _, _, _, _, _, _, _ -> },
    onSaveThemeMode: (ThemeMode) -> Unit = {},
    onSaveColorSchemeMode: (ColorSchemeMode) -> Unit = {},
    onSaveNavBarStyle: (NavBarStyle) -> Unit = {},
    onOnBoardingFinished: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val pagerCoroutineScope = rememberCoroutineScope()

    // Page 2 State: Student Profile
    var profileFirstName by remember { mutableStateOf("") }
    var profileMiddleName by remember { mutableStateOf("") }
    var profileLastName by remember { mutableStateOf("") }
    var profileEmail by remember { mutableStateOf("") }
    var profileAge by remember { mutableStateOf("") }
    var profileUniversity by remember { mutableStateOf("") }
    var profileProgram by remember { mutableStateOf("") }
    var profileYearLevel by remember { mutableStateOf("1st Year") }
    var profileStudentStatus by remember { mutableStateOf("Regular") }

    // Page 3 State: Class Alert Preferences & Custom Dialog
    var reminderOffsetMinutes by remember { mutableIntStateOf(15) }
    var isCustomReminderSelected by remember { mutableStateOf(false) }
    var showCustomDurationDialog by remember { mutableStateOf(false) }
    var scheduleViewPreference by remember { mutableStateOf("Daily View") }

    // Page 4 State: Attendance Target
    var targetAttendancePercent by remember { mutableFloatStateOf(80f) }

    // Page 5 State: Personalization
    var selectedThemeMode by remember { mutableStateOf(ThemeMode.SYSTEM) }
    var selectedColorSchemeMode by remember { mutableStateOf(ColorSchemeMode.DEFAULT) }
    var selectedNavBarStyle by remember { mutableStateOf(NavBarStyle.BOTTOM) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row with Indicator, Back Button, & Page 1 Skip Action
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
            ) {
                if (pagerState.currentPage > 0) {
                    TextButton(
                        onClick = {
                            pagerCoroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                PageIndicator(
                    pagerState = pagerState,
                    style = IndicatorStyle.ElasticPill,
                    modifier = Modifier.align(Alignment.Center)
                )

                if (pagerState.currentPage == 0) {
                    TextButton(
                        onClick = onOnBoardingFinished,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "Skip",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { pageIndex ->
                val pageData = onboardingPages[pageIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(pageData.pageIconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(18.dp)
                            .size(28.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = pageData.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = pageData.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Page 1 Specific Content: Feature Highlights & Live Schedule Preview
                    if (pageIndex == 0) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            items(pageData.highlights) { feature ->
                                ElevatedCard(
                                    modifier = Modifier
                                        .width(155.dp)
                                        .height(135.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(MaterialTheme.colorScheme.primaryContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                painter = painterResource(feature.iconRes),
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = feature.title,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = feature.description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 8.dp)
                                .weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Live Schedule Preview",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Monday • 7 Classes",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(sampleScheduleList) { sampleClass ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (sampleClass.isCurrent) {
                                                MaterialTheme.colorScheme.primaryContainer
                                            } else {
                                                MaterialTheme.colorScheme.surfaceContainerHigh
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = sampleClass.title,
                                                        style = MaterialTheme.typography.titleSmall,
                                                        color = if (sampleClass.isCurrent) {
                                                            MaterialTheme.colorScheme.onPrimaryContainer
                                                        } else {
                                                            MaterialTheme.colorScheme.onSurface
                                                        }
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = "${sampleClass.time}  •  ${sampleClass.room}",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = if (sampleClass.isCurrent) {
                                                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                                        } else {
                                                            MaterialTheme.colorScheme.onSurfaceVariant
                                                        }
                                                    )
                                                }

                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = when {
                                                        sampleClass.isCurrent -> MaterialTheme.colorScheme.primary
                                                        sampleClass.status == "Completed" -> MaterialTheme.colorScheme.surfaceContainer
                                                        else -> MaterialTheme.colorScheme.secondaryContainer
                                                    }
                                                ) {
                                                    Text(
                                                        text = sampleClass.status,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = when {
                                                            sampleClass.isCurrent -> MaterialTheme.colorScheme.onPrimary
                                                            sampleClass.status == "Completed" -> MaterialTheme.colorScheme.onSurfaceVariant
                                                            else -> MaterialTheme.colorScheme.onSecondaryContainer
                                                        },
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Page 2 Specific Content: Student Profile Form
                    if (pageIndex == 1) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.person_icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = profileFirstName,
                                    onValueChange = { profileFirstName = it },
                                    label = { Text("Given Name / First Name *") },
                                    singleLine = true,
                                    isError = profileFirstName.isBlank(),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = profileMiddleName,
                                        onValueChange = { profileMiddleName = it },
                                        label = { Text("Middle Name") },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f)
                                    )

                                    OutlinedTextField(
                                        value = profileLastName,
                                        onValueChange = { profileLastName = it },
                                        label = { Text("Last Name *") },
                                        singleLine = true,
                                        isError = profileLastName.isBlank(),
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = profileEmail,
                                        onValueChange = { profileEmail = it },
                                        label = { Text("Email Address *") },
                                        singleLine = true,
                                        isError = profileEmail.isBlank(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                        modifier = Modifier.weight(0.72f)
                                    )

                                    OutlinedTextField(
                                        value = profileAge,
                                        onValueChange = { profileAge = it },
                                        label = { Text("Age") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(0.28f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Year Level *",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        items(listOf("1st Year", "2nd Year", "3rd Year", "4th Year", "Extendee")) { level ->
                                            FilterChip(
                                                selected = (profileYearLevel == level),
                                                onClick = { profileYearLevel = level },
                                                label = { Text(level, style = MaterialTheme.typography.labelSmall) }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Student Status *",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        listOf("Regular", "Irregular", "Shiftee").forEach { status ->
                                            FilterChip(
                                                selected = (profileStudentStatus == status),
                                                onClick = { profileStudentStatus = status },
                                                label = { Text(status, style = MaterialTheme.typography.labelSmall) }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = profileProgram,
                                    onValueChange = { profileProgram = it },
                                    label = { Text("Program / Major *") },
                                    singleLine = true,
                                    isError = profileProgram.isBlank(),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = profileUniversity,
                                    onValueChange = { profileUniversity = it },
                                    label = { Text("University / Institution *") },
                                    singleLine = true,
                                    isError = profileUniversity.isBlank(),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // Page 3 Specific Content: Class Alert Preferences
                    if (pageIndex == 2) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Default Class Reminder Timing",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    items(listOf(5, 10, 15, 30)) { mins ->
                                        FilterChip(
                                            selected = (!isCustomReminderSelected && reminderOffsetMinutes == mins),
                                            onClick = {
                                                isCustomReminderSelected = false
                                                reminderOffsetMinutes = mins
                                            },
                                            label = { Text("$mins mins") }
                                        )
                                    }
                                    item {
                                        FilterChip(
                                            selected = isCustomReminderSelected,
                                            onClick = {
                                                showCustomDurationDialog = true
                                            },
                                            label = {
                                                Text(
                                                    if (isCustomReminderSelected) "$reminderOffsetMinutes mins (Custom)" else "Custom..."
                                                )
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Schedule View Preference",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                SingleChoiceSegmentedButtonRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp)
                                ) {
                                    listOf("Daily View", "Weekly Grid").forEachIndexed { idx, option ->
                                        SegmentedButton(
                                            selected = (scheduleViewPreference == option),
                                            onClick = { scheduleViewPreference = option },
                                            shape = SegmentedButtonDefaults.itemShape(
                                                index = idx,
                                                count = 2,
                                                baseShape = RoundedCornerShape(8.dp)
                                            )
                                        ) {
                                            Text(option)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Dynamic View Preview Box
                                if (scheduleViewPreference == "Daily View") {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.alarm_icon),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(18.dp),
                                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "Class Starting Soon! (Daily Alert)",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    text = "CS101 starts in $reminderOffsetMinutes mins • Room 302",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // Weekly Grid Timetable Preview
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = "Weekly Grid Timetable Preview",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                listOf("Mon", "Tue", "Wed", "Thu", "Fri").forEach { day ->
                                                    Column(
                                                        modifier = Modifier.weight(1f),
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        Text(
                                                            text = day,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = MaterialTheme.colorScheme.primary
                                                        )
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(36.dp)
                                                                .clip(RoundedCornerShape(6.dp))
                                                                .background(
                                                                    if (day == "Mon" || day == "Wed" || day == "Fri") {
                                                                        MaterialTheme.colorScheme.primaryContainer
                                                                    } else {
                                                                        MaterialTheme.colorScheme.secondaryContainer
                                                                    }
                                                                ),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Text(
                                                                text = if (day == "Mon" || day == "Wed" || day == "Fri") "CS101" else "MATH",
                                                                style = MaterialTheme.typography.labelSmall,
                                                                color = if (day == "Mon" || day == "Wed" || day == "Fri") {
                                                                    MaterialTheme.colorScheme.onPrimaryContainer
                                                                } else {
                                                                    MaterialTheme.colorScheme.onSecondaryContainer
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Page 4 Specific Content: Attendance Target Setup
                    if (pageIndex == 3) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Target Attendance Goal",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = "${targetAttendancePercent.toInt()}%",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Slider(
                                    value = targetAttendancePercent,
                                    onValueChange = { targetAttendancePercent = it },
                                    valueRange = 60f..95f,
                                    steps = 6
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Live Attendance Status Preview",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val targetInt = targetAttendancePercent.toInt()
                                    listOf(
                                        Triple("CS101 — Data Structures", targetInt + 8, "Safe"),
                                        Triple("MATH201 — Linear Algebra", targetInt, "Target Met"),
                                        Triple("PHY102 — General Physics", (targetInt - 8).coerceAtLeast(50), "Warning"),
                                        Triple("CHEM101 — Organic Chem", (targetInt - 22).coerceAtLeast(40), "Advised to Withdraw (5.0)")
                                    ).forEach { (subject, percent, status) ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = subject,
                                                        style = MaterialTheme.typography.titleSmall,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = "Current: $percent%  •  Target: $targetInt%",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }

                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = when (status) {
                                                        "Safe" -> MaterialTheme.colorScheme.primaryContainer
                                                        "Target Met" -> MaterialTheme.colorScheme.secondaryContainer
                                                        "Warning" -> MaterialTheme.colorScheme.tertiaryContainer
                                                        else -> MaterialTheme.colorScheme.errorContainer
                                                    }
                                                ) {
                                                    Text(
                                                        text = status,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = when (status) {
                                                            "Safe" -> MaterialTheme.colorScheme.onPrimaryContainer
                                                            "Target Met" -> MaterialTheme.colorScheme.onSecondaryContainer
                                                            "Warning" -> MaterialTheme.colorScheme.onTertiaryContainer
                                                            else -> MaterialTheme.colorScheme.onErrorContainer
                                                        },
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Page 5 Specific Content: Personalization & Theme Styling
                    if (pageIndex == 4) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "App Theme Mode",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                SingleChoiceSegmentedButtonRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp)
                                ) {
                                    ThemeMode.entries.forEachIndexed { idx, mode ->
                                        SegmentedButton(
                                            selected = (selectedThemeMode == mode),
                                            onClick = {
                                                selectedThemeMode = mode
                                                onSaveThemeMode(mode)
                                            },
                                            shape = SegmentedButtonDefaults.itemShape(
                                                index = idx,
                                                count = ThemeMode.entries.size,
                                                baseShape = RoundedCornerShape(8.dp)
                                            )
                                        ) {
                                            Text(mode.label)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Color Scheme Palette",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                SingleChoiceSegmentedButtonRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp)
                                ) {
                                    ColorSchemeMode.entries.forEachIndexed { idx, mode ->
                                        SegmentedButton(
                                            selected = (selectedColorSchemeMode == mode),
                                            onClick = {
                                                selectedColorSchemeMode = mode
                                                onSaveColorSchemeMode(mode)
                                            },
                                            shape = SegmentedButtonDefaults.itemShape(
                                                index = idx,
                                                count = ColorSchemeMode.entries.size,
                                                baseShape = RoundedCornerShape(8.dp)
                                            )
                                        ) {
                                            Text(mode.label)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Navigation Bar Style",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                SingleChoiceSegmentedButtonRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp)
                                ) {
                                    NavBarStyle.entries.forEachIndexed { idx, style ->
                                        SegmentedButton(
                                            selected = (selectedNavBarStyle == style),
                                            onClick = {
                                                selectedNavBarStyle = style
                                                onSaveNavBarStyle(style)
                                            },
                                            shape = SegmentedButtonDefaults.itemShape(
                                                index = idx,
                                                count = NavBarStyle.entries.size,
                                                baseShape = RoundedCornerShape(8.dp)
                                            )
                                        ) {
                                            Text(
                                                when (style) {
                                                    NavBarStyle.BOTTOM -> "Bottom Bar"
                                                    NavBarStyle.FLOATING -> "Floating Dock"
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    val isPage2Valid = (pageIndex != 1) || (
                            profileFirstName.isNotBlank() &&
                                    profileLastName.isNotBlank() &&
                        profileEmail.isNotBlank() &&
                        profileUniversity.isNotBlank() &&
                        profileProgram.isNotBlank()
                    )

                    Button(
                        onClick = {
                            if (pagerState.currentPage < onboardingPages.size - 1) {
                                pagerCoroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                onSaveProfile(
                                    profileFirstName,
                                    profileMiddleName,
                                    profileLastName,
                                    profileEmail,
                                    profileAge,
                                    profileUniversity,
                                    profileProgram,
                                    profileYearLevel,
                                    profileStudentStatus
                                )
                                onOnBoardingFinished()
                            }
                        },
                        enabled = isPage2Valid,
                        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
                        modifier = Modifier
                            .padding(bottom = 18.dp)
                            .width(325.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = if (pageIndex == 1 && !isPage2Valid) "Fill Required Fields (*)" else pageData.buttonLabel,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

        }
    }

    if (showCustomDurationDialog) {
        CustomDurationDialog(
            initialCustomReminder = CustomReminder(reminderOffsetMinutes, TimeUnit.MINUTES),
            maxMinutes = CustomReminder.DEFAULT_MAX_MINUTES,
            onConfirm = { customReminder ->
                reminderOffsetMinutes = customReminder.totalMinutes
                isCustomReminderSelected = true
                showCustomDurationDialog = false
            },
            onDismiss = {
                showCustomDurationDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingScreenPreview() {
    OnBoardingScreen()
}

// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.corin.lectrium.components.dialogs.AddAssignmentDialog
import org.corin.lectrium.components.dialogs.AddCourseDialog
import org.corin.lectrium.components.dialogs.AddExamDialog
import org.corin.lectrium.components.dialogs.AddProjectDialog
import org.corin.lectrium.components.navigation.GooMorphFloatingNavBar
import org.corin.lectrium.components.navigation.SpeedDialOption
import org.corin.lectrium.model.ScreenDestinations
import org.corin.lectrium.model.UserProfile
import org.corin.lectrium.preferences.ClassReminderData
import org.corin.lectrium.preferences.NavBarPreferences
import org.corin.lectrium.preferences.ThemePreferences
import org.corin.lectrium.preferences.UserProfilePreferences
import org.corin.lectrium.screens.AttendanceScreen
import org.corin.lectrium.screens.ExamsScreen
import org.corin.lectrium.screens.HomeScreen
import org.corin.lectrium.screens.ManageProfileScreen
import org.corin.lectrium.screens.OnBoardingScreen
import org.corin.lectrium.screens.OpenSourceLicensesScreen
import org.corin.lectrium.screens.PrivacyPolicyScreen
import org.corin.lectrium.screens.ScheduleScreen
import org.corin.lectrium.screens.SettingsScreen
import org.corin.lectrium.screens.TasksScreen
import org.corin.lectrium.ui.theme.LectriumTheme
import org.corin.lectrium.viewmodel.AssignmentsViewModel
import org.corin.lectrium.viewmodel.ClassReminderViewModel
import org.corin.lectrium.viewmodel.ExamsViewModel
import org.corin.lectrium.viewmodel.NavBarViewModel
import org.corin.lectrium.viewmodel.ProjectsViewModel
import org.corin.lectrium.viewmodel.ScheduleViewModel
import org.corin.lectrium.viewmodel.ThemeViewModel

class LectriumApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themePreferences = remember { ThemePreferences(applicationContext) }
            val themeViewModel = remember { ThemeViewModel(themePreferences) }
            val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()
            val colorSchemeMode by themeViewModel.colorSchemeMode.collectAsStateWithLifecycle()
            val customColorHex by themeViewModel.customColorHex.collectAsStateWithLifecycle()

            val userProfilePreferences = remember { UserProfilePreferences(applicationContext) }
            val isOnboardingCompleted by userProfilePreferences.isOnboardingCompletedFlow.collectAsStateWithLifecycle(initialValue = false)
            val isPrivacyPolicyAccepted by userProfilePreferences.isPrivacyPolicyAcceptedFlow.collectAsStateWithLifecycle(
                initialValue = false
            )
            val userProfile by userProfilePreferences.userProfile.collectAsStateWithLifecycle(initialValue = UserProfile())
            val scope = rememberCoroutineScope()

            LectriumTheme(
                themeMode = themeMode,
                colorSchemeMode = colorSchemeMode,
                customColorHex = customColorHex
            ) {
                val reminderData = remember { ClassReminderData(applicationContext) }
                val reminderViewModel = remember { ClassReminderViewModel(reminderData) }

                val navBarPreferences = remember { NavBarPreferences(applicationContext) }
                val navBarViewModel = remember { NavBarViewModel(navBarPreferences) }

                when {
                    !isOnboardingCompleted -> {
                        OnBoardingScreen(
                            onSaveProfile = { fName, mName, lName, email, age, university, program, yearLevel, studentStatus ->
                                scope.launch {
                                    userProfilePreferences.updateUserProfile(
                                        firstName = fName,
                                        middleName = mName,
                                        lastName = lName,
                                        email = email,
                                        age = age,
                                        profileUniversity = university,
                                        profileProgram = program,
                                        profileYearLevel = yearLevel,
                                        studentStatus = studentStatus
                                    )
                                }
                            },
                            onSaveThemeMode = { mode ->
                                themeViewModel.onThemeModeSelected(mode)
                            },
                            onSaveColorSchemeMode = { mode ->
                                themeViewModel.onColorSchemeModeSelected(mode)
                            },
                            onSaveNavBarStyle = { style ->
                                navBarViewModel.onNavBarStyleSelected(style)
                            },
                            onOnBoardingFinished = {
                                scope.launch {
                                    userProfilePreferences.setOnboardingCompleted(true)
                                }
                            }
                        )
                    }

                    !isPrivacyPolicyAccepted -> {
                        PrivacyPolicyScreen(
                            onAccept = {
                                scope.launch {
                                    userProfilePreferences.setPrivacyPolicyAccepted(true)
                                }
                            }
                        )
                    }

                    else -> {
                        LectriumAppContent(
                            classReminderViewModel = reminderViewModel,
                            navBarViewModel = navBarViewModel,
                            themeViewModel = themeViewModel,
                            userProfilePreferences = userProfilePreferences,
                            userProfile = userProfile
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LectriumAppContent(
    classReminderViewModel: ClassReminderViewModel? = null,
    navBarViewModel: NavBarViewModel? = null,
    themeViewModel: ThemeViewModel? = null,
    userProfilePreferences: UserProfilePreferences? = null,
    userProfile: UserProfile = UserProfile()
) {
    val scheduleViewModel: ScheduleViewModel = viewModel()
    val assignmentsViewModel: AssignmentsViewModel = viewModel()
    val projectsViewModel: ProjectsViewModel = viewModel()
    val examsViewModel: ExamsViewModel = viewModel()

    val courses by scheduleViewModel.coursesState.collectAsStateWithLifecycle()
    val schedules by scheduleViewModel.schedulesState.collectAsStateWithLifecycle()
    val viewMode by scheduleViewModel.viewModeState.collectAsStateWithLifecycle()
    val showSaturday by scheduleViewModel.showSaturdayState.collectAsStateWithLifecycle()
    val showSunday by scheduleViewModel.showSundayState.collectAsStateWithLifecycle()
    val freeGaps by scheduleViewModel.freeGapsState.collectAsStateWithLifecycle()
    val attendanceCourses by scheduleViewModel.attendanceCoursesState.collectAsStateWithLifecycle()

    val assignments by assignmentsViewModel.assignmentsState.collectAsStateWithLifecycle()
    val projects by projectsViewModel.projectsState.collectAsStateWithLifecycle()
    val exams by examsViewModel.examsState.collectAsStateWithLifecycle()

    var currentScreenDestination by remember { mutableStateOf(ScreenDestinations.HOME) }
    var currentSubScreen by remember { mutableStateOf<String?>(null) } // "MANAGE_PROFILE", "PRIVACY_POLICY", "OPEN_SOURCE_LICENSES"

    var showAddCourseDialog by remember { mutableStateOf(false) }
    var showAddAssignmentDialog by remember { mutableStateOf(false) }
    var showAddExamDialog by remember { mutableStateOf(false) }
    var showAddProjectDialog by remember { mutableStateOf(false) }

    val speedDialOptions = remember(currentScreenDestination) {
        when (currentScreenDestination) {
            ScreenDestinations.SCHEDULES -> listOf(
                SpeedDialOption(
                    "Add Course & Schedule",
                    R.drawable.dock_icon
                ) { showAddCourseDialog = true }
            )

            ScreenDestinations.TASKS -> listOf(
                SpeedDialOption(
                    "Add Assignment",
                    R.drawable.bar_chart_icon
                ) { showAddAssignmentDialog = true },
                SpeedDialOption("Add Project", R.drawable.person_icon) {
                    showAddProjectDialog = true
                }
            )

            ScreenDestinations.EXAMS -> listOf(
                SpeedDialOption("Add Exam", R.drawable.alarm_icon) { showAddExamDialog = true }
            )

            else -> emptyList()
        }
    }

    AnimatedContent(
        targetState = currentSubScreen,
        transitionSpec = {
            (fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.98f, animationSpec = tween(220)))
                .togetherWith(fadeOut(animationSpec = tween(180)))
        },
        label = "SubScreenTransition"
    ) { activeSubScreen ->
        when (activeSubScreen) {
            "MANAGE_PROFILE" -> {
                ManageProfileScreen(
                    userProfilePreferences = userProfilePreferences,
                    currentProfile = userProfile,
                    onBack = { currentSubScreen = null }
                )
            }

            "PRIVACY_POLICY" -> {
                PrivacyPolicyScreen(
                    onBack = { currentSubScreen = null }
                )
            }

            "OPEN_SOURCE_LICENSES" -> {
                OpenSourceLicensesScreen(
                    onBack = { currentSubScreen = null }
                )
            }

            else -> {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = innerPadding.calculateTopPadding(),
                                    bottom = innerPadding.calculateBottomPadding() + 48.dp
                                )
                        ) {
                            AnimatedContent(
                                targetState = currentScreenDestination,
                                transitionSpec = {
                                    (fadeIn(animationSpec = tween(220)) + scaleIn(
                                        initialScale = 0.98f,
                                        animationSpec = tween(220)
                                    ))
                                        .togetherWith(fadeOut(animationSpec = tween(180)))
                                },
                                label = "ScreenTransition"
                            ) { targetDestination ->
                                when (targetDestination) {
                                    ScreenDestinations.HOME -> HomeScreen(
                                        userName = userProfile.firstName.ifBlank { userProfile.profileName },
                                        userProfileImage = userProfile.profileImage,
                                        courses = courses,
                                        schedules = schedules,
                                        assignments = assignments,
                                        exams = exams,
                                        projects = projects,
                                        onSeeAllUpcoming = {
                                            currentScreenDestination = ScreenDestinations.TASKS
                                        },
                                        onNavigateToAttendance = {
                                            currentScreenDestination = ScreenDestinations.ATTENDANCE
                                        }
                                    )

                                    ScreenDestinations.SCHEDULES -> ScheduleScreen(
                                        courses = courses,
                                        schedules = schedules,
                                        assignments = assignments,
                                        exams = exams,
                                        projects = projects,
                                        viewMode = viewMode,
                                        showSaturday = showSaturday,
                                        showSunday = showSunday,
                                        freeGaps = freeGaps,
                                        onViewModeChange = { mode ->
                                            scheduleViewModel.setViewMode(
                                                mode
                                            )
                                        },
                                        onToggleSaturday = { show ->
                                            scheduleViewModel.setShowSaturday(
                                                show
                                            )
                                        },
                                        onToggleSunday = { show ->
                                            scheduleViewModel.setShowSunday(
                                                show
                                            )
                                        },
                                        onAddCourse = { course, scheds ->
                                            scheduleViewModel.addCourseWithSchedules(
                                                course,
                                                scheds
                                            )
                                        }
                                    )

                                    ScreenDestinations.TASKS -> TasksScreen(
                                        assignments = assignments,
                                        projects = projects,
                                        courses = courses,
                                        onToggleAssignment = { assignment ->
                                            assignmentsViewModel.toggleAssignmentStatus(
                                                assignment
                                            )
                                        },
                                        onAddAssignment = { assignment, courseCode ->
                                            assignmentsViewModel.addAssignment(
                                                assignment,
                                                courseCode
                                            )
                                        },
                                        onAddProject = { project, courseCode ->
                                            projectsViewModel.addProject(
                                                project,
                                                courseCode
                                            )
                                        }
                                    )

                                    ScreenDestinations.EXAMS -> ExamsScreen(
                                        exams = exams,
                                        courses = courses,
                                        onAddExam = { exam, courseCode ->
                                            examsViewModel.addExam(
                                                exam,
                                                courseCode
                                            )
                                        }
                                    )

                                    ScreenDestinations.ATTENDANCE -> AttendanceScreen(
                                        courses = attendanceCourses
                                    )

                                    ScreenDestinations.SETTINGS -> SettingsScreen(
                                        classReminderViewModel = classReminderViewModel,
                                        navBarViewModel = navBarViewModel,
                                        themeViewModel = themeViewModel,
                                        onNavigateToManageProfile = {
                                            currentSubScreen = "MANAGE_PROFILE"
                                        },
                                        onNavigateToPrivacyPolicy = {
                                            currentSubScreen = "PRIVACY_POLICY"
                                        },
                                        onNavigateToOpenSourceLicenses = {
                                            currentSubScreen = "OPEN_SOURCE_LICENSES"
                                        }
                                    )
                                }
                            }
                        }

                        // Floating Dock + Action FAB Row at Bottom (Raised above Android gesture bar)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp, start = 8.dp, end = 8.dp)
                        ) {
                            val isFabVisible = currentScreenDestination in listOf(
                                ScreenDestinations.SCHEDULES,
                                ScreenDestinations.TASKS,
                                ScreenDestinations.EXAMS
                            )

                            GooMorphFloatingNavBar(
                                currentScreenDestination = currentScreenDestination,
                                onScreenDestinationSelected = { selectedDestination ->
                                    currentScreenDestination = selectedDestination
                                },
                                isFabVisible = isFabVisible,
                                speedDialOptions = speedDialOptions,
                                onFabClick = {
                                    when (currentScreenDestination) {
                                        ScreenDestinations.SCHEDULES -> showAddCourseDialog = true
                                        ScreenDestinations.TASKS -> showAddAssignmentDialog = true
                                        ScreenDestinations.EXAMS -> showAddExamDialog = true
                                        else -> {}
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddCourseDialog) {
        AddCourseDialog(
            onDismissRequest = { showAddCourseDialog = false },
            onSaveCourse = { course, scheds ->
                scheduleViewModel.addCourseWithSchedules(course, scheds)
            }
        )
    }

    if (showAddAssignmentDialog) {
        AddAssignmentDialog(
            courses = courses,
            onDismissRequest = { showAddAssignmentDialog = false },
            onSaveAssignment = { assignment, courseCode ->
                assignmentsViewModel.addAssignment(assignment, courseCode)
            }
        )
    }

    if (showAddProjectDialog) {
        AddProjectDialog(
            courses = courses,
            onDismissRequest = { showAddProjectDialog = false },
            onSaveProject = { project, courseCode ->
                projectsViewModel.addProject(project, courseCode)
            }
        )
    }

    if (showAddExamDialog) {
        AddExamDialog(
            courses = courses,
            onDismissRequest = { showAddExamDialog = false },
            onSaveExam = { exam, courseCode ->
                examsViewModel.addExam(exam, courseCode)
            }
        )
    }
}

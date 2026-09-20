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
import kotlinx.coroutines.launch
import org.corin.lectrium.components.navigation.NavBar
import org.corin.lectrium.model.NavBarStyle
import org.corin.lectrium.model.ScreenDestinations
import org.corin.lectrium.model.UserProfile
import org.corin.lectrium.preferences.ClassReminderData
import org.corin.lectrium.preferences.NavBarPreferences
import org.corin.lectrium.preferences.ThemePreferences
import org.corin.lectrium.preferences.UserProfilePreferences
import org.corin.lectrium.screens.AttendanceScreen
import org.corin.lectrium.screens.HomeScreen
import org.corin.lectrium.screens.ManageProfileScreen
import org.corin.lectrium.screens.OnBoardingScreen
import org.corin.lectrium.screens.SettingsScreen
import org.corin.lectrium.ui.theme.LectriumTheme
import org.corin.lectrium.viewmodel.ClassReminderViewModel
import org.corin.lectrium.viewmodel.NavBarViewModel
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
            val userProfile by userProfilePreferences.userProfile.collectAsStateWithLifecycle(initialValue = UserProfile())
            val scope = rememberCoroutineScope()
            var isTestingOnboarding by remember { mutableStateOf(false) }

            LectriumTheme(
                themeMode = themeMode,
                colorSchemeMode = colorSchemeMode,
                customColorHex = customColorHex
            ) {
                val reminderData = remember { ClassReminderData(applicationContext) }
                val reminderViewModel = remember { ClassReminderViewModel(reminderData) }

                val navBarPreferences = remember { NavBarPreferences(applicationContext) }
                val navBarViewModel = remember { NavBarViewModel(navBarPreferences) }

                val showOnboarding = !isOnboardingCompleted || isTestingOnboarding

                AnimatedContent(
                    targetState = showOnboarding,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.98f, animationSpec = tween(300)))
                            .togetherWith(fadeOut(animationSpec = tween(250)))
                    },
                    label = "OnboardingTransition"
                ) { displayingOnboarding ->
                    if (displayingOnboarding) {
                        OnBoardingScreen(
                            onSaveProfile = { name, email, age, university, program, yearLevel, studentStatus ->
                                scope.launch {
                                    userProfilePreferences.updateUserProfile(
                                        profileName = name,
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
                                isTestingOnboarding = false
                            }
                        )
                    } else {
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
    var currentScreenDestination by remember { mutableStateOf(ScreenDestinations.HOME) }
    var isManagingProfile by remember { mutableStateOf(false) }

    val currentNavBarStyle by (navBarViewModel?.navBarStyle?.collectAsStateWithLifecycle()
        ?: remember { mutableStateOf(NavBarStyle.BOTTOM) })

    val isFloating = currentNavBarStyle == NavBarStyle.FLOATING

    AnimatedContent(
        targetState = isManagingProfile,
        transitionSpec = {
            (fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.98f, animationSpec = tween(220)))
                .togetherWith(fadeOut(animationSpec = tween(180)))
        },
        label = "ManageProfileSubScreenTransition"
    ) { managingProfile ->
        if (managingProfile) {
            ManageProfileScreen(
                userProfilePreferences = userProfilePreferences,
                currentProfile = userProfile,
                onBack = { isManagingProfile = false }
            )
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    if (!isFloating) {
                        NavBar(
                            currentScreenDestination = currentScreenDestination,
                            onScreenDestinationSelected = { selectedDestination ->
                                currentScreenDestination = selectedDestination
                            },
                            navBarStyle = NavBarStyle.BOTTOM
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = innerPadding.calculateTopPadding(),
                                bottom = if (isFloating) innerPadding.calculateBottomPadding() + 96.dp else innerPadding.calculateBottomPadding()
                            )
                    ) {
                        AnimatedContent(
                            targetState = currentScreenDestination,
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.98f, animationSpec = tween(220)))
                                    .togetherWith(fadeOut(animationSpec = tween(180)))
                            },
                            label = "ScreenTransition"
                        ) { targetDestination ->
                            when (targetDestination) {
                                ScreenDestinations.HOME -> HomeScreen()
                                ScreenDestinations.ATTENDANCE -> AttendanceScreen()
                                ScreenDestinations.SETTINGS -> SettingsScreen(
                                    classReminderViewModel = classReminderViewModel,
                                    navBarViewModel = navBarViewModel,
                                    themeViewModel = themeViewModel,
                                    currentNavBarStyle = currentNavBarStyle,
                                    onNavBarStyleChange = { newStyle ->
                                        navBarViewModel?.onNavBarStyleSelected(newStyle)
                                    },
                                    onNavigateToManageProfile = {
                                        isManagingProfile = true
                                    }
                                )
                            }
                        }
                    }

                    if (isFloating) {
                        NavBar(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            currentScreenDestination = currentScreenDestination,
                            onScreenDestinationSelected = { selectedDestination ->
                                currentScreenDestination = selectedDestination
                            },
                            navBarStyle = NavBarStyle.FLOATING
                        )
                    }
                }
            }
        }
    }
}

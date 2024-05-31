package sk.vmproject.run.presentation.active_run

import android.Manifest
import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachReversed
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import org.koin.androidx.compose.koinViewModel
import sk.vmproject.core.presentation.designsystem.StartIcon
import sk.vmproject.core.presentation.designsystem.StopIcon
import sk.vmproject.core.presentation.designsystem.VirtualOCRTheme
import sk.vmproject.core.presentation.designsystem.VirtualOcrBlack
import sk.vmproject.core.presentation.designsystem.VirtualOcrGray
import sk.vmproject.core.presentation.designsystem.components.ObstacleItem
import sk.vmproject.core.presentation.designsystem.components.VirtualOcrDialog
import sk.vmproject.core.presentation.designsystem.components.VirtualOcrScaffold
import sk.vmproject.core.presentation.designsystem.components.VirtualOcrToolbar
import sk.vmproject.core.presentation.designsystem.components.utils.ObstaclesFormatter
import sk.vmproject.core.presentation.ui.ObserveAsEvents
import sk.vmproject.core.presentation.ui.formatted
import sk.vmproject.run.presentation.R
import sk.vmproject.run.presentation.active_run.components.AccessLocationTextProvider
import sk.vmproject.run.presentation.active_run.components.ActiveRunDataCard
import sk.vmproject.run.presentation.active_run.components.NotificationPermissionTextProvider
import sk.vmproject.run.presentation.active_run.components.PermissionDialog
import sk.vmproject.run.presentation.active_run.components.VirtualOcrFloatingActionButton
import sk.vmproject.run.presentation.active_run.maps.TrackerMap
import sk.vmproject.run.presentation.active_run.service.ActiveRunService
import sk.vmproject.run.presentation.util.hasLocationPermission
import sk.vmproject.run.presentation.util.hasNotificationPermission
import sk.vmproject.run.presentation.util.openAppSetting

@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel(),
    requestPermissionsViewModel: RequestPermissionsViewModel = koinViewModel(),
    onBack: () -> Unit,
    onFinish: () -> Unit,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
) {
    val context = LocalContext.current
    val activity = context as Activity

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is ActiveRunEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            ActiveRunEvent.RunSaved -> onFinish()
        }
    }


    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        if (Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.POST_NOTIFICATIONS
        } else {
            return
        }
    )

    val dialogQueue = requestPermissionsViewModel.visiblePermissionDialogQueue

    val multiplePermissionResulLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionsToRequest.forEach { permission ->
                requestPermissionsViewModel.onPermissionResult(
                    permission = permission,
                    isGranted = permissions[permission] == true
                )
            }

            val hasCourseLocationPermission =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            val hasFineLocationPermission =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            if (Build.VERSION.SDK_INT >= 33) {
                val hasPostNotificationsPermission =
                    permissions[Manifest.permission.POST_NOTIFICATIONS] == true
                if (hasFineLocationPermission && hasCourseLocationPermission && hasPostNotificationsPermission) {
                    viewModel.onAction(ActiveRunAction.OnAcceptPermission)
                }
            } else {
                if (hasFineLocationPermission && hasCourseLocationPermission) {
                    viewModel.onAction(ActiveRunAction.OnAcceptPermission)
                }
            }
        }
    )

    LaunchedEffect(key1 = true) {
        multiplePermissionResulLauncher.launch(permissionsToRequest)
    }

    dialogQueue.fastForEachReversed { permission ->
        PermissionDialog(
            permissionTextProvider = when (permission) {
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    AccessLocationTextProvider()
                }

                Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    AccessLocationTextProvider()
                }

                Manifest.permission.POST_NOTIFICATIONS -> {
                    NotificationPermissionTextProvider()
                }

                else -> return@fastForEachReversed
            },
            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                activity,
                permission
            ),
            onDismiss = requestPermissionsViewModel::dismissDialog,
            onConfirm = {
                requestPermissionsViewModel.dismissDialog()
                multiplePermissionResulLauncher.launch(permissionsToRequest)
            },
            onGoToAppSettings = {
                requestPermissionsViewModel.onGoSettingsDismissDialog()
                activity.openAppSetting()
            }
        )
    }

    ActiveRunScreen(
        state = viewModel.state,
        onServiceToggle = onServiceToggle,
        onAction = { action ->
            when (action) {
                is ActiveRunAction.OnToggleRunClick -> {
                    if (viewModel.hasPermissionState.value) {
                        viewModel.onAction(action)
                    } else {
                        multiplePermissionResulLauncher.launch(permissionsToRequest)
                    }
                }

                is ActiveRunAction.OnBackClick -> {
                    if (!viewModel.state.hasStartedRunning) {
                        onBack()
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun ActiveRunScreen(
    state: ActiveRunState,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.shouldTrack) {
        if (context.hasLocationPermission() && context.hasNotificationPermission() && state.shouldTrack && !ActiveRunService.isServiceActive) {
            onServiceToggle(true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        VirtualOcrScaffold(
            withGradient = false,
            topAppBar = {
                VirtualOcrToolbar(
                    showBackButton = true,
                    title = "",
                    onBackClick = {
                        onAction(ActiveRunAction.OnBackClick)
                    }
                )
            },
            floatingActionButton = {
                VirtualOcrFloatingActionButton(
                    icon = if (state.shouldTrack) {
                        StopIcon
                    } else {
                        StartIcon
                    },
                    onClick = {
                        onAction(ActiveRunAction.OnToggleRunClick)
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                TrackerMap(
                    currentLocation = state.currentLocation,
                    locations = state.runData.locations,
                    modifier = Modifier
                        .fillMaxSize()
                )

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .padding(horizontal = 16.dp)
                        .align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.runWithObstacles?.let { runWithObstacles ->
                        items(
                            items = runWithObstacles.obstacles,
                            key = { it.obstacleId }
                        ) { obstacle ->
                            ObstacleItem(
                                ordinalNumber = obstacle.ordinalNumber,
                                obstacleDescription = ObstaclesFormatter.getObstacleTypeWithNumberOfRepsText(
                                    numberOfReps = obstacle.numberOfReps,
                                    obstacleType = obstacle.obstacleType
                                ),
                                distanceFromStart = ObstaclesFormatter.getDistanceInKilometersText(
                                    distanceInMeters = obstacle.distanceFromStartInMeters
                                ),
                                obstacleReachTime = obstacle.obstacleReachTimeStamp?.formatted()
                            )
                        }
                        //spacer because the last item was covered by floating action button
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }

                ActiveRunDataCard(
                    elapsedTime = state.elapsedTime,
                    runData = state.runData,
                    runWithObstacles = state.runWithObstacles,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth(0.95f)
                )

                if (state.isSavingRun) {
                    CircularProgressIndicator()
                }
            }

        }

        if (!state.shouldTrack && state.hasStartedRunning) {
            VirtualOcrDialog(
                title = stringResource(id = R.string.running_is_paused),
                onDismiss = {
                    onAction(ActiveRunAction.OnResumeRunClick)
                },
                description = stringResource(id = R.string.resume_or_finish_run),
                primaryButton = {
                    OutlinedButton(
                        onClick = { onAction(ActiveRunAction.OnResumeRunClick) },
                        shape = RoundedCornerShape(100f),
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.resume),
                            fontWeight = FontWeight.Medium
                        )
                    }

                },
                secondaryButton = {
                    Button(
                        onClick = {
                            onAction(ActiveRunAction.OnFinishRunClick)
                            onServiceToggle(false)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = VirtualOcrGray,
                            disabledContentColor = VirtualOcrBlack
                        ),
                        shape = RoundedCornerShape(100f),
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.finish),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            )
        }

        //replace Text with dialog and navigate to solve the issue
        if (state.showTTSInitErrorDialog == true) {
            Text(text = "TTS ERROR INIT")
        }
        if (state.showTTSLanguageErrorDialog == true) {
            Text(text = "TTS ENGLISH LANGUAGE UNAVAILABLE")
        }
        //********************************************

        AnimatedVisibility(
            visible = state.countDownLabel != null,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            state.countDownLabel?.let {
                Text(
                    text = it,
                    fontSize = 130.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Preview
@Composable
private fun ActiveRunScreenPreview() {
    VirtualOCRTheme {
        ActiveRunScreen(
            state = ActiveRunState(),
            onAction = {},
            onServiceToggle = {}
        )
    }
}
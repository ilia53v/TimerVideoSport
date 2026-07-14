package com.sport.timervideosport.android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.sport.timervideosport.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Cameraswitch


class MainActivity : ComponentActivity() {

    private lateinit var recorder: VideoRecorderAndroidWrapper

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) finish()
        }

    private val audioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recorder = VideoRecorderAndroidWrapper(this)

        if (!hasCameraPermission())
            permissionLauncher.launch(Manifest.permission.CAMERA)

        if (!hasAudioPermission()) {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        setContent {
            AppTheme  {
                AppScreen(recorder)
            }
        }
    }
}


@Composable
fun AppScreen(recorder: VideoRecorderAndroidWrapper) {

    var textColor by remember {
        mutableStateOf(Color(0xFF00E5FF))
    }

    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    val activity = LocalContext.current as ComponentActivity

    // ---- Timer ----
    var timerSeconds by remember { mutableStateOf(5) }
    var remaining by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val soundPlayer = remember {
        SoundPlayer(context)
    }

    var selectedSound by remember {
        mutableStateOf(Sounds.BEEP)
    }

    var selectedQuality by remember {
        mutableStateOf(VideoQuality.SD)
    }

    var lensFacing by remember {
        mutableStateOf(CameraSelector.LENS_FACING_BACK)
    }

    LaunchedEffect(
        selectedQuality,
        lensFacing,
        previewView
    ) {

        previewView?.let {
            startCamera(
                activity = activity,
                previewView = it,
                recorder = recorder,
                quality = selectedQuality.quality,
                lensFacing = lensFacing
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            soundPlayer.release()
        }
    }

    // ---- Countdown ----
    LaunchedEffect(isRunning) {
        if (!isRunning) return@LaunchedEffect

        while (remaining > 0) {
            delay(1000)
            remaining--
        }

        if (remaining == 0) {
            isRunning = false
            activity.lifecycleScope.launch {
                recorder.stop()
            }
            soundPlayer.play(selectedSound)
        }
    }

    Box(Modifier.fillMaxSize()) {

        // ---------- Camera preview ----------
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    post {
                        previewView = this
                        startCamera(
                            activity = activity,
                            previewView = this,
                            recorder = recorder,
                            quality = selectedQuality.quality,
                            lensFacing = lensFacing

                        )
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // ---------- Timer setup ----------
        Column(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Row(Modifier
                .fillMaxWidth()
                .height(75.dp)
            ){
                Text(text = "Таймер: ${formatTime(timerSeconds)}",
                    color = textColor,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp))

                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "Звук:",
                    softWrap = true,
                    maxLines = 2,
                    lineHeight = 10.sp,
                    color = textColor,//MaterialTheme.colorScheme.primary,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
                SoundDropdown(
                    selected = selectedSound,
                    onSelected = { selectedSound = it },
                    soundPlayer = soundPlayer,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 10.dp),
                    color = textColor
                )
                Row {
                    ColorPickerButton(
                        selectedColor = textColor,
                        onColorSelected = {
                            textColor = it
                        }
                    )
                }
            }

            Slider(
                value = timerSeconds.toFloat(),
                onValueChange = { timerSeconds = it.toInt() },
                valueRange = 1f..360f,
                colors = SliderColors(
                    textColor,
                    textColor,
                    textColor,
                    textColor,
                    textColor,
                    textColor,
                    textColor,
                    textColor,
                    textColor,
                    textColor
                )
            )
        }

        // ---------- Timer overlay ----------
        if (isRunning) {
            Text(
                text = formatTime(remaining),
                style = MaterialTheme.typography.displayMedium,
                color = textColor,//MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 8.dp)
            )
        }

        // ---------- Buttons quality ----------
        Row(Modifier
            .align(Alignment.BottomStart)
            .padding(top = 0.dp,
                end = 16.dp,
                start = 16.dp,
                bottom = 16.dp)
        ){
            Button(
                onClick = {
                    lensFacing =
                        if (lensFacing == CameraSelector.LENS_FACING_BACK)
                            CameraSelector.LENS_FACING_FRONT
                        else
                            CameraSelector.LENS_FACING_BACK
                },
                modifier = Modifier.size(70.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonColors(
                    textColor,
                    textColor,
                    textColor,
                    textColor
                )
            ){
                Icon(
                    imageVector = (Icons.Filled.Cameraswitch),
                    tint = Color.White,
                    contentDescription = "Сменить камеру",
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // ---------- Buttons quality ----------
        Row(Modifier
            .align(Alignment.BottomEnd)
            .padding(top = 0.dp,
                end = 16.dp,
                start = 16.dp,
                bottom = 30.dp)
        ){
            VideoQualitySelector(
                selected = selectedQuality,
                onSelected = {
                    selectedQuality = it
                },
                accentColor = textColor
            )
        }

        // ---------- Controls ----------
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .width(110.dp)
                .height(110.dp)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                if(isRunning == false) {
                    remaining = timerSeconds
                    isRunning = true
                    val enableAudio = context.hasAudioPermission()
                    activity.lifecycleScope.launch {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            recorder.start(enableAudio)
                        }
                    }
                }else{
                    isRunning = false
                    activity.lifecycleScope.launch {
                        recorder.stop()
                    }
                    soundPlayer.stop()
                }

            }, modifier = Modifier
                .width(100.dp)
                .height(100.dp),
                colors = IconButtonColors(containerColor = textColor,//MaterialTheme.colorScheme.primary,
                    disabledContainerColor = textColor,//MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    disabledContentColor = Color.White)
            ) {

                Icon(
                    painter =
                        painterResource(
                            if (!isRunning)
                                R.drawable.play_button
                            else
                                R.drawable.stop_button
                        ),
                    contentDescription = "Запись/Стоп",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 10.dp)
                )
            }
        }
    }
}

// ----------------------------------------------------------------------
// Start camera
// ----------------------------------------------------------------------
private fun startCamera(
    activity: ComponentActivity,
    previewView: PreviewView,
    recorder: VideoRecorderAndroidWrapper,
    quality: Quality,
    lensFacing: Int
) {

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    val future =
        ProcessCameraProvider.getInstance(activity)

    future.addListener({

        val provider = future.get()

        recorder.buildVideoCapture(quality)

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        provider.unbindAll()

        provider.bindToLifecycle(
            activity,
            cameraSelector,
            preview,
            recorder.videoCapture
        )

    }, ContextCompat.getMainExecutor(activity))
}
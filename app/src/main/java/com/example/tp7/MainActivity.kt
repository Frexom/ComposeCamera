package com.example.tp7

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.get
import com.example.tp7.ui.theme.TP7Theme

class MainActivity : ComponentActivity() {

    enum class State { Unknown, Allowed, Disallowed }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state = mutableStateOf<State>(State.Unknown)
        val res = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result: Boolean ->
            if (result) state.value = State.Allowed else state.value = State.Disallowed
        }

        setContent {
            TP7Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color.Red
                ) {
                    Column() {
                        if (state.value == State.Allowed) {
                            Text("Camera launched")
                            Camera(Modifier.fillMaxSize())
                        } else {
                            res.launch(Manifest.permission.CAMERA)
                        }
                    }

                }
            }
        }

    }

    @Composable
    fun Camera(modifier: Modifier = Modifier) {
        val cameraController = LifecycleCameraController(applicationContext)
        val context = LocalLifecycleOwner.current
        AndroidView(modifier = modifier, factory = { context: Context ->
            val previewView: PreviewView = PreviewView(context)
            previewView
        }, update = {
            cameraController.bindToLifecycle(context)
            it.controller = cameraController

        })

    }


    @Composable
    fun Greeting(state: MutableState<MainActivity.State>, res: ActivityResultLauncher<String>) {
        if (state.value == MainActivity.State.Allowed) {
            Camera()
        } else {
            res.launch(Manifest.permission.CAMERA)
        }
    }
}


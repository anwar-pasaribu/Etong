package com.unwur.etong

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Firebase.initialize(this)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = getColor(R.color.etongPrimary),
                darkScrim = getColor(R.color.etongPrimaryDark)
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}
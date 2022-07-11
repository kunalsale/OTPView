package com.ksale.otpview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ksale.otpview.ui.otpview.OtpView
import com.ksale.otpview.ui.theme.OTPViewTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OTPViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    color = MaterialTheme.colors.background
                ) {
                    var otpStr by remember {
                        mutableStateOf("")
                    }
                    var isError by remember {
                        mutableStateOf(false)
                    }
                    OtpView (
                        otpStr = otpStr,
                        isError = isError
                    ) { otp ->
                        otpStr = otp
                        if (otpStr.length == 6) {
                            // Completely filled
                            isError = otpStr != "123456"
                            if (isError) {
                                otpStr = ""
                            }
                        }

                        if (isError && otpStr.isNotEmpty()) {
                            isError= false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OTPViewTheme {
        Greeting("Android")
    }
}
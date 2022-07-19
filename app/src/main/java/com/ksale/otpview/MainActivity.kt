package com.ksale.otpview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ksale.otpview.ui.otpview.OtpView
import com.ksale.otpview.ui.otpview.shapeoptions.PinShape
import com.ksale.otpview.ui.theme.OTPViewTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OTPViewTheme {
                SampleScreen()
            }
        }
    }
}

@Preview
@Composable
fun SampleScreen() {
    // A surface container using the 'background' color from the theme
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = MaterialTheme.colors.primary),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "OTP View",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(32.dp))

            RoundRectOtpView() // <- OTP view

            Spacer(modifier = Modifier.size(32.dp))

            LineOtpView()  // <- OTP view

            Spacer(modifier = Modifier.size(32.dp))

            CircleOtpView() // <- OTP view
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LineOtpView(
    shape: PinShape = PinShape.LineShape(5f)
) {
    var otpStr by remember {
        mutableStateOf("")
    }
    var isError by remember {
        mutableStateOf(false)
    }
    OtpView(
        otpStr = otpStr,
        isError = isError,
        filledColor = Color.Green,
        focusColor = Color.Blue,
        cursorColor = Color.Blue,
        shape = shape,
        textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 22.sp)
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
            isError = false
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RoundRectOtpView() {
    var otpStr by remember {
        mutableStateOf("")
    }
    var isError by remember {
        mutableStateOf(false)
    }
    OtpView(
        otpStr = otpStr,
        isError = isError,
        shape = PinShape.RectangularShape(25f),
        textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 22.sp)
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
            isError = false
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CircleOtpView() {
    var otpStr by remember {
        mutableStateOf("")
    }
    var isError by remember {
        mutableStateOf(false)
    }
    OtpView(
        modifier = Modifier.width(240.dp),
        otpStr = otpStr,
        isError = isError,
        shape = PinShape.CircleShape,
        numberOfFields = 4
    ) { otp ->
        otpStr = otp
        if (otpStr.length == 4) {
            // Completely filled
            isError = otpStr != "1234"
            if (isError) {
                otpStr = ""
            }
        }

        if (isError && otpStr.isNotEmpty()) {
            isError = false
        }
    }
}
package com.ksale.otpview.ui.otpview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ksale.otpview.ui.otpview.shapeoptions.PinShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun OtpView(
    modifier: Modifier = Modifier,
    numberOfFields: Int = 6,
    singlePinWidth: Dp = 50.dp,
    pinHeight: Dp = 50.dp,
    isCursorEnabled: Boolean = true,
    defaultColor: Color = Color.Gray,
    focusColor: Color = Color.Yellow,
    filledColor: Color = Color.Yellow,
    cursorColor: Color = Color.Yellow,
    errorColor: Color = Color.Red,
    drawStyle: DrawStyle = Stroke(width = 10f),
    shape: PinShape = PinShape.LineShape(10f),
    textStyle: androidx.compose.ui.text.TextStyle = androidx.compose.ui.text.TextStyle(
        color = Color.Black,
        fontSize = 18.sp,
    ),
    onFilled: (otp: String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val otpLength = remember { numberOfFields }
    var isError by remember { mutableStateOf(false) }
    var isFocus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    OtpView(
        modifier = modifier.height(height = pinHeight),
        singlePinWidth = singlePinWidth,
        text = text,
        textStyle = textStyle,
        shape = shape,
        isFocus = isFocus,
        isCursorEnabled = isCursorEnabled,
        isError = isError,
        drawStyle = drawStyle,
        defaultColor = defaultColor,
        focusColor = focusColor,
        filledColor = filledColor,
        cursorColor = cursorColor,
        errorColor = errorColor,
        onValueChange = { value ->
            text = value
            isError = if (text.length == numberOfFields) {
                text != "123456"
            } else {
                false
            }
        },
        onFocusChanged = {
            isFocus = it
        },
        onClick = {
            focusRequester.requestFocus()
            keyboard?.show()
        },
        numberOfFields = otpLength,
        focusRequester = focusRequester
    )
}

@ExperimentalComposeUiApi
@Composable
private fun OtpView(
    modifier: Modifier,
    numberOfFields: Int,
    singlePinWidth: Dp,
    text: String,
    textStyle: androidx.compose.ui.text.TextStyle,
    isError: Boolean,
    isFocus: Boolean,
    isCursorEnabled: Boolean,
    defaultColor: Color,
    focusColor: Color,
    filledColor: Color,
    cursorColor: Color,
    errorColor: Color,
    shape: PinShape,
    drawStyle: DrawStyle,
    focusRequester: FocusRequester,
    onFocusChanged: (hasFocus: Boolean) -> Unit,
    onClick: (index: Int) -> Unit,
    onValueChange: (String) -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        TextField(
            value = text,
            onValueChange = {
                if (it.length <= numberOfFields) {
                    onValueChange(it)
                }
            },
            modifier = Modifier
                .size(0.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    onFocusChanged(it.isFocused)
                },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(

            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val nextFocusIndex = text.length
            for (i in 0 until numberOfFields) {
                PinField(
                    modifier = Modifier,
                    width = singlePinWidth,
                    digit = text.getOrNull(i)?.toString() ?: "",
                    textStyle = textStyle,
                    shape = shape,
                    isFocus = isFocus,
                    isFilled = text.getOrNull(i) != null,
                    isCurrentPinFocused = nextFocusIndex == i,
                    isCursorEnabled = isCursorEnabled,
                    isError = isError,
                    defaultColor = defaultColor,
                    focusColor = focusColor,
                    filledColor = filledColor,
                    cursorColor = cursorColor,
                    errorColor = errorColor,
                    drawStyle = drawStyle,
                    index = i,
                    onClick = onClick,
                )
            }
        }
    }
}

@Composable
fun PinField(
    modifier: Modifier = Modifier,
    width: Dp,
    index: Int,
    digit: String,
    textStyle: androidx.compose.ui.text.TextStyle,
    isFocus: Boolean,
    isFilled: Boolean,
    isCurrentPinFocused: Boolean,
    isCursorEnabled: Boolean,
    isError: Boolean,
    defaultColor: Color,
    focusColor: Color,
    filledColor: Color,
    cursorColor: Color,
    errorColor: Color,
    shape: PinShape,
    drawStyle: DrawStyle,
    onClick: (index: Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

    if (isFocus && isCurrentPinFocused && !isFilled) {
        LaunchedEffect(key1 = cursorSymbol, true) {
            scope.launch {
                delay(500)
                setCursorSymbol(if (cursorSymbol.isEmpty()) "|" else "")
            }
        }
    }
    Box(
        modifier = modifier
            .width(width)
            .fillMaxHeight()
            .drawBehind {
                drawPath(
                    path = shape.drawShape(this.size),
                    color = if (isError) errorColor else if (isFilled) filledColor else if (isFocus && isCurrentPinFocused) focusColor else defaultColor,
                    style = drawStyle
                )
            }
            .clickable {
                onClick(index)
            },
        contentAlignment = Alignment.Center
    ) {
        if (isCursorEnabled && digit == ""  && (isFocus && isCurrentPinFocused && !isFilled)) {
            Text(
                text = cursorSymbol,
                color = cursorColor,
                style = textStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentSize()
            )
        }
        Text(
            text = digit,
            style = textStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentSize()
        )
    }
}
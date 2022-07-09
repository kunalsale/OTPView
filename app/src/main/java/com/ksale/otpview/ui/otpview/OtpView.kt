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

/**
 * OtpView is a composable function which is exposed to usage by external classes.
 * It will internally call private OtpView to draw the view
 *
 * @param modifier: Modifier that can be passed for configuration
 * @param numberOfFields: Number of pins to be needed in the otp
 * @param singlePinWidth: width of single pin
 * @param isCursorEnabled: Is cursor needed to be displayed
 * @param defaultColor: Default color for the pin shape, when the pin is not focused
 * or filled.
 * @param filledColor: Color for shape outline when it is filled with digit.
 * @param cursorColor: Color of the cursor
 * @param errorColor: Color for the pin outline when the otp entered is incorrect
 * @param drawStyle: [DrawStyle] for the shape
 * @param shape: It is custom shapes created for the pins [PinShape]
 * @param textStyle: Style to be applied on text
 * @param onFilled: Lambda when the otp is filled with the given length.
 */
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

/**
 * Internal composable to draw the view.
 *
 * @param text: Text of the otp For eg: "123", "123456"
 * @param isError: Boolean which tells whether the otp entered is incorrect
 * @param isFocus: Whether the otp is currently focused
 * @param isCursorEnabled: To show the cursor or not
 * @param focusRequester: [FocusRequester] to control the focus of the TextField
 * @param onFocusChanged: Called when the focus of the TextField is changed
 * @param onClick: Called when otp view is clicked
 * @param onValueChange: Lambda for passing the value changes in the OTP
 *
 */
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

/**
 * It is composable for displaying an individual pin of the OtpView
 *
 * @param modifier: Modifier for the Pin
 * @param width: Width of the pin
 * @param index: index of the pin
 * @param digit: Digit in the particular pin
 * @param textStyle: Style to be applied on the digit
 */
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
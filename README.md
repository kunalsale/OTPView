# OTPView
OTPView is a highly costumizable OTP view made in the Jetpack compose UI. 

<img src=https://user-images.githubusercontent.com/31345204/179820889-fa3264fe-8a40-44aa-bea0-34c953ef1f10.png width=200>   <img src=https://user-images.githubusercontent.com/31345204/179821505-ae97e4a7-8369-49ea-b2f1-db59ae79150e.png width=200> 

#### Usage:
```
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
```

## Courtesy:

1. Idea of using single TextField and displaying different view for the pins:
https://github.com/ch8n/Compose-OtpView

2. Custom shapes 
https://juliensalvi.medium.com/custom-shape-with-jetpack-compose-1cb48a991d42





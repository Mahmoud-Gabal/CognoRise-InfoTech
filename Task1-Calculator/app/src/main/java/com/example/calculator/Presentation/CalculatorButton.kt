package com.example.calculator.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.LightGray

@Preview(showBackground = true)
@Composable
fun CalculatorButton(
    modifier: Modifier = Modifier,
    input: String = "Ac",
    color: Color = LightGray,
    onClick : () -> Unit = {}
) {

    Button(
        onClick = onClick ,
        modifier = modifier.size(90.dp).clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        Text(
            text = input,
            color = Color.White,
            fontSize =29.sp

        )
    }

}
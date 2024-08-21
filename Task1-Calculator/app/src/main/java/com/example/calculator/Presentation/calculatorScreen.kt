package com.example.calculator.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.Domain.CalculationViewModel
import com.example.calculator.R
import com.example.calculator.ui.theme.LightGray
import com.example.calculator.ui.theme.MediumGray
import com.example.calculator.ui.theme.Orange
import java.util.Stack


@Preview(showBackground = true)
@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues()
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .padding(paddingValues = padding)
    ) {
        val calcModel: CalculationViewModel = viewModel()
        val exp = calcModel.expression.value
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Text(
                text = exp,
                fontSize =
                if (exp.length < 40) 60.sp
                else if (exp.length < 96) 40.sp
                else 35.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                textAlign = TextAlign.End,
                lineHeight = if (exp.length < 40) 60.sp
                else if (exp.length < 96) 40.sp
                else 35.sp
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalculatorButton(
                    input = "AC",
                    modifier = Modifier.width(185.dp),
                    color = LightGray,
                    onClick = {
                        calcModel.clearExpression()
                    }
                )
                CalculatorButton(
                    input = "Del",
                    modifier = Modifier,
                    color = LightGray,
                    onClick = {
                        if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                            calcModel.clearExpression()
                        }
                        calcModel.deleteFromExpression()
                    }
                )
                CalculatorButton(
                    input = "/",
                    modifier = Modifier,
                    color = Orange,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("/")

                        }
                    }
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalculatorButton(
                    input = "7",
                    modifier = Modifier,
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("7")
                        }
                    }
                )
                CalculatorButton(
                    input = "8",
                    modifier = Modifier,
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("8")
                        }
                    }
                )
                CalculatorButton(
                    input = "9",
                    modifier = Modifier,
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("9")
                        }
                    }
                )
                CalculatorButton(
                    input = "x",
                    modifier = Modifier,
                    color = Orange,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("*")
                        }
                    }
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalculatorButton(
                    input = "4",
                    modifier = Modifier,
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("4")
                        }
                    }
                )
                CalculatorButton(
                    input = "5",
                    modifier = Modifier,
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("5")
                        }
                    }
                )
                CalculatorButton(
                    input = "6",
                    modifier = Modifier,
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("6")
                        }
                    }
                )
                CalculatorButton(
                    input = "-",
                    modifier = Modifier,
                    color = Orange,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("-")
                        }
                    }
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalculatorButton(
                    input = "1",
                    modifier = Modifier,
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("1")
                        }
                    }
                )
                CalculatorButton(
                    input = "2",
                    modifier = Modifier,
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("2")
                        }
                    }
                )
                CalculatorButton(
                    input = "3",
                    modifier = Modifier,
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("3")
                        }
                    }
                )
                CalculatorButton(
                    input = "+",
                    modifier = Modifier,
                    color = Orange,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("+")
                        }
                    }
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalculatorButton(
                    input = "0",
                    modifier = Modifier.width(185.dp),
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !"|| exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression("0")
                        }
                    }
                )
                CalculatorButton(
                    input = ".",
                    modifier = Modifier,
                    color = MediumGray,
                    onClick = {
                        if (exp.length < 100) {
                            if (exp == "Infinity" || exp == "Invalid Syntax !" || exp == "null"){
                                calcModel.clearExpression()
                            }
                            calcModel.updateExpression(".")
                        }
                    }
                )
                CalculatorButton(
                    input = "=",
                    modifier = Modifier,
                    color = Orange,
                    onClick = {
                        if (!exp.isEmpty()&&exp != "Infinity" && exp != "Invalid Syntax !"&& exp != "null"){
                            if (calcModel.isValid()) {
                                calcModel.EvaluationOfInfix()
                            }else{
                                calcModel.clearExpression()
                                calcModel.updateExpression("Invalid Syntax !")
                            }
                        }
                    }
                )
            }
        }
    }

}
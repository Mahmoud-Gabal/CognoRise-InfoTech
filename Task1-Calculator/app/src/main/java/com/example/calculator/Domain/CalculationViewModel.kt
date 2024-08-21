package com.example.calculator.Domain

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.calculator.Domain.DataStructure.stack
import java.util.Stack
import kotlin.math.exp

class CalculationViewModel(

) : ViewModel() {


    private val _expression = mutableStateOf("")
    val expression: State<String> = _expression

    fun isValid(): Boolean {
        val rg1 = Regex("[+\\-*/]+[/*]+")  // is containing
        val rg0 = Regex("[.]{2}")  // is containing
        val rg2 = Regex("[+\\-*/]*[0-9]*[.]*[0-9]*[+\\-*/]+")  // is ending with
        val rg3 = Regex("[+\\-]*[/*]+[+\\-]*[0-9]+")  // is starting with
        val last_rg2_in_exp = rg2.findAll(_expression.value).toList().map { it.value }.lastOrNull()
        val first_rg3_in_exp = rg3.find(_expression.value)?.value
        return if (!_expression.value.contains(rg0) && !_expression.value.contains(rg1) && !_expression.value.endsWith(
                last_rg2_in_exp.toString()
            ) && !_expression.value.startsWith(
                first_rg3_in_exp.toString()
            )
        ) {
            true
        } else {
            false
        }
    }

    fun updateExpression(AddedItem: String) {
        _expression.value += AddedItem
    }

    fun clearExpression() {
        _expression.value = ""
    }

    fun deleteFromExpression() {
        _expression.value = _expression.value.dropLast(1)
    }

    fun InfixToPostfix(exp: String): String {
        val stack = stack<Char>()
        var newExp = ""
        for (i in exp.indices) {
            if (isOperator(exp[i])) {
                if (exp[i] == '-' && newExp.isEmpty()) {
                    newExp += "${exp[i]}"
                    continue
                } else if (exp[i] == '-' && exp[i - 1] == '/' || exp[i] == '-' && exp[i - 1] == '*') {
                    newExp += "${exp[i]}"
                    continue
                } else if (exp[i] == '+' && newExp.isEmpty() || exp[i] == '+' && exp[i - 1] == '*') continue
                else if (exp[i] == '+' && exp[i - 1] == '/') continue
                else newExp += " "
                if (hasMorePriority(exp[i], stack.peek())) {
                    stack.push(exp[i])
                } else {
                    newExp += "${stack.pop()} "
                    if (hasMorePriority(exp[i], stack.peek()))
                        stack.push(exp[i])
                    else {
                        newExp += "${stack.pop()} "
                        stack.push(exp[i])
                    }
                }
            } else {
                newExp += "${exp[i]}"
            }
        }
        while (!stack.isEmpty()) {
            newExp += " ${stack.pop()}"
        }
        return newExp.trim()
    }

    fun isOperator(ch: Char): Boolean {
        val operatorList = listOf('*', '/', '+', '-')
        return operatorList.contains(ch)
    }

    fun hasMorePriority(first: Char, second: Char?): Boolean {
        var firstindex: Int = 0
        var secondindex: Int = 0
        when (first) {
            '*', '/' -> firstindex = 2
            '+', '-' -> firstindex = 1
        }
        when (second) {
            '*', '/' -> secondindex = 2
            '+', '-' -> secondindex = 1
            else -> secondindex = 0
        }
        return firstindex > secondindex

    }

    fun EvaluationOfInfix(): Unit {
        var expression = _expression.value
        val rg = Regex("[+-]{2}")
        while (expression.contains(rg)) {
            expression = expression.replace("++", "+")
            expression = expression.replace("+-", "-")
            expression = expression.replace("--", "+")
        }
        val rg2 = Regex("[+-]?[0-9]*[.]?[0-9]*")
        if (expression.matches(rg2)) {
            _expression.value = try {
                expression.replace("+", "").toDouble().toString()
            }catch (e : Exception){
                0.0.toString()
            }
            return
        }
        val exp = InfixToPostfix(expression)
        val stack = stack<Double>()
        var pushedNum = ""
        for (i in exp.indices) {
            if (isOperator(exp[i])) {
                if (i != exp.lastIndex) {
                    when (exp[i + 1]) {
                        ' ' -> {
                            val num2 = stack.pop()
                            val num1 = stack.pop()
                            val result = applyOperation(num1!!, num2!!, exp[i])
                            stack.push(result)
                        }

                        else -> pushedNum += exp[i]
                    }
                    continue
                }
                val num2 = stack.pop()
                val num1 = stack.pop()
                val result = applyOperation(num1!!, num2!!, exp[i])
                stack.push(result)
            } else if (exp[i] == ' ') {
                if (pushedNum.isNotEmpty()) {
                    if (pushedNum == ".") pushedNum = "0"
                    stack.push(pushedNum.toDouble())
                    pushedNum = ""
                }
            } else {
                pushedNum += exp[i]
            }
        }
        _expression.value = stack.peek().toString().replace("+", "")
    }

    fun applyOperation(num1: Double, num2: Double, operator: Char): Double {
        return when (operator) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '*' -> num1 * num2
            '/' -> num1 / num2
            else -> 0.0
        }
    }


}
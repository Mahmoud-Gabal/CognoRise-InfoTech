package com.example.calculator.Domain.DataStructure

class stack<T> {

    val elements = mutableListOf<T>()

    fun push(item: T) {
        elements.add(item)
    }

    fun pop(): T? {
        return if (elements.isEmpty()) {
            println("you can not pop from empty stack")
            null
        } else {
            elements.removeAt(elements.size - 1)
        }
    }

    fun peek(): T? {
        return elements.lastOrNull()
    }

    fun isEmpty() = elements.isEmpty()
}
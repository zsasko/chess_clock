package com.zsasko.chessclock.utils

fun String.toDigitsOrZero(): Int {
    return this.filter { it.isDigit() }.takeIf { it.isNotEmpty() }?.toInt() ?: 0
}
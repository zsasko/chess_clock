package com.zsasko.chessclock.utils

import java.util.Locale

/**
 * Formats the value in milliseconds into minutes and seconds.
 */
fun Long.formatMillis(): String {
    val totalSeconds = (this / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}


fun Int.minutesToMilliseconds(): Long {
    return this * 60_000L
}

fun Int.secondsToMilliseconds(): Long {
    return this * 1_000L
}
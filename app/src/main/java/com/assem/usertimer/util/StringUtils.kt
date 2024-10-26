package com.assem.usertimer.util

fun String.formatMacAddress(): String {
    val cleanedText = this.replace(":", "")
    return cleanedText.chunked(2).joinToString(":") { it.padEnd(2, ' ') }
        .uppercase()
        .trim()
}
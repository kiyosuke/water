package kiyosuke.com.water.util

public fun String.toIntOrString(): Any {
    return try {
        this.toInt()
    } catch (e: NumberFormatException) {
        this
    }
}
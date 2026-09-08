package com.example.callblocker

object NumberUtils {

    /**
     * Normalizes an Indian phone number/prefix for matching.
     *
     * Examples:
     * +91 1401234567 -> 1401234567
     * 911401234567   -> 1401234567
     * 140-123-4567   -> 1401234567
     *
     * This MVP treats a leading 91 on a number longer than 10 digits
     * as India's country code.
     */
    fun normalizeIndianNumber(value: String): String {
        var digits = value.filter { it.isDigit() }

        if (digits.startsWith("91") && digits.length > 10) {
            digits = digits.substring(2)
        }

        return digits
    }
}

package com.example.callblocker

import android.content.Context

object PrefixStore {
    private const val PREFS_NAME = "call_blocker_preferences"
    private const val KEY_PREFIXES = "blocked_prefixes"

    fun getPrefixes(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_PREFIXES, emptySet())?.toSet() ?: emptySet()
    }

    fun addPrefix(context: Context, prefix: String) {
        val normalized = NumberUtils.normalizeIndianNumber(prefix)
        if (normalized.isBlank()) return

        val updated = getPrefixes(context).toMutableSet()
        updated.add(normalized)

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_PREFIXES, updated)
            .apply()
    }

    fun removePrefix(context: Context, prefix: String) {
        val updated = getPrefixes(context).toMutableSet()
        updated.remove(prefix)

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_PREFIXES, updated)
            .apply()
    }
}

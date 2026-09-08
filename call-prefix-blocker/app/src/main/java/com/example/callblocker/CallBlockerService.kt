package com.example.callblocker

import android.telecom.Call
import android.telecom.CallScreeningService

class CallBlockerService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        val rawNumber = callDetails.handle?.schemeSpecificPart.orEmpty()
        val number = NumberUtils.normalizeIndianNumber(rawNumber)

        val blockedPrefixes = PrefixStore.getPrefixes(this)

        val shouldBlock = number.isNotBlank() &&
            blockedPrefixes.any { prefix -> number.startsWith(prefix) }

        val response = CallResponse.Builder()
            .setDisallowCall(shouldBlock)
            .setRejectCall(shouldBlock)
            .setSkipCallLog(false)
            .setSkipNotification(shouldBlock)
            .build()

        respondToCall(callDetails, response)
    }
}

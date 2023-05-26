package com.huhx.snowflake

class SnowFlake(private val workerId: Long) {
    private var sequence = 0L

    private var lastTimestamp = -1L

    fun getId(): Long {
        var currentTimestamp = getCurrentTimestamp()
        if (tolerateTimestampBackwardsIfNeed(currentTimestamp)) {
            currentTimestamp = getCurrentTimestamp()
        }
        if (lastTimestamp == currentTimestamp) {
            if (sequenceIncreaseIfReachLimitReset()) {
                currentTimestamp = waitUntilNextTime(currentTimestamp)
            }
        } else {
            sequence = 0L
        }
        lastTimestamp = currentTimestamp
        return (currentTimestamp - EPOCH shl TIMESTAMP_LEFT_SHIFT_BITS.toInt()
                or (workerId shl WORKER_ID_LEFT_SHIFT_BITS.toInt())
                or sequence)
    }

    private fun tolerateTimestampBackwardsIfNeed(curTimestamp: Long): Boolean {
        if (lastTimestamp <= curTimestamp) {
            return false
        }
        val timeDifference = lastTimestamp - curTimestamp
        if (timeDifference < MAX_TIMESTAMP_BACKWARDS_TO_WAIT) {
            waitUntilNextTime(lastTimestamp)
        } else {
            throw SnowflakeException()
        }
        return true
    }

    private fun sequenceIncreaseIfReachLimitReset(): Boolean {
        return 0L == sequence + 1 and SEQUENCE_MASK.also { sequence = it }
    }

    private fun waitUntilNextTime(timestampToContinue: Long): Long {
        var timestamp: Long
        do {
            timestamp = getCurrentTimestamp()
        } while (timestamp <= timestampToContinue)
        return timestamp
    }

    private fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }

    companion object {
        /**
         * timestamp start from (Wed Oct 26 2021 00:00:00 GMT+0800 (China Standard Time)).
         * DO NOT change this value!
         */
        private const val EPOCH = 1666713600000L

        /**
         * worker-id take 14 bits.
         */
        private const val WORKER_ID_BITS = 14L

        /**
         * sequence take 8 bits.
         */
        private const val SEQUENCE_BITS = 8L

        /**
         * mask for some bit operation on sequence.
         */
        private const val SEQUENCE_MASK = (1L shl SEQUENCE_BITS.toInt()) - 1

        /**
         * worker-id left-shift length.
         */
        private const val WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS

        /**
         * timestamp left-shift length.
         */
        private const val TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS

        /**
         * machine time may go backwards, if under 10 milliseconds we wait.
         */
        private const val MAX_TIMESTAMP_BACKWARDS_TO_WAIT = 10
    }
}

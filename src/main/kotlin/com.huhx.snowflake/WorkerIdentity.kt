package com.huhx.snowflake

import java.net.InetAddress
import java.net.UnknownHostException

object WorkerIdentity {
    private const val SIX_MASK = (1L shl 6) - 1

    fun getWorkerId(): Long {
        return try {
            val ips = InetAddress.getLocalHost().hostAddress.split(".")
            val subnet = ips[2].toLong()
            val machine = ips[3].toLong()
            SIX_MASK and subnet shl 8 or machine
        } catch (e: UnknownHostException) {
            throw IllegalStateException("can not get worker id", e)
        }
    }
}

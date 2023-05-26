package com.huhx.snowflake

object SnowId {
    private val snowFlake = SnowFlake(WorkerIdentity.getWorkerId())

    fun next(): Long {
        return snowFlake.getId()
    }
}

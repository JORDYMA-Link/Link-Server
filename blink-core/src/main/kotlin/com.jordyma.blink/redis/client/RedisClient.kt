package com.jordyma.blink.redis.client

interface RedisClient{

    fun set(key: String, value: String)

    fun get(key: String): String?

    fun decr(key: String): Long?

    fun incr(key: String): Long?

    fun sadd(key: String, value: String): Long?

    fun scard(key: String): Long?

    fun expire(key: String, seconds: Long): Boolean

    fun eval(script: String, keys: List<String>, args: List<String>): Any?

    fun zcount(key: String, min: Long, max: Long): Int

    fun zadd(key: String, score: Double, member: String): Long

    fun zremrangebyscore(key: String, min: Double, max: Double): Long
}
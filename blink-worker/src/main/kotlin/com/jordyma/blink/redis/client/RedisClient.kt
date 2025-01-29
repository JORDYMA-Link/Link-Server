package com.jordyma.blink.redis.client

interface RedisClient{

    fun set(key: String, value: String)

    fun get(key: String): String?

    fun decr(key: String): Long?

    // TODO implement the rest of the methods
//    fun delete(key: String)
//
//    fun exists(key: String): Boolean
//
//    fun expire(key: String, seconds: Long)
//
//    fun ttl(key: String): Long
//
//    fun setnx(key: String, value: String): Boolean
//
//    fun setex(key: String, seconds: Long, value: String)
//
//    fun psetex(key: String, milliseconds: Long, value: String)
//
//    fun setbit(key: String, offset: Long, value: Boolean): Boolean
//
//    fun getbit(key: String, offset: Long): Boolean
//
//    fun setrange(key: String, offset: Long, value: String)
//
//    fun getrange(key: String, start: Long, end: Long): String
//
//    fun mset(vararg keyValues: String)
//
//    fun mget(vararg keys: String): List<String>
//
//    fun msetnx(vararg keyValues: String): Boolean
//
//    fun incr(key: String): Long
//
//    fun incrby(key: String, increment: Long): Long
//
//    fun incrbyfloat(key: String, increment: Double): Double
//
//
//    fun decrby(key: String, decrement: Long): Long
//
//    fun append(key: String, value: String): Long
//
//    fun substr(key: String, start: Long, end: Long): String
//
//    fun hset(key: String, field: String, value: String): Boolean
//
//    fun hget(key: String, field: String): String?
//
//    fun hdel(key: String, vararg fields: String): Long
//
//    fun hexists(key: String, field: String): Boolean
//
//    fun hlen(key: String): Long
//
//    fun hkeys(key: String): Set<String>
//
//    fun hvals(key: String): List<String>
//
//    fun hgetall(key: String): Map<String, String>
//
//    fun hincrby(key: String, field: String, increment: Long): Long
//
//    fun hincrbyfloat(key: String, field: String, increment: Double): Double
//
//    fun lpush(key: String, vararg values: String): Long
//
//    fun rpush(key: String, vararg values: String): Long

}
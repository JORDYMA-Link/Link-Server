package com.jordyma.blink.global.util

import java.time.LocalDate

operator fun LocalDate.rangeTo(other: LocalDate) = LocalDateProgression(this, other)

class LocalDateProgression(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    val stepDays: Long = 1
) : Iterable<LocalDate>, ClosedRange<LocalDate> {
    override fun iterator(): Iterator<LocalDate> = LocalDateIterator(start, endInclusive, stepDays)
}

class LocalDateIterator(
    start: LocalDate,
    private val endInclusive: LocalDate,
    private val stepDays: Long
) : Iterator<LocalDate> {
    private var current = start

    override fun hasNext(): Boolean = current <= endInclusive

    override fun next(): LocalDate {
        val next = current
        current = current.plusDays(stepDays)
        return next
    }
}
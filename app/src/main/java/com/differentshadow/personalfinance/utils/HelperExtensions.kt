package com.differentshadow.personalfinance.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAdjusters
import java.util.Currency

fun Long.toDate(): LocalDate {
    return LocalDate.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

fun Long.toDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

fun Long.toDateString(pattern: String): String {
    return SimpleDateFormat(pattern).format(this)
}

fun String.toLong(pattern: String): Long {
    return SimpleDateFormat(pattern).parse(this).time
}

fun LocalDate.toDateString(pattern: String): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDate.getDayWeekStart(dayOfWeek: DayOfWeek): LocalDate {
    return this.with(TemporalAdjusters.previousOrSame(dayOfWeek))
}

fun LocalDate.getFirstDayOfMonth(): LocalDate {
    return this.with(TemporalAdjusters.firstDayOfMonth())
}

fun LocalDate.getLastDayOfMonth(): LocalDate {
    return this.with(TemporalAdjusters.lastDayOfMonth())
}

fun LocalDate.getFirstDayOfYear(): LocalDate {
    return this.with(TemporalAdjusters.firstDayOfYear())
}

fun LocalDate.getLastDayOfYear(): LocalDate {
    return this.with(TemporalAdjusters.lastDayOfYear())
}


fun LocalDate.toLong(): Long {
    val startOfDay = this.atStartOfDay(ZoneId.systemDefault())
    return startOfDay.toInstant().toEpochMilli()
}

fun LocalDateTime.toLong(): Long {
    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Int.toCurrency(preferCurrency: Currency): String {

    val numberParse = NumberFormat.getCurrencyInstance()

    numberParse.currency = preferCurrency

    numberParse.maximumFractionDigits = 0
    numberParse.minimumFractionDigits = 0

    return numberParse.format(this)
}

fun Int.toCurrency(preferCurrency: Currency, fractionsDigits: Int = 2): String {

    val numberParse = NumberFormat.getCurrencyInstance()

    numberParse.currency = preferCurrency

    numberParse.maximumFractionDigits = fractionsDigits
    numberParse.minimumFractionDigits = fractionsDigits

    return numberParse.format(this)
}

fun BigDecimal.toCurrency(preferCurrency: Currency): String {

    val numberParse = NumberFormat.getCurrencyInstance()

    numberParse.currency = preferCurrency

    numberParse.maximumFractionDigits = 0
    numberParse.minimumFractionDigits = 0

    return numberParse.format(this)
}

fun BigDecimal.toCurrency(preferCurrency: Currency = NumberFormat.getCurrencyInstance().currency, fractionsDigits: Int = 2): String {

    val numberParse = NumberFormat.getCurrencyInstance()

    numberParse.currency = preferCurrency

    numberParse.maximumFractionDigits = fractionsDigits
    numberParse.minimumFractionDigits = fractionsDigits

    return numberParse.format(this)
}

fun Long.toCurrency(preferCurrency: Currency): String {

    val numberParse = NumberFormat.getCurrencyInstance()

    numberParse.currency = preferCurrency

    numberParse.maximumFractionDigits = 0
    numberParse.minimumFractionDigits = 0

    return numberParse.format(this)
}

fun Long.toCurrency(preferCurrency: Currency = NumberFormat.getCurrencyInstance().currency, fractionsDigits: Int = 2): String {

    val numberParse = NumberFormat.getCurrencyInstance()

    numberParse.currency = preferCurrency

    numberParse.maximumFractionDigits = fractionsDigits
    numberParse.minimumFractionDigits = fractionsDigits

    return numberParse.format(this)
}


fun Float.toCurrency(preferCurrency: Currency): String {

    val numberParse = NumberFormat.getCurrencyInstance()

    numberParse.currency = preferCurrency

    numberParse.maximumFractionDigits = 0
    numberParse.minimumFractionDigits = 0

    return numberParse.format(this)
}

fun Float.toCurrency(preferCurrency: Currency = NumberFormat.getCurrencyInstance().currency, fractionsDigits: Int = 2): String {

    val numberParse = NumberFormat.getCurrencyInstance()

    numberParse.currency = preferCurrency

    numberParse.maximumFractionDigits = fractionsDigits
    numberParse.minimumFractionDigits = fractionsDigits

    return numberParse.format(this)
}


fun Double.toCurrency(preferCurrency: Currency): String {

    val numberParse = NumberFormat.getCurrencyInstance()

    numberParse.currency = preferCurrency

    numberParse.maximumFractionDigits = 0
    numberParse.minimumFractionDigits = 0

    return numberParse.format(this)
}

fun Double.toCurrency(preferCurrency: Currency = NumberFormat.getCurrencyInstance().currency, fractionsDigits: Int = 2): String {

    val numberParse = NumberFormat.getCurrencyInstance()

    numberParse.currency = preferCurrency

    numberParse.maximumFractionDigits = fractionsDigits
    numberParse.minimumFractionDigits = fractionsDigits

    return numberParse.format(this)
}

class LocalDateRangeIterator(private val startDate: LocalDate, private val endDate: LocalDate) :
    Iterator<LocalDate> {
    private var currentDate = startDate

    override fun hasNext(): Boolean {
        return currentDate <= endDate
    }

    override fun next(): LocalDate {
        val nextDate = currentDate
        currentDate = currentDate.plusDays(1)
        return nextDate
    }
}

operator fun Pair<LocalDate, LocalDate>.iterator(): Iterator<LocalDate> {
    return LocalDateRangeIterator(first, second)
}


fun Pair<LocalDate, LocalDate>.toDateStringList(timeFrame: TimeFrame): List<String> {
    val startDate = first
    val endDate = second

    return when (timeFrame) {
        TimeFrame.WEEKLY -> {
            val dateFormatter = DateTimeFormatter.ofPattern("E")
            generateSequence(startDate) { it.plusDays(1)  }
                .takeWhile { it <= endDate }
                .map { it.format(dateFormatter) }
                .toList()
        }
        TimeFrame.MONTHLY -> {
            val dateFormatter = DateTimeFormatter.ofPattern("MMM")
            generateSequence(startDate) { it.plusMonths(1)  }
                .takeWhile { it <= endDate }
                .map { it.format(dateFormatter) }
                .toList()
        }
        TimeFrame.ANNUALLY -> {
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy")
            generateSequence(startDate) { it.plusYears(1)  }
                .takeWhile { it <= endDate }
                .map { it.format(dateFormatter) }
                .toList()
        }

        TimeFrame.ALL -> {
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy")
            generateSequence(startDate) { it.plusYears(1)  }
                .takeWhile { it <= endDate }
                .map { it.format(dateFormatter) }
                .toList()
        }
    }
}
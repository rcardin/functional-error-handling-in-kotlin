package `in`.rcard

import arrow.core.raise.fold
import `in`.rcard.domain.JobId
import `in`.rcard.raise.CurrencyConverter
import `in`.rcard.raise.JobsService
import `in`.rcard.raise.LiveJobs
import `in`.rcard.raise.RaiseCurrencyConverter

fun main() {
    val service = JobsService(LiveJobs(), RaiseCurrencyConverter(CurrencyConverter()))
    fold({ service.getSalaryGapWithMax(listOf(JobId(1), JobId(42), JobId(-1))) },
        { error -> println("The risen errors are: $error") },
        { salaryGap -> println("The list of salary gaps is $salaryGap") })
}

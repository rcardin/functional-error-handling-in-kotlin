package `in`.rcard

import arrow.core.raise.fold
import `in`.rcard.domain.JobId
import `in`.rcard.raise.CurrencyConverter
import `in`.rcard.raise.JobsService
import `in`.rcard.raise.LiveJobs
import `in`.rcard.raise.RaiseCurrencyConverter

fun main() {
    val service = JobsService(LiveJobs(), RaiseCurrencyConverter(CurrencyConverter()))
    fold({ service.getSalaryGapWithMax(JobId(42)) },
        { error -> println("An error was raised: $error") },
        { salaryGap -> println("The salary gap is $salaryGap") })
}

package `in`.rcard

import arrow.core.raise.fold
import `in`.rcard.domain.JobId
import `in`.rcard.raise.CurrencyConverter
import `in`.rcard.raise.JobsService
import `in`.rcard.raise.LiveJobs
import `in`.rcard.raise.RaiseCurrencyConverter

fun main() {
    val jobService = JobsService(LiveJobs(), RaiseCurrencyConverter(CurrencyConverter()))
    fold({ jobService.getSalaryGapWithMaxJobErrors(listOf(JobId(-1), JobId(42))) },
        { error -> println("The risen errors are: $error") },
        { salaryGaps -> println("The salary gaps are $salaryGaps") })
}

package `in`.rcard

import `in`.rcard.domain.CurrencyConverter
import `in`.rcard.domain.JobId
import `in`.rcard.nullable.Jobs
import `in`.rcard.nullable.JobsService
import `in`.rcard.nullable.LiveJobs

fun main() {
    val jobs: Jobs = LiveJobs()
    val currencyConverter = CurrencyConverter()
    val jobsService = JobsService(jobs, currencyConverter)
    val salarySum = jobsService.sumSalaries2(JobId(42), JobId(2)) ?: 0.0
    println("The sum of the salaries using 'sumSalaries' is $salarySum")
}

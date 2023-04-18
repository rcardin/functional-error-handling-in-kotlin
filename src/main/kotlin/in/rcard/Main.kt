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
    val salarySum1 = jobsService.sumSalaries(JobId(100), JobId(2))
    val salarySum2 = jobsService.sumSalaries2(JobId(100), JobId(2))
    println("The sum of the salaries using 'sumSalaries' is $salarySum1")
    println("The sum of the salaries using 'sumSalaries2' is $salarySum2")
}

package `in`.rcard

import arrow.core.getOrElse
import `in`.rcard.domain.JobId
import `in`.rcard.option.Jobs
import `in`.rcard.option.JobsService
import `in`.rcard.option.LiveJobs

fun main() {
    val jobs: Jobs = LiveJobs()
    val jobsService = JobsService(jobs)
    val salarySum = jobsService.getSalaryGapWithMax2(JobId(42))
    println("The sum of the salaries using 'sumSalaries' is ${salarySum.getOrElse { 0.0 }}")
}

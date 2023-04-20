package `in`.rcard

import arrow.core.Option
import arrow.core.getOrElse
import `in`.rcard.domain.JobId
import `in`.rcard.option.Jobs
import `in`.rcard.option.JobsService
import `in`.rcard.option.LiveJobs

fun main() {
    val jobs: Jobs = LiveJobs()
    val jobsService = JobsService(jobs)
    val appleJobId = JobId(1)
    val salaryGap: Option<Double> = jobsService.getSalaryGapWithMax(appleJobId)
    println("The salary gap between $appleJobId and the max salary is ${salaryGap.getOrElse { 0.0 }}")
}

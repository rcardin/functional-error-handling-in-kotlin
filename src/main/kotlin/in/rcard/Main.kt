package `in`.rcard

import `in`.rcard.domain.JobId
import `in`.rcard.option.Jobs
import `in`.rcard.option.JobsService
import `in`.rcard.option.LiveJobs

fun main() {
    val jobs: Jobs = LiveJobs()
    val jobsService = JobsService(jobs)
    val fakeJobId = JobId(42)
    val salaryGap: Double? = jobsService.getSalaryGapWithMax4(fakeJobId)
    println("The salary gap between $fakeJobId and the max salary is ${salaryGap ?: 0.0}")
}

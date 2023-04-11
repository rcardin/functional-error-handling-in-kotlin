package `in`.rcard

import `in`.rcard.domain.JobId
import `in`.rcard.exception.Jobs
import `in`.rcard.exception.JobsService
import `in`.rcard.exception.LiveJobs

fun main() {
    val jobs: Jobs = LiveJobs()
    val jobsService = JobsService(jobs)
    val jobId: Long = 42
    val salary = jobsService.retrieveSalary(JobId(jobId))
    println("The salary of the job $jobId is $salary")
}

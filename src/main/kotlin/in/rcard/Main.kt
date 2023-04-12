package `in`.rcard

import `in`.rcard.domain.JobId
import `in`.rcard.nullable.Jobs
import `in`.rcard.nullable.JobsService
import `in`.rcard.nullable.LiveJobs

fun main() {
    val jobs: Jobs = LiveJobs()
    val jobsService = JobsService(jobs)
    val jobId: Long = 42
    val salary = jobsService.retrieveSalary(JobId(jobId))
    println("The salary of the job $jobId is $salary")
}

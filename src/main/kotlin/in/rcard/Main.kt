package `in`.rcard

import `in`.rcard.domain.JobId
import `in`.rcard.option.Jobs
import `in`.rcard.option.JobsService
import `in`.rcard.option.LiveJobs

fun main() {
    val jobs: Jobs = LiveJobs()
    val jobsService = JobsService(jobs)
    jobsService.printOptionJob(JobId(42))
}

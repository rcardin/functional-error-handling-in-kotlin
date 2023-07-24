package `in`.rcard

import `in`.rcard.domain.JobId
import `in`.rcard.raise.JobsService
import `in`.rcard.raise.LiveJobs

fun main() {
    val appleJobId = JobId(1)
    val jobs = LiveJobs()
    val jobService = JobsService(jobs)
    jobService.printSalary(appleJobId)
}

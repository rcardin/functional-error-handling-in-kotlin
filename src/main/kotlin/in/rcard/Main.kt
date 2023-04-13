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
    val appleJobId = JobId(1)
    val isAppleJob = jobsService.isAppleJob(appleJobId)
    println("Q: Is the job with id $appleJobId an Apple job?\nA: $isAppleJob")
}

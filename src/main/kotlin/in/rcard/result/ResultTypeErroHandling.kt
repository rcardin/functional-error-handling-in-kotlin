package `in`.rcard.result

import `in`.rcard.domain.Company
import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId
import `in`.rcard.domain.Role
import `in`.rcard.domain.Salary

fun main() {
    JobService(LiveJobs()).printOptionJob(JobId(1))
}

val appleJob: Result<Job> = Result.success(
    Job(
        JobId(2),
        Company("Apple, Inc."),
        Role("Software Engineer"),
        Salary(70_000.00),
    ),
)

val notFoundJob: Result<Job> = Result.failure(NoSuchElementException("Job not found"))

val result = 42.toResult()
fun <T> T.toResult(): Result<T> =
    if (this is Throwable) Result.failure(this) else Result.success(this)

interface Jobs {

    fun findById(id: JobId): Result<Job?>
}

class LiveJobs : Jobs {

    override fun findById(id: JobId): Result<Job?> = id.runCatching {
        JOBS_DATABASE[this]
    }
}

class JobService(private val jobs: Jobs) {

    fun printOptionJob(jobId: JobId) {
        val maybeJob: Result<Job?> = jobs.findById(jobId)
        if (maybeJob.isSuccess) {
            maybeJob.getOrNull()?.apply { println("Job found: $this") } ?: println("Job not found for id $jobId")
        } else {
            println("Something went wrong: ${maybeJob.exceptionOrNull()}")
        }
    }
}

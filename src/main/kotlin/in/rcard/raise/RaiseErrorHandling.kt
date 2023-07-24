package `in`.rcard.raise

import arrow.core.raise.Raise
import arrow.core.raise.fold
import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId
import `in`.rcard.either.JobError
import `in`.rcard.either.JobNotFound

context(Raise<JobNotFound>)
fun appleJob(): Job = JOBS_DATABASE[JobId(1)]!!

// fun Raise<JobNotFound>.appleJob(): Job = JOBS_DATABASE[JobId(1)]!!

context(Raise<JobNotFound>)
fun jobNotFound(): Job = raise(JobNotFound(JobId(42)))

// fun Raise<JobNotFound>.jobNotFound(): Job = raise(GenericError("Job not found"))

val retrieveCompany: Job.() -> String = { company.name }

val appleCompany = retrieveCompany(JOBS_DATABASE[JobId(1)]!!)

interface Logger {
    fun info(message: String)
}

val consoleLogger = object : Logger {
    override fun info(message: String) {
        println("[INFO] $message")
    }
}

class JobsService(private val jobs: Jobs) {

    fun printSalary(jobId: JobId) = fold(
        block = { jobs.findById(jobId) },
        recover = { error: JobError ->
            when (error) {
                is JobNotFound -> println("Job with id ${jobId.value} not found")
                else -> println("An error was raised: $error")
            }
        },
        transform = { job: Job ->
            println("Job salary for job with id ${jobId.value} is ${job.salary}")
        },
    )
}

interface Jobs {

    context (Raise<JobError>)
    fun findById(id: JobId): Job
}

class LiveJobs : Jobs {

    context (Raise<JobError>)
    override fun findById(id: JobId): Job {
        return JOBS_DATABASE[id] ?: raise(JobNotFound(id))
    }
}

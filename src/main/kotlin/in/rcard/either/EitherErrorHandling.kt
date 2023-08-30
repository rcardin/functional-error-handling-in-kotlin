package `in`.rcard.either

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.Option
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId
import `in`.rcard.domain.Salary

sealed interface JobError
data class JobNotFound(val jobId: JobId) : JobError
data class GenericError(val cause: String) : JobError
data object NegativeSalary : JobError

object EitherJobDomain {
    @JvmInline
    value class Salary private constructor(val value: Double) {
        companion object {
            operator fun invoke(value: Double): Either<JobError, Salary> = either.eager {
                ensure(value >= 0.0) { NegativeSalary }
                Salary(value)
            }
        }
    }
}

val appleJobId = JobId(1)
val appleJob: Either<JobError, Job> = Right(JOBS_DATABASE[appleJobId]!!)
val jobNotFound: Either<JobError, Job> = Left(JobNotFound(appleJobId))

val anotherAppleJob = JOBS_DATABASE[appleJobId]!!.right()
val anotherJobNotFound: Either<JobError, Job> = JobNotFound(appleJobId).left()

val jobSalary: Salary = jobNotFound.fold({ Salary(0.0) }, { it.salary })
val jobSalary2: Salary = jobNotFound.map { it.salary }.getOrElse { Salary(0.0) }

val appleJobOrNull: Job? = appleJob.getOrNull()
val maybeAppleJob: Option<Job> = appleJob.getOrNone()

val jobCompany: String = appleJob.map { it.company.name }.getOrElse { "Unknown company" }
val jobCompany2: String = appleJob.map { it.company.name }.getOrElse { jobError ->
    when (jobError) {
        is JobNotFound -> "Job not found"
        is GenericError -> "Generic error"
        is NegativeSalary -> "Negative amount"
        else -> "Unknown error"
    }

}

fun printSalary(maybeJob: Either<JobError, Job>) = when (maybeJob) {
    is Right -> println("Job salary is ${maybeJob.value.salary}")
    is Left -> println("No job found")
}

interface Jobs {

    fun findById(id: JobId): Either<JobError, Job>
    fun findAll(): Either<JobError, List<Job>>
}

class LiveJobs : Jobs {
    override fun findById(id: JobId): Either<JobError, Job> = catch {
        JOBS_DATABASE[id]
    }.mapLeft { GenericError(it.message ?: "Unknown error") }
        .flatMap { maybeJob -> maybeJob?.right() ?: JobNotFound(id).left() }

    override fun findAll(): Either<JobError, List<Job>> =
        JOBS_DATABASE.values.toList().right()
}

class JobsService(private val jobs: Jobs) {
    fun getSalaryGapWithMax(jobId: JobId): Either<JobError, Double> =
        jobs.findById(jobId).flatMap { job ->
            jobs.findAll().flatMap { jobs ->
                jobs.maxSalary().map { maxSalary ->
                    (maxSalary.value - job.salary.value)
                }
            }
        }

    fun getSalaryGapWithMax2(jobId: JobId): Either<JobError, Double> = either.eager {
        val job = jobs.findById(jobId).bind()
        val jobsList = jobs.findAll().bind()
        val maxSalary = jobsList.maxSalary().bind()
        maxSalary.value - job.salary.value
    }

    fun getSalaryGapWithMax3(jobId: JobId): Either<JobError, Double> = either.eager {
        val job = jobs.findById(jobId).bind()
        val jobsList = jobs.findAll().bind()
        val maxSalary = ensureNotNull(jobsList.maxSalary2()) { GenericError("No jobs found") }
        maxSalary.value - job.salary.value
    }

    private fun List<Job>.maxSalary(): Either<GenericError, Salary> =
        if (this.isEmpty()) {
            GenericError("No jobs found").left()
        } else {
            this.maxBy { it.salary.value }.salary.right()
        }

    private fun List<Job>.maxSalary2(): Salary? = this.maxBy { it.salary.value }.salary
}

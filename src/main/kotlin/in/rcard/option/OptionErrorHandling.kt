package `in`.rcard.option

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.continuations.ensureNotNull
import arrow.core.continuations.nullable
import arrow.core.continuations.option
import arrow.core.none
import arrow.core.some
import arrow.core.toOption
import `in`.rcard.domain.Company
import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId
import `in`.rcard.domain.Role
import `in`.rcard.domain.Salary

val awsJob: Some<Job> =
    Some(
        Job(
            JobId(1),
            Company("AWS"),
            Role("Software Engineer"),
            Salary(100_000.00),
        ),
    )
val noJob: None = None

val appleJob: Option<Job> =
    Job(
        JobId(2),
        Company("Apple, Inc."),
        Role("Software Engineer"),
        Salary(70_000.00),
    ).some()
val noAppleJob: Option<Job> = none()

val microsoftJob: Job? =
    Job(
        JobId(3),
        Company("Microsoft"),
        Role("Software Engineer"),
        Salary(80_000.00),
    )
val maybeMsJob: Option<Job> = Option.fromNullable(microsoftJob)
val noMsJob: Option<Job> = Option.fromNullable(null)

val googleJob: Option<Job> =
    Job(
        JobId(4),
        Company("Google"),
        Role("Software Engineer"),
        Salary(90_000.00),
    ).toOption()
val noGoogleJob: Option<Job> = null.toOption()

interface Jobs {

    fun findAll(): List<Job>
    fun findById(id: JobId): Option<Job>
}

class LiveJobs : Jobs {
    override fun findAll(): List<Job> = JOBS_DATABASE.values.toList()

    override fun findById(id: JobId): Option<Job> = try {
        JOBS_DATABASE[id].toOption()
    } catch (e: Exception) {
        none()
    }
}

class JobsService(private val jobs: Jobs) {

    fun printOptionJob(jobId: JobId) {
        val maybeJob: Option<Job> = jobs.findById(jobId)
        when (maybeJob) {
            is Some -> println("Job found: ${maybeJob.value}")
            is None -> println("Job not found for id $jobId")
        }
    }

    fun getSalaryGapWithMax(jobId: JobId): Option<Double> {
        val maybeJob: Option<Job> = jobs.findById(jobId)
        val maybeMaxSalary: Option<Salary> =
            jobs.findAll().maxBy { it.salary.value }.toOption().map { it.salary }
        return maybeJob.flatMap { job ->
            maybeMaxSalary.map { maxSalary ->
                maxSalary.value - job.salary.value
            }
        }
    }

    fun getSalaryGapWithMax2(jobId: JobId): Option<Double> = option.eager {
        println("Searching for the job with id $jobId")
        val job: Job = jobs.findById(jobId).bind()
        println("Job found: $job")
        println("Searching for the job with the max salary")
        val maxSalaryJob: Job = jobs.findAll().maxBy { it.salary.value }.toOption().bind()
        println("Job found: $maxSalaryJob")
        maxSalaryJob.salary.value - job.salary.value
    }

    fun getSalaryGapWithMax3(jobId: JobId): Option<Double> = option.eager {
        val job: Job = jobs.findById(jobId).bind()
        val maxSalaryJob: Job = ensureNotNull(
            jobs.findAll().maxBy { it.salary.value },
        )
        maxSalaryJob.salary.value - job.salary.value
    }

    fun getSalaryGapWithMax4(jobId: JobId): Double? = nullable.eager {
        println("Searching for the job with id $jobId")
        val job: Job = jobs.findById(jobId).bind()
        println("Job found: $job")
        println("Searching for the job with the max salary")
        val maxSalaryJob: Job = ensureNotNull(
            jobs.findAll().maxBy { it.salary.value },
        )
        println("Job found: $maxSalaryJob")
        maxSalaryJob.salary.value - job.salary.value
    }
}

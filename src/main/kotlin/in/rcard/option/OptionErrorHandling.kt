package `in`.rcard.option

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
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

    fun findAll(): List<Job> = JOBS_DATABASE.values.toList()
    fun findById(id: JobId): Option<Job>
}

class LiveJobs : Jobs {
    override fun findById(id: JobId): Option<Job> {
        val job: Job? = JOBS_DATABASE[id]
        return job.toOption()
    }
}

class JobsService(private val jobs: Jobs) {

    fun getSalaryGapWithMax(jobId: JobId): Option<Double> {
        val job: Option<Job> = jobs.findById(jobId)
        val maxSalary: Option<Salary> =
            jobs.findAll().maxBy { it.salary.value }.toOption().map { it.salary }
        return job.flatMap { j ->
            maxSalary.map { m ->
                m.value - j.salary.value
            }
        }
    }

    suspend fun getSalaryGapWithMax2(jobId: JobId): Option<Double> = option {
        val job: Job = jobs.findById(jobId).bind()
        val maxSalaryJob: Job = jobs.findAll().maxBy { it.salary.value }.toOption().bind()
        maxSalaryJob.salary.value - job.salary.value
    }

    suspend fun getSalaryGapWithMax3(jobId: JobId): Option<Double> = option {
        val job: Job = jobs.findById(jobId).bind()
        val maxSalaryJob: Job = jobs.findAll().maxBy { it.salary.value }.toOption().bind()
        maxSalaryJob.salary.value - job.salary.value
    }
}

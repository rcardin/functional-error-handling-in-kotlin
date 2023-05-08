package `in`.rcard.result

import arrow.core.Option
import arrow.core.toOption
import `in`.rcard.domain.Company
import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId
import `in`.rcard.domain.Role
import `in`.rcard.domain.Salary
import java.lang.IllegalArgumentException

fun main() {
    val currencyConverter = CurrencyConverter()
    val jobs = LiveJobs()
    val notFoundJobId = JobId(42)
    val maybeSalary: Result<Double> = JobService(jobs, currencyConverter).getSalaryInEur(notFoundJobId)
    maybeSalary.fold({
        println("The salary of jobId $notFoundJobId is $it")
    }, {
        when (it) {
            is IllegalArgumentException -> println("The amount must be positive")
            else -> println("An error occurred ${it.message}")
        }
    })
}

val appleJob: Result<Job> = Result.success(
    Job(
        JobId(2),
        Company("Apple, Inc."),
        Role("Software Engineer"),
        Salary(70_000.00),
    ),
)

val appleJobSalary: Result<Salary> = appleJob.map { it.salary }

val notFoundJob: Result<Job> = Result.failure(NoSuchElementException("Job not found"))

val result = 42.toResult()
fun <T> T.toResult(): Result<T> =
    if (this is Throwable) Result.failure(this) else Result.success(this)

interface Jobs {

    fun findAll(): Result<List<Job>>
    fun findById(id: JobId): Result<Job?>
}

class LiveJobs : Jobs {
    override fun findAll(): Result<List<Job>> = Result.success(JOBS_DATABASE.values.toList())

    override fun findById(id: JobId): Result<Job?> = id.runCatching {
        JOBS_DATABASE[this]
    }
}

class CurrencyConverter {
    @Throws(IllegalArgumentException::class)
    fun convertUsdToEur(amount: Double?): Double =
        if (amount != null && amount >= 0.0) {
            amount * 0.91
        } else {
            throw IllegalArgumentException("Amount must be positive")
        }
}

class JobService(private val jobs: Jobs, private val currencyConverter: CurrencyConverter) {

    fun printOptionJob(jobId: JobId) {
        val maybeJob: Result<Job?> = jobs.findById(jobId)
        if (maybeJob.isSuccess) {
            maybeJob.getOrNull()?.apply { println("Job found: $this") } ?: println("Job not found for id $jobId")
        } else {
            println("Something went wrong: ${maybeJob.exceptionOrNull()}")
        }
    }

    fun getSalaryInEur(jobId: JobId): Result<Double> =
        jobs.findById(jobId)
            .map { it?.salary }
            .mapCatching { currencyConverter.convertUsdToEur(it?.value) }

//    fun getSalaryGapWithMax(jobId: JobId): Option<Double> {
//        val maybeJob: Result<Job?> = jobs.findById(jobId)
//        val maybeMaxSalary: Result<Salary> =
//            jobs.findAll().map { jobsList-> jobsList.maxBy { it.salary.value }
//        return maybeJob.flatMap { job ->
//            maybeMaxSalary.map { maxSalary ->
//                maxSalary.value - job.salary.value
//            }
//        }
//    }
}

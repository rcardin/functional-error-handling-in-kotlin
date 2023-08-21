package `in`.rcard.raise

import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.raise.NullableRaise
import arrow.core.raise.OptionRaise
import arrow.core.raise.Raise
import arrow.core.raise.ResultRaise
import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.raise.fold
import arrow.core.raise.nullable
import arrow.core.raise.option
import `in`.rcard.domain.Company
import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId
import `in`.rcard.domain.Role
import `in`.rcard.domain.Salary
import `in`.rcard.either.GenericError
import `in`.rcard.either.JobError
import `in`.rcard.either.JobNotFound
import `in`.rcard.either.NegativeSalary

sealed interface CurrencyConversionError
data object NegativeAmount : CurrencyConversionError

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

class JobsService(private val jobs: Jobs, private val converter: RaiseCurrencyConverter) {

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

    fun company(jobId: JobId): Either<JobError, Company> = either {
        jobs.findById(jobId).company
    }

    context (Raise<JobError>)
    fun companyWithRaise(jobId: JobId): Company = company(jobId).bind()

    fun salary(jobId: JobId): Option<Salary> = option {
        jobs.findByIdWithOption(jobId).salary
    }

    context (OptionRaise)
    fun salaryWithRaise(jobId: JobId): Salary = salary(jobId).bind()

    context (NullableRaise)
    fun salaryWithNullableRaise(jobId: JobId): Salary = salary(jobId).bind()

    fun role(jobId: JobId): Role? = nullable {
        jobs.findByIdWithNullable(jobId).role
    }

    context (NullableRaise)
    fun roleWithRaise(jobId: JobId): Role = role(jobId).bind()

    context (Raise<JobError>, Raise<Throwable>)
    fun salaryInEur(jobId: JobId): Double {
        val job = jobs.findById(jobId)
        return catch(
            {
                converter.convertUsdToEur(job.salary.value)
            },
            {
                raise(it)
            },
        )
    }

    context (Raise<JobError>)
    fun getSalaryGapWithMax(jobId: JobId): Double {
        val job: Job = jobs.findById(jobId)
        val jobList: List<Job> = jobs.findAll()
        val maxSalary: Salary = jobList.maxSalary()
        return maxSalary.value - job.salary.value
    }

    context (Raise<JobError>, Raise<NegativeAmount>)
    fun getSalaryGapWithMaxInEur(jobId: JobId): Double {
        val job: Job = jobs.findById(jobId)
        val jobList: List<Job> = jobs.findAll()
        val maxSalary: Salary = jobList.maxSalary()
        val salaryGap = maxSalary.value - job.salary.value
        return converter.convertUsdToEurRaisingNegativeAmount(salaryGap)
    }
}

interface Jobs {

    context (Raise<JobError>)
    fun findById(id: JobId): Job

    context (Raise<JobError>)
    fun findAll(): List<Job>

    context (Raise<None>)
    fun findByIdWithOption(id: JobId): Job

    context (NullableRaise)
    fun findByIdWithNullable(id: JobId): Job
}

class LiveJobs : Jobs {

    context (Raise<JobError>)
    override fun findById(id: JobId): Job {
        return JOBS_DATABASE[id] ?: raise(JobNotFound(id))
    }

    context(Raise<JobError>)
    override fun findAll(): List<Job> = catch({
        JOBS_DATABASE.values.toList()
    }) { _: Throwable ->
        raise(GenericError("An error occurred while retrieving all the jobs"))
    }

    context (Raise<None>)
    override fun findByIdWithOption(id: JobId): Job {
        return JOBS_DATABASE[id] ?: raise(None)
    }

    context(NullableRaise)
    override fun findByIdWithNullable(id: JobId): Job {
        return JOBS_DATABASE[id] ?: raise(null)
    }
}

context (Raise<JobError>)
private fun List<Job>.maxSalary(): Salary =
    if (isEmpty()) {
        raise(GenericError("No jobs found"))
    } else {
        this.maxBy { it.salary.value }.salary
    }

fun convertUsdToEur(amount: Double?, converter: CurrencyConverter) {
    converter.convertUsdToEur(amount)
}

class CurrencyConverter {
    @Throws(IllegalArgumentException::class)
    fun convertUsdToEur(amount: Double?): Double =
        if (amount == null || amount < 0.0) {
            throw IllegalArgumentException("Amount must be positive")
        } else {
            amount * 0.91
        }
}

class RaiseCurrencyConverter(private val currencyConverter: CurrencyConverter) {

    context (Raise<NegativeSalary>)
    fun convertUsdToEur(amount: Double?): Double = catch({
        currencyConverter.convertUsdToEur(amount)
    }) { _: IllegalArgumentException ->
        raise(NegativeSalary)
    }

    context (Raise<NegativeAmount>)
    fun convertUsdToEurRaisingNegativeAmount(amount: Double?): Double = catch({
        currencyConverter.convertUsdToEur(amount)
    }) { _: IllegalArgumentException ->
        raise(NegativeAmount)
    }

    context (ResultRaise)
    fun convertUsdToEurRaiseException(amount: Double?): Double = catch({
        currencyConverter.convertUsdToEur(amount)
    }) { ex: IllegalArgumentException ->
        raise(ex)
    }
}

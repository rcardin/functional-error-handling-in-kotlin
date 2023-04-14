package `in`.rcard.nullable

import `in`.rcard.domain.CurrencyConverter
import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId

interface Jobs {
    fun findById(id: JobId): Job?
}

class LiveJobs : Jobs {
    override fun findById(id: JobId): Job? = try {
        JOBS_DATABASE[id]
    } catch (e: Exception) {
        null
    }
}

class JobsService(private val jobs: Jobs, private val converter: CurrencyConverter) {
    fun retrieveSalary(id: JobId): Double =
        jobs.findById(id)?.salary?.value ?: 0.0

    fun retrieveSalaryInEur(id: JobId): Double =
        jobs.findById(id)?.let { converter.convertUsdToEur(it.salary.value) } ?: 0.0

    fun isAppleJob(id: JobId): Boolean =
        jobs.findById(id)?.takeIf { it.company.name == "Apple" } != null

    fun sumSalaries(jobId1: JobId, jobId2: JobId): Double? {
        val maybeJob1: Job? = jobs.findById(jobId1)
        val maybeJob2: Job? = jobs.findById(jobId2)
        return maybeJob1?.let { job1 ->
            maybeJob2?.let { job2 ->
                job1.salary.value + job2.salary.value
            }
        }
    }
}

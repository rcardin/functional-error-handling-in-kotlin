package `in`.rcard.nullable

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

class JobsService(private val jobs: Jobs) {
    fun retrieveSalary(id: JobId): Double =
        jobs.findById(id)?.salary?.value ?: 0.0
}

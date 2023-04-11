package `in`.rcard.exception

import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId

interface Jobs {
    fun findById(id: JobId): Job
}

class LiveJobs : Jobs {
    override fun findById(id: JobId): Job {
        val maybeJob: Job? = JOBS_DATABASE[id]
        if (maybeJob != null) {
            return maybeJob
        } else {
            throw NoSuchElementException("Job not found")
        }
    }
}

class JobsService(private val jobs: Jobs) {
    fun retrieveSalary(id: JobId): Double {
        return try {
            val job = jobs.findById(id)
            job.salary.value
        } catch (e: Exception) {
            0.0
        }
    }
}

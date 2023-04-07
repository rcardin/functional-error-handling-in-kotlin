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
            throw RuntimeException("Job not found")
        }
    }
}

package `in`.rcard.nullable

import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId

interface Jobs {
    fun findById(id: JobId): Job?
}

class LiveJobs : Jobs {
    override fun findById(id: JobId): Job? = JOBS_DATABASE[id]
}

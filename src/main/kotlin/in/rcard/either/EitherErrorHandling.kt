package `in`.rcard.either

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId

sealed interface JobError
data class JobNotFound(val jobId: JobId) : JobError
data class GenericError(val cause: String) : JobError

val appleJobId = JobId(1)
val appleJob: Either<JobError, Job> = Right(JOBS_DATABASE[appleJobId]!!)
val jobNotFound: Either<JobError, JobNotFound> = Left(JobNotFound(appleJobId))

val anotherAppleJob = JOBS_DATABASE[appleJobId]!!.right()
val anotherJobNotFound: Either<JobError, JobNotFound> = JobNotFound(appleJobId).left()

interface Jobs {

    fun findById(id: JobId): Either<JobError, Job>
}

class LiveJobs : Jobs {
    override fun findById(id: JobId): Either<JobError, Job> = catch {
        JOBS_DATABASE[id]
    }.mapLeft { GenericError(it.message ?: "Unknown error") }
        .flatMap { maybeJob -> maybeJob?.right() ?: JobNotFound(id).left() }
}

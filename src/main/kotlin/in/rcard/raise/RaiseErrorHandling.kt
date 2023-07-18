package `in`.rcard.raise

import arrow.core.raise.Raise
import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId
import `in`.rcard.either.JobNotFound

context(Raise<JobNotFound>)
fun appleJob(): Job = JOBS_DATABASE[JobId(1)]!!

context(Raise<JobNotFound>)
fun jobNotFound(): Job = raise(JobNotFound(JobId(42)))

val retrieveCompany: Job.() -> String = { company.name }

val appleCompany = retrieveCompany(JOBS_DATABASE[JobId(1)]!!)

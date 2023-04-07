package `in`.rcard.option

import arrow.core.Option
import arrow.core.toOption
import `in`.rcard.domain.JOBS_DATABASE
import `in`.rcard.domain.Job
import `in`.rcard.domain.JobId

// val rockTheJvmJob: Some<Job> =
//    Some(Job(Company("Rock The Jvm"), Role("Technical Writer"), Salary(100_000.00)))
// val noJob: None = None
//
// val appleJob: Option<Job> =
//    Job(Company("Apple, Inc."), Role("Software Engineer"), Salary(70_000.00)).some()
// val noAppleJob: Option<Job> = none()
//
// val microsoftJob: Job? =
//    Job(Company("Microsoft"), Role("Software Engineer"), Salary(80_000.00))
// val maybeMsJob: Option<Job> = Option.fromNullable(microsoftJob)
// val noMsJob: Option<Job> = Option.fromNullable(null)
//
// val googleJob: Option<Job> =
//    Job(Company("Google"), Role("Software Engineer"), Salary(90_000.00)).toOption()
// val noGoogleJob: Option<Job> = null.toOption()

interface Jobs {
    fun findById(id: JobId): Option<Job>
}

class LiveJobs : Jobs {
    override fun findById(id: JobId): Option<Job> {
        val job: Job? = JOBS_DATABASE[id]
        return job.toOption()
    }
}

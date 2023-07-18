package `in`.rcard

import arrow.core.raise.fold
import `in`.rcard.domain.Job
import `in`.rcard.either.JobError
import `in`.rcard.raise.appleJob

fun main() {
    fold(
        block = { appleJob() },
        recover = { error: JobError ->
            println("An error was raised: $error")
        },
        transform = { job: Job ->
            println(job)
        },
    )
}

package `in`.rcard

import arrow.core.Either
import arrow.core.raise.either
import `in`.rcard.either.EitherJobDomain.Salary
import `in`.rcard.either.JobError
import `in`.rcard.raise.appleJob

fun main() {
    either {
        appleJob()
    }
}

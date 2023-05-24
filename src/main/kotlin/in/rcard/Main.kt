package `in`.rcard

import arrow.core.Either
import `in`.rcard.domain.Salary
import `in`.rcard.either.JobError

fun main() {
    val saalryOrError: Either<JobError, Salary> = Salary(-1.0)
}

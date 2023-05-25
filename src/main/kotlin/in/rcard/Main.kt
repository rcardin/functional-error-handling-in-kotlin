package `in`.rcard

import arrow.core.Either
import `in`.rcard.either.EitherJobDomain.Salary
import `in`.rcard.either.JobError

fun main() {
    val salaryOrError: Either<JobError, Salary> = Salary(-1.0)
    println(salaryOrError)
}

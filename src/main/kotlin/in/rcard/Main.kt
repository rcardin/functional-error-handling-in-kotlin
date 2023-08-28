package `in`.rcard

import arrow.core.raise.fold
import `in`.rcard.raise.ValidatedJob

fun main() {
    fold({ ValidatedJob.Salary(-1.0, "eu") },
        { error -> println("The risen errors are: $error") },
        { salary -> println("The valid salary is $salary") })
}

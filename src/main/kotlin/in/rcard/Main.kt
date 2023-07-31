package `in`.rcard

import arrow.core.raise.fold
import `in`.rcard.either.JobError
import `in`.rcard.raise.CurrencyConverter
import `in`.rcard.raise.RaiseCurrencyConverter

fun main() {
    val converter = RaiseCurrencyConverter(CurrencyConverter())
    fold(
        block = { converter.convertUsdToEur(-100.0) },
        catch = { ex: Throwable ->
            println("An exception was thrown: $ex")
        },
        recover = { error: JobError ->
            println("An error was raised: $error")
        },
        transform = { salaryInEur: Double ->
            println("Salary in EUR: $salaryInEur")
        },
    )
}

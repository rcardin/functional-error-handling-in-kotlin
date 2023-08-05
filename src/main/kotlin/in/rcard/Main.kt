package `in`.rcard

import arrow.core.raise.recover
import `in`.rcard.either.JobError
import `in`.rcard.raise.CurrencyConverter
import `in`.rcard.raise.RaiseCurrencyConverter

fun main() {
    val converter = RaiseCurrencyConverter(CurrencyConverter())
    recover({ converter.convertUsdToEur(-1.0) }) { _: JobError ->
        0.0
    }
}

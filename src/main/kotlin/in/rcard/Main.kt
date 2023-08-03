package `in`.rcard

import arrow.core.raise.ResultRaise
import arrow.core.raise.result
import `in`.rcard.raise.CurrencyConverter
import `in`.rcard.raise.RaiseCurrencyConverter

fun main() {
    val converter = RaiseCurrencyConverter(CurrencyConverter())
    val maybeSalaryInEur: (Double) -> Result<Double> = { salary: Double ->
        result {
            converter.convertUsdToEurRaiseException(salary)
        }
    }

    val maybeSalaryInEurRaise: context(ResultRaise) (Double) -> Double = { salary: Double ->
        maybeSalaryInEur(salary).bind()
    }
}

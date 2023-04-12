package `in`.rcard.domain

val JOBS_DATABASE: Map<JobId, Job> = mapOf(
    JobId(1) to Job(
        JobId(2),
        Company("Apple, Inc."),
        Role("Software Engineer"),
        Salary(70_000.00),
    ),
    JobId(2) to Job(
        JobId(3),
        Company("Microsoft"),
        Role("Software Engineer"),
        Salary(80_000.00),
    ),
    JobId(3) to Job(
        JobId(4),
        Company("Google"),
        Role("Software Engineer"),
        Salary(90_000.00),
    ),
)

class CurrencyConverter {
    fun convertUsdToEur(amount: Double): Double = amount * 0.91
}

data class Job(val id: JobId, val company: Company, val role: Role, val salary: Salary)

@JvmInline
value class JobId(val value: Long)

@JvmInline
value class Company(val name: String)

@JvmInline
value class Role(val name: String)

@JvmInline
value class Salary(val value: Double) {
    operator fun compareTo(other: Salary): Int = value.compareTo(other.value)
}

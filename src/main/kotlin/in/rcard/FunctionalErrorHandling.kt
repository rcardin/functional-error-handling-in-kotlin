package `in`.rcard

fun main() {
    val jobsService = JobsService(FakeJobs())
    val highlyPaidJobs = jobsService.getHighlyPaidJobs(Salary(100_000.00))
    println("Best jobs on the market are: $highlyPaidJobs")
}

interface Jobs {
    fun findAll(): List<Job>
}

class FakeJobs : Jobs {
    override fun findAll(): List<Job> = throw RuntimeException("Boom!")
}

class JobsService(private val jobs: Jobs) {
    fun getHighlyPaidJobs(minimumSalary: Salary): List<Job> {
        val retrievedJobs = jobs.findAll()
        return try {
            retrievedJobs.filter { it.salary.value > minimumSalary.value }
        } catch (e: Exception) {
            listOf()
        }
    }
}

data class Job(val company: Company, val role: Role, val salary: Salary)

@JvmInline
value class Company(val name: String)

@JvmInline
value class Role(val name: String)

@JvmInline
value class Salary(val value: Double)

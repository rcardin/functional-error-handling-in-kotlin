package `in`.rcard

fun main() {
    val jobsService = NullableJobsService(LiveNullableJobs())
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
            retrievedJobs.filter { it.salary > minimumSalary }
        } catch (e: Exception) {
            listOf()
        }
    }
}

interface NullableJobs {
    fun findAll(): List<Job>?
}

class LiveNullableJobs : NullableJobs {
    override fun findAll(): List<Job>? = null
}

class NullableJobsService(private val jobs: NullableJobs) {
    fun getHighlyPaidJobs(minimumSalary: Salary): List<Job> =
        jobs.findAll()?.filter { it.salary > minimumSalary } ?: listOf()

    fun getJobsByCompanyMap(): Map<String, List<Job>> {
        val jobs = jobs.findAll()
        return jobs?.let {
            it.groupBy { job -> job.company.name }
        } ?: return mapOf()
    }
}

data class Job(val company: Company, val role: Role, val salary: Salary)

@JvmInline
value class Company(val name: String)

@JvmInline
value class Role(val name: String)

@JvmInline
value class Salary(val value: Double) {
    operator fun compareTo(other: Salary): Int = value.compareTo(other.value)
}

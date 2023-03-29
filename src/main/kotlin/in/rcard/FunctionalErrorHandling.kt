package `in`.rcard

fun main() {
    val companiesService = CompaniesService(FakeCompanies())
    val availableCompaniesNames = companiesService.getAvailableCompaniesNames()
    println("Available companies are: $availableCompaniesNames")
}

interface Companies {
    fun findAll(): List<Company>
}

class FakeCompanies : Companies {
    override fun findAll(): List<Company> = throw RuntimeException("Boom!")
}

class CompaniesService(private val companies: Companies) {
    fun getAvailableCompaniesNames(): List<String> {
        val retrievedCompanies = companies.findAll()
        return try {
            retrievedCompanies.map { it.name }
        } catch (e: Exception) {
            listOf()
        }
    }
}

data class Company(val name: String)

package tranlong5252.fakebookapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tranlong5252.fakebookapi.db.AccountRepository
import tranlong5252.fakebookapi.dto.accounts.AccountResponseDto
import tranlong5252.fakebookapi.dto.accounts.CreateAccountDto
import tranlong5252.fakebookapi.exception.NotFoundException
import tranlong5252.fakebookapi.exception.ValidationError
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.module.CryptoService
import java.util.*


@Service
class AccountService {
    @Autowired
    lateinit var cryptoService: CryptoService

    @Autowired
    private lateinit var repository: AccountRepository

    fun createAccount(dto: CreateAccountDto) : AccountResponseDto {
        val errors = mutableListOf<ValidationError>()
        val response = AccountResponseDto()
        print(dto.username)
        print(dto.password)

        val results = listOf(
            this.repository.findAll().map { it }.any { it.username == dto.username }
        )

        if (results.isNotEmpty() && results[0]) {
            val error = ValidationError()
            error.property = "username"
            error.constraints = mapOf("isUnique" to "Username already existed!")
            errors.add(error)
        }

        if (errors.isNotEmpty()) {
            throw errors[0]
        }

        val account = this.repository.save(Account().apply {
            this.id = UUID.randomUUID().toString()
            this.username = dto.username
            this.password = cryptoService
                .crypto(dto.password)
        })
        return response.apply {
            this.id = account.id
            this.username = account.username
        }
    }

    fun getAccountById(id: String): Account {
        val account = this.repository.findById(id)
        if (account.isEmpty) {
            throw NotFoundException("Account not found")
        }
        return account.get()
    }

    fun getAccountByUsername(username: String): Account {
        val account = this.repository.getAccountByUsername(username)
        return account
    }
}

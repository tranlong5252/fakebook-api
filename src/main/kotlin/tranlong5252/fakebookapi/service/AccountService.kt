package tranlong5252.fakebookapi.accounts

import tranlong5252.fakebookapi.db.AccountRepository
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service
import tranlong5252.fakebookapi.crypto.CryptoService
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.exception.NotFoundException
import tranlong5252.fakebookapi.exception.ValidationError
import java.util.*

@Service
class AccountService {
    private var cryptoService: CryptoService? = null
    private var repository: AccountRepository? = null

    init {
        this.cryptoService = CryptoService()
        this.repository = AccountRepository()
    }

    fun createAccount(registerRequestDto: RegisterRequestDto): RegisterResponseDto {
        val errors = mutableListOf<ValidationError>()

        val results = listOf(
            this.repository?.exists(Example.of(Account().apply { this.username = registerRequestDto.username }))
        )

        if (results.isNotEmpty() && results[0] == true) {
            val error = ValidationError()
            error.property = "username"
            error.constraints = mapOf("isUnique" to "Username already existed!")
            errors.add(error)
        }

        if (errors.isNotEmpty()) {
            throw errors[0]
        }

        val account = this.repository!!.save(Account().apply {
            this.id = UUID.randomUUID().toString()
            this.username = registerRequestDto.username
            this.password = cryptoService!!.signSomething(registerRequestDto.password!!)
        })
        return RegisterResponseDto().apply {
            this.id = account.id
            this.username = account.username
        }
    }

    fun getAccountById(id: String): Account {
        val account = this.repository?.findOne(Example.of(Account().apply { this.id = id }))
        if (account == null || account.isEmpty) {
            throw NotFoundException("Account not found");
        }
        return account.get();
    }

    fun getAccountByUsername(username: String): Account {
        val account = this.repository?.findOne(Example.of(Account().apply { this.username = username }))
        if (account == null || account.isEmpty) {
            throw NotFoundException("Account not found");
        }
        return account.get();
    }
}

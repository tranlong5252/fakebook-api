package tranlong5252.fakebookapi.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import tranlong5252.fakebookapi.db.AccountRepository
import tranlong5252.fakebookapi.dto.accounts.AccountDetailDto
import tranlong5252.fakebookapi.dto.accounts.AccountResponseDto
import tranlong5252.fakebookapi.dto.accounts.CreateAccountDto
import tranlong5252.fakebookapi.dto.auth.AccountLoginResponseDto
import tranlong5252.fakebookapi.exception.NotFoundException
import tranlong5252.fakebookapi.exception.UnauthorizedException
import tranlong5252.fakebookapi.exception.ValidationError
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.model.AccountDetail
import tranlong5252.fakebookapi.module.CryptoService
import java.util.*


@Service
class AccountService {
    @Autowired
    lateinit var cryptoService: CryptoService

    @Autowired
    lateinit var repository: AccountRepository

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
            this.detail = AccountDetail().apply {
                this.email = dto.detail!!.email
                this.fname = dto.detail!!.fname
                this.lname = dto.detail!!.lname
                this.age = dto.detail!!.age
            }
        })
        return response.apply {
            this.id = account.id
            this.username = account.username
            this.detail = AccountDetailDto().apply {
                this.email = account.detail!!.email
                this.fname = account.detail!!.fname
                this.lname = account.detail!!.lname
                this.age = account.detail!!.age
            }
        }
    }

    fun getAccountById(id: String): Account {
        return  this.repository.findById(id).get() ?: throw NotFoundException("Account not found")
    }

    fun getAccountByUsername(username: String): Account {
        return this.repository.getAccountByUsername(username) ?: throw NotFoundException("Account not found")
    }
}

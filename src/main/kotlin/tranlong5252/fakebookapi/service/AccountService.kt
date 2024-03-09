package tranlong5252.fakebookapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tranlong5252.fakebookapi.db.AccountRepository
import tranlong5252.fakebookapi.dto.accounts.AccountDetailDto
import tranlong5252.fakebookapi.dto.accounts.AccountResponseDto
import tranlong5252.fakebookapi.dto.accounts.CreateAccountDto
import tranlong5252.fakebookapi.exception.FakebookException
import tranlong5252.fakebookapi.exception.entity.ValidationError
import tranlong5252.fakebookapi.exception.errors.EntityNotFoundErrorReport
import tranlong5252.fakebookapi.exception.errors.ValidationErrorReport
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
            val error = ValidationError(
                "username",
                mapOf("isUnique" to "Username already existed!"),
                "Username already existed!"
            )
            errors.add(error)
        }

        if (errors.isNotEmpty()) {
            throw FakebookException(ValidationErrorReport(errors))
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

    fun getAccountById(id: String) = this.repository.findById(id).get()

    fun getAccountByUsername(username: String): Account? {
        return this.repository.getAccountByUsername(username)
    }
}

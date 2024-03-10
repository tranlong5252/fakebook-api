package tranlong5252.fakebookapi.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import tranlong5252.fakebookapi.db.AccountRepository
import tranlong5252.fakebookapi.dto.GetManyResponse
import tranlong5252.fakebookapi.dto.PageRequestDto
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

    fun updateDetail(id: String, detailDto: AccountDetailDto): AccountResponseDto? {
        val account = this.repository.findById(id)
        if (account.isEmpty) {
            throw FakebookException(EntityNotFoundErrorReport("id", id))
        }
        val entity = account.get()
        entity.detail = detailDto.toEntity()
        this.repository.save(entity)
        return AccountResponseDto().apply {
            this.id = entity.id
            this.username = entity.username
            this.detail = AccountDetailDto().apply {
                this.email = entity.detail!!.email
                this.fname = entity.detail!!.fname
                this.lname = entity.detail!!.lname
                this.age = entity.detail!!.age
            }
        }
    }

    fun getAccounts(request: HttpServletRequest, page: PageRequestDto): GetManyResponse<AccountResponseDto> {
        val pageRequest = PageRequest.of(page.page, page.take)
        val accounts = this.repository.findAll(pageRequest)
        val records = this.repository.count()
        val totalPage = records / page.take
        val nextPage = if (page.page < totalPage) page.page + 1 else page.page
        val data = accounts.map {
            AccountResponseDto().apply {
                this.id = it.id
                this.username = it.username
                this.detail = AccountDetailDto().apply {
                    this.email = it.detail!!.email
                    this.fname = it.detail!!.fname
                    this.lname = it.detail!!.lname
                    this.age = it.detail!!.age
                }
            }
        }
        return GetManyResponse(page.page, page.take, data.toList(), records.toInt(), totalPage.toInt(), nextPage)
    }
}

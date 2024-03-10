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
import tranlong5252.fakebookapi.module.CryptoService
import tranlong5252.fakebookapi.utils.accountResponse
import tranlong5252.fakebookapi.utils.newAccount
import java.util.*
import kotlin.math.ceil


@Service
class AccountService {
    @Autowired
    lateinit var cryptoService: CryptoService

    @Autowired
    lateinit var repository: AccountRepository

    fun createAccount(dto: CreateAccountDto) : AccountResponseDto {
        val errors = mutableListOf<ValidationError>()

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
        val account = this.repository.save(
            newAccount(
                UUID.randomUUID().toString(),
                dto.username,
                cryptoService.crypto(dto.password),
                dto.detail!!.toEntity()
            )
        )
        return accountResponse(account)
    }

    fun updateDetail(id: String, detailDto: AccountDetailDto): AccountResponseDto? {
        val optional = this.repository.findById(id)
        if (optional.isEmpty) {
            throw FakebookException(EntityNotFoundErrorReport("id", id))
        }
        val account = optional.get()
        account.detail = detailDto.toEntity()
        this.repository.save(account)
        return accountResponse(account)
    }

    fun getAccounts(request: HttpServletRequest, dto: PageRequestDto): GetManyResponse<AccountResponseDto> {
        val page = if (dto.page < 1) 0 else dto.page - 1
        val pageRequest = PageRequest.of(page, dto.take)
        val accounts = this.repository.findAll(pageRequest)
        val records = this.repository.count()
        val totalPage = ceil(records.toDouble() / dto.take).toInt()
        val nextPage = if (page < totalPage) page + 1 else null
        val prevPage = if (page > 0) page - 1 else null
        val data = accounts.map {
            accountResponse(it)
        }
        return GetManyResponse(page, dto.take, data.toList(), records.toInt(), totalPage.toInt(), nextPage , prevPage)
    }
}

package tranlong5252.fakebookapi.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tranlong5252.fakebookapi.dto.accounts.AccountDetailDto
import tranlong5252.fakebookapi.dto.accounts.AccountResponseDto
import tranlong5252.fakebookapi.service.AccountService
import tranlong5252.fakebookapi.dto.accounts.CreateAccountDto
import tranlong5252.fakebookapi.dto.GetManyResponse
import tranlong5252.fakebookapi.dto.PageRequestDto
import tranlong5252.fakebookapi.service.AuthService

@RestController
@RequestMapping("/accounts")
class AccountController {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var authService: AuthService

    @PostMapping("/")
    fun createAccount(@RequestBody registerDto: CreateAccountDto): ResponseEntity<AccountResponseDto> {
        val response = accountService.createAccount(registerDto)
        return ResponseEntity.ok().body(response)
    }

    //return all accounts
    @GetMapping("/")
    fun getAccounts(request: HttpServletRequest, @RequestBody page: PageRequestDto): ResponseEntity<GetManyResponse<AccountResponseDto>> {
        val header = request.getHeader("Authorization")
        val account = authService.verifyAccessToken(header)

        val response = accountService.getAccounts(request, page)
        return ResponseEntity.ok().body(response)
    }

    @PutMapping("/detail")
    fun updateDetail(request: HttpServletRequest, @RequestBody detailDto: AccountDetailDto): ResponseEntity<AccountResponseDto> {
        val userAgent = request.getHeader("Authorization")
        val account = authService.verifyAccessToken(userAgent)
        val response = accountService.updateDetail(account.id, detailDto)
        return ResponseEntity.ok().body(response)
    }
}
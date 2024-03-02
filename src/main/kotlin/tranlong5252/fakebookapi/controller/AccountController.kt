package tranlong5252.fakebookapi.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tranlong5252.fakebookapi.service.AccountService
import tranlong5252.fakebookapi.dto.accounts.CreateAccountDto

@RestController
@RequestMapping("/accounts")
class AccountController {

    @Autowired
    private lateinit var accountService: AccountService

    @PostMapping("/")
    fun createAccount(@RequestBody registerDto: CreateAccountDto): ResponseEntity<Map<String, String?>> {
        val response = accountService.createAccount(registerDto)
        return ResponseEntity.ok().body(response)
    }
}
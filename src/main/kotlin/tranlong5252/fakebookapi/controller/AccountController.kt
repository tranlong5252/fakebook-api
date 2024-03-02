package tranlong5252.fakebookapi.accounts

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController {

    @Autowired
    private lateinit var accountService: AccountService

    @PostMapping("/")
    fun createAccount(@RequestBody registerDto: RegisterRequestDto): ResponseEntity<RegisterResponseDto>  {
        val response = accountService.createAccount(registerDto)
        return ResponseEntity.ok().body(response)
    }
}
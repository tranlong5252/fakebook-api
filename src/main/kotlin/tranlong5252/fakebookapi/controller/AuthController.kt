package tranlong5252.fakebookapi.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
internal class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @PostMapping("login-with-username-and-password")
    fun login(@RequestBody loginDto: LoginWithUsernameAndPasswordDto): ResponseEntity<LoginResponseDto> {

        val response = authService.loginWithUsernameAndPassword(loginDto)
        val headers = HttpHeaders()
        headers.add("content-type", "application/json")
        return ResponseEntity.ok().headers(headers).body(response)
    }
}


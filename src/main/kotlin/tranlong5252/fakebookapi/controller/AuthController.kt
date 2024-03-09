package tranlong5252.fakebookapi.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tranlong5252.fakebookapi.dto.accounts.AccountResponseDto
import tranlong5252.fakebookapi.dto.auth.AccountLoginResponseDto
import tranlong5252.fakebookapi.dto.auth.LoginWithUsernameAndPasswordDto
import tranlong5252.fakebookapi.service.AuthService

@RestController
@RequestMapping("/auth")
internal class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @PostMapping("login-with-username-and-password")
    fun login(@RequestBody loginDto: LoginWithUsernameAndPasswordDto): ResponseEntity<AccountLoginResponseDto> {
        val response = authService.loginWithUsernameAndPassword(loginDto)
        val headers = HttpHeaders()
        headers.add("content-type", "application/json")
        return ResponseEntity.ok().headers(headers).body(response)
    }

    @GetMapping("/profile")
    fun profile(request: HttpServletRequest): ResponseEntity<AccountResponseDto> {
        //get header
        val userAgent = request.getHeader("Authorization")
        val response = authService.verifyAccessToken(userAgent)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/login-with-google")
    fun loginWithGoogle(@RequestParam credential: String): ResponseEntity<AccountLoginResponseDto> {
        val response = authService.loginWithGoogle(credential)
        val headers = HttpHeaders()
        headers.add("content-type", "application/json")
        return ResponseEntity.ok().headers(headers).body(response)
    }
}


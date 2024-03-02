package tranlong5252.fakebookapi.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tranlong5252.fakebookapi.dto.auth.LoginWithUsernameAndPasswordDto
import tranlong5252.fakebookapi.service.AuthService

@RestController
@RequestMapping("/auth")
internal class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @PostMapping("login-with-username-and-password")
    fun login(@RequestBody loginDto: LoginWithUsernameAndPasswordDto): ResponseEntity<Map<String, String?>> {
        try {
            val response = authService.loginWithUsernameAndPassword(loginDto)
            val headers = HttpHeaders()
            headers.add("content-type", "application/json")
            return ResponseEntity.ok().headers(headers).body(response)
        } catch (e: Exception) {
            val headers = HttpHeaders()
            headers.add("content-type", "application/json")
            return ResponseEntity.status(401).headers(headers).body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/profile")
    fun profile(request: HttpServletRequest): ResponseEntity<Map<String, String?>> {
        try {
            //get header
            val userAgent = request.getHeader("Authorization")
            val response = authService.verifyAccessToken(userAgent)
            return ResponseEntity.ok().body(response)
        } catch (e: Exception) {
            val headers = HttpHeaders()
            headers.add("content-type", "application/json")
            return ResponseEntity.status(401).headers(headers).body(mapOf("error" to e.message))
        }
    }
}


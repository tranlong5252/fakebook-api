package tranlong5252.fakebookapi.auth

import org.springframework.stereotype.Service

@Service
class AuthService {
    fun loginWithUsernameAndPassword(loginWithUsernameAndPasswordDto: LoginWithUsernameAndPasswordDto): LoginResponseDto {
        val response = LoginResponseDto()
        response.accessToken = "user access token"
        return response
    }
}

package tranlong5252.fakebookapi.dto.auth

import jakarta.validation.constraints.NotEmpty

class LoginWithUsernameAndPasswordDto {
    @NotEmpty
    lateinit var username: String private set
    @NotEmpty
    lateinit var password: String  private set
}
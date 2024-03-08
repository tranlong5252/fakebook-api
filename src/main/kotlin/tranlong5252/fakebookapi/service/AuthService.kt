package tranlong5252.fakebookapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tranlong5252.fakebookapi.dto.accounts.AccountResponseDto
import tranlong5252.fakebookapi.dto.auth.AccountLoginResponseDto
import tranlong5252.fakebookapi.dto.auth.LoginWithUsernameAndPasswordDto
import tranlong5252.fakebookapi.exception.UnauthorizedException
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.module.CryptoService

@Service
class AuthService {
    @Autowired
    lateinit var cryptoService: CryptoService

    @Autowired
    private lateinit var accountService: AccountService

    fun loginWithUsernameAndPassword(dto: LoginWithUsernameAndPasswordDto): AccountLoginResponseDto {
        val account = accountService.getAccountByUsername(dto.username)
        if (!cryptoService.verify(dto.password, account.password)) {
            throw UnauthorizedException("Incorrect password!");
        }
        return AccountLoginResponseDto().apply {
            this.accessToken = cryptoService.signJwt(account.id)
        }
    }

    fun verifyAccessToken(accessToken: String) : AccountResponseDto {
        val account: Account
        try {
            val accountId = this.cryptoService.verifyJwt(accessToken.replace("Bearer ", ""))
            account = this.accountService.getAccountById(accountId)
            return AccountResponseDto().apply {
                this.id = account.id
                this.username = account.username
            }
        } catch (err : Exception) {
            if (err is UnauthorizedException) {
                throw UnauthorizedException("Invalid access token")
            } else {
                println(err)
            }
        }
        throw UnauthorizedException("Invalid access token")
    }

}

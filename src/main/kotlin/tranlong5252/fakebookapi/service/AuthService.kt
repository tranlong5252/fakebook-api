package tranlong5252.fakebookapi.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import tranlong5252.fakebookapi.dto.accounts.AccountDetailDto
import tranlong5252.fakebookapi.dto.accounts.AccountResponseDto
import tranlong5252.fakebookapi.dto.auth.AccountLoginResponseDto
import tranlong5252.fakebookapi.dto.auth.LoginWithUsernameAndPasswordDto
import tranlong5252.fakebookapi.exception.UnauthorizedException
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.model.AccountDetail
import tranlong5252.fakebookapi.module.CryptoService
import java.util.*

@Service
class AuthService {
    @Autowired
    lateinit var cryptoService: CryptoService

    @Autowired
    private lateinit var accountService: AccountService

    fun loginWithUsernameAndPassword(dto: LoginWithUsernameAndPasswordDto): AccountLoginResponseDto {
        val account = accountService.getAccountByUsername(dto.username)
        if (!cryptoService.verify(dto.password, account.password)) {
            throw UnauthorizedException("Incorrect password!")
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
                this.detail = account.detail?.let {
                    AccountDetailDto().apply {
                        this.email = it.email
                        this.fname = it.fname
                        this.lname = it.lname
                        this.age = it.age
                    }
                }
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

    @Value("\${google.clientId}")
    private lateinit var clientId: String

    fun loginWithGoogle(@RequestParam credential: String): AccountLoginResponseDto {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(Collections.singletonList(clientId))
            .build()

        val idToken = verifier.verify(credential)
        if (idToken != null) {
            val payload = idToken.payload
            val email = payload.email
            val account = this.accountService.repository.findByEmail(email)
            if (account == null) {
                val newAccount = this.accountService.repository.save(Account().apply {
                    this.id = UUID.randomUUID().toString()
                    this.username = email
                    this.email = email
                    this.detail = AccountDetail().apply {
                        this.email = email
                        val name = payload.getValue("name").toString()
                        this.lname = name.split(" ")[0]
                        this.fname = name.split(" ")[1]
                    }
                })
                return AccountLoginResponseDto().apply {
                    this.accessToken = cryptoService.signJwt(newAccount.id)
                }
            }
            return AccountLoginResponseDto().apply {
                this.accessToken = cryptoService.signJwt(account.id)
            }
        } else {
            throw UnauthorizedException("Invalid credential")
        }
    }
}

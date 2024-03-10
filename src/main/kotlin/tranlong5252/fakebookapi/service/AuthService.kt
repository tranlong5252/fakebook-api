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
import tranlong5252.fakebookapi.exception.FakebookException
import tranlong5252.fakebookapi.exception.errors.LoginErrorReport
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.model.AccountDetail
import tranlong5252.fakebookapi.module.CryptoService
import tranlong5252.fakebookapi.security.JwtProvider
import java.util.*

@Service
class AuthService {
    @Autowired
    lateinit var cryptoService: CryptoService

    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @Autowired
    private lateinit var accountService: AccountService

    fun loginWithUsernameAndPassword(dto: LoginWithUsernameAndPasswordDto): AccountLoginResponseDto {
        val account = accountService.getAccountByUsername(dto.username)?: throw FakebookException(
            LoginErrorReport(
                "Account not found", mapOf(
                    "username" to dto.username
                )
            )
        )
        if (!cryptoService.verify(dto.password, account.password)) {
            throw FakebookException(
                LoginErrorReport(
                    "Invalid username or password", mapOf(
                        "username" to dto.username,
                        "password" to dto.password
                    )
                )
            )
        }
        return AccountLoginResponseDto().apply {
            this.accessToken = jwtProvider.signToken(account)
        }
    }

    fun verifyAccessToken(accessToken: String) : AccountResponseDto {
        try {
            val account = this.jwtProvider.verifyToken(accessToken.replace("Bearer ", ""))
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
            throw FakebookException(LoginErrorReport("Invalid token", mapOf("token" to accessToken)))
        }
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
                    this.accessToken = jwtProvider.signToken(newAccount)
                }
            }
            return AccountLoginResponseDto().apply {
                this.accessToken = jwtProvider.signToken(account)
            }
        } else {
            throw FakebookException(LoginErrorReport("Invalid google credential", mapOf("credential" to credential)))
        }
    }
}

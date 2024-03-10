package tranlong5252.fakebookapi.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import tranlong5252.fakebookapi.exception.FakebookException
import tranlong5252.fakebookapi.exception.errors.EntityNotFoundErrorReport
import tranlong5252.fakebookapi.exception.errors.JwtErrorReport
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.service.AccountService
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtProviderImpl : JwtProvider {

    @Value("\${secrets.JWT_SECRET}")
    lateinit var jwtSecret: String
    @Value("\${secrets.JWT_EXPIRE}")
    lateinit var jwtExpire: String
    @Value("\${secrets.JWT_ISSUER}")
    lateinit var jwtIssuer: String
    @Value("\${secrets.JWT_TIMEZONE}")
    lateinit var jwtTimezone: String

    @Autowired
    lateinit var accountService: AccountService

    @Throws(FakebookException::class)
    override fun signToken(account: Account?): String {
        try {
            val algorithm = Algorithm.HMAC256(jwtSecret)
            val now = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of(jwtTimezone))
            return JWT.create()
                .withSubject(account?.id)
                .withIssuedAt(now.toInstant())
                .withExpiresAt(now.toInstant().plus(jwtExpire.toLong(), ChronoUnit.SECONDS))
                .withIssuer(jwtIssuer)
                .sign(algorithm)
        } catch (e: Exception) {
            throw FakebookException(
                JwtErrorReport("Sign token error", mapOf(
                    "accountInfo" to account,
                    "errorMessage" to e.message)
                )
            )
        }
    }

    @Throws(FakebookException::class)
    override fun verifyToken(token: String?): Account {
        try {
            val algorithm: Algorithm = Algorithm.HMAC256(jwtSecret)
            val payload: DecodedJWT = JWT.require(algorithm)
                .build()
                .verify(token)
            val accountId = payload.subject
            val findAccountResult = accountService.repository.findById(accountId)
            if (findAccountResult.isEmpty) {
                throw FakebookException(EntityNotFoundErrorReport("id", accountId))
            }
            return findAccountResult.get()
        } catch (e: FakebookException) {
            throw e
        } catch (e: Exception) {
            throw FakebookException(JwtErrorReport("Verify token error", mapOf(
                "token" to token,
                "errorMessage" to e.message)
            ))
        }
    }
}
package tranlong5252.fakebookapi.security

import org.springframework.stereotype.Component
import tranlong5252.fakebookapi.exception.FakebookException
import tranlong5252.fakebookapi.model.Account

@Component
interface JwtProvider {
    @Throws(FakebookException::class)
    fun signToken(account: Account?): String
    @Throws(FakebookException::class)
    fun verifyToken(token: String?): Account
}
package tranlong5252.fakebookapi.security
import org.springframework.security.core.Authentication
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.utils.enums.AccountRole

class FakebookAuthentication(private val account: Account?) : Authentication {

    override fun getAuthorities() = if (account != null) listOf(FakebookAuthority.of(AccountRole.fromOrdinal(account.role))) else listOf()

    override fun getCredentials() = null

    override fun getDetails() = null

    override fun getPrincipal() = account

    override fun isAuthenticated(): Boolean {
        return account != null
    }

    @Throws(IllegalArgumentException::class)
    override fun setAuthenticated(isAuthenticated: Boolean) {
        throw IllegalArgumentException()
    }

    override fun getName(): String = Account::class.java.name
}
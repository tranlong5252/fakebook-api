package tranlong5252.fakebookapi.security

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import tranlong5252.fakebookapi.model.Account
import java.util.*

class FakebookAuditorAware : AuditorAware<Account> {
    override fun getCurrentAuditor(): Optional<Account> {
        val context = SecurityContextHolder.getContext()
        val auth = context.authentication
        try {
            val account = auth.principal as Account
            return Optional.of(account)
        } catch (e: Exception) {
        }
        return Optional.empty()
    }
}
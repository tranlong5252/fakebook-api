package tranlong5252.fakebookapi.security

import org.springframework.security.core.GrantedAuthority
import tranlong5252.fakebookapi.utils.enums.AccountRole

class FakebookAuthority private constructor(private val authority: String) : GrantedAuthority {
    companion object {
        fun of(role: AccountRole) = FakebookAuthority(role.value)
    }

    override fun getAuthority() = authority

}
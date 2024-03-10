package tranlong5252.fakebookapi.security

import org.springframework.security.core.GrantedAuthority
import tranlong5252.fakebookapi.utils.enums.AccountRoleEnum

class FakebookAuthority private constructor(authority: String) : GrantedAuthority {
    companion object {
        fun of(role: AccountRoleEnum) = FakebookAuthority(role.value)
    }

    private var authority: String? = authority

    override fun getAuthority(): String {
        return authority!!
    }

}
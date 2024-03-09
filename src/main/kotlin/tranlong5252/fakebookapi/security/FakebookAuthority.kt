package tranlong5252.fakebookapi.security

import org.springframework.security.core.GrantedAuthority
import tranlong5252.fakebookapi.utils.enums.AccountRoleEnum

class FakebookAuthority : GrantedAuthority {
    companion object {
        fun of(role: AccountRoleEnum): FakebookAuthority {
            return FakebookAuthority(role.getValue())
        }
    }

    private var authority: String? = null

    override fun getAuthority(): String {
        return authority!!
    }

    private constructor(authority : String) {
        this.authority = authority
    }
}
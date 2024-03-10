package tranlong5252.fakebookapi.dto.accounts

import tranlong5252.fakebookapi.dto.DtoBase
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.utils.enums.AccountRole

class AccountResponseDto : DtoBase<Account> {
    lateinit var id: String
    lateinit var username: String
    var role: Int = AccountRole.USER.ordinal
    lateinit var detail: AccountDetailDto

    override fun toEntity(): Account {
        val entity = Account()
        entity.id = this.id
        entity.username = this.username
        entity.role = this.role
        entity.detail = this.detail.toEntity()
        return entity
    }
}
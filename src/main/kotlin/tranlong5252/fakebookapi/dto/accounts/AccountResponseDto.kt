package tranlong5252.fakebookapi.dto.accounts

import tranlong5252.fakebookapi.dto.DtoBase
import tranlong5252.fakebookapi.model.Account

class AccountResponseDto : DtoBase<Account> {
    lateinit var id: String
    lateinit var username: String

    var detail: AccountDetailDto? = null

    override fun toEntity(): Account {
        val entity = Account()
        entity.id = this.id
        entity.username = this.username
        entity.detail = this.detail?.toEntity()
        return entity
    }
}
package tranlong5252.fakebookapi.dto.accounts

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import tranlong5252.fakebookapi.dto.DtoBase
import tranlong5252.fakebookapi.model.AccountDetail

class AccountDetailDto : DtoBase<AccountDetail> {

    lateinit var lname: String
    lateinit var fname: String

    @Min(1)
    @Max(120)
    var age: Int = 1

    @Email
    lateinit var email: String

    var avt: String? = null

    override fun toEntity(): AccountDetail {
        val entity = AccountDetail()
        entity.lname = this.lname
        entity.fname = this.fname
        entity.age = this.age
        entity.email = this.email
        entity.avatar = this.avt
        return entity
    }
}
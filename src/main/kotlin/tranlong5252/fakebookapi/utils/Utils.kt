package tranlong5252.fakebookapi.utils

import tranlong5252.fakebookapi.dto.accounts.AccountDetailDto
import tranlong5252.fakebookapi.dto.accounts.AccountResponseDto
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.model.AccountDetail

fun accountResponse(account: Account) = AccountResponseDto().apply {
    this.id = account.id
    this.username = account.username
    this.role = account.role
    this.detail = AccountDetailDto().apply {
        this.email = account.detail!!.email
        this.fname = account.detail!!.fname
        this.lname = account.detail!!.lname
        this.age = account.detail!!.age
    }
}

fun newAccount(id: String, username: String, password: String, detail: AccountDetail) = Account().apply {
    this.id = id
    this.username = username
    this.password = password
    this.detail = detail
}
package tranlong5252.fakebookapi.utils

import tranlong5252.fakebookapi.dto.accounts.AccountDetailDto
import tranlong5252.fakebookapi.dto.accounts.AccountResponseDto
import tranlong5252.fakebookapi.model.Account
import tranlong5252.fakebookapi.model.AccountDetail
import java.text.SimpleDateFormat
import java.util.*

fun accountResponse(account: Account) = AccountResponseDto().apply {
    this.id = account.id
    this.username = account.username
    this.role = account.role
    this.detail = AccountDetailDto().apply {
        this.email = account.detail!!.email
        this.fname = account.detail!!.fname
        this.lname = account.detail!!.lname
        this.age = account.detail!!.age
        this.avt = account.detail!!.avatar
    }
}

fun newAccount(username: String, password: String, detail: AccountDetail) = Account().apply {
    this.username = username
    this.password = password
    this.detail = detail
}


val format = "yyyy_MM_dd_HH_mm_ss"
fun dateToString(date: Date) = SimpleDateFormat(format).format(date)
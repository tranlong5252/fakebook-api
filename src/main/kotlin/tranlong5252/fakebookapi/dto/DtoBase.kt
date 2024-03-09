package tranlong5252.fakebookapi.dto

import tranlong5252.fakebookapi.model.AccountDetail

interface DtoBase<T> {
    fun toEntity(): T
}
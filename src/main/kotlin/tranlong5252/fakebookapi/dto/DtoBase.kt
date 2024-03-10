package tranlong5252.fakebookapi.dto

interface DtoBase<T> {
    fun toEntity(): T
}
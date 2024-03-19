package tranlong5252.fakebookapi.exception.entity

class ValidationError(
    val field: String? = null,
    val value: Any? = null,
    val message: String? = null,
) {
    companion object {
        fun of(field: String, value: Any, message: String): ValidationError {
            return ValidationError(field, value, message)
        }
    }
}
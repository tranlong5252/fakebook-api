package tranlong5252.fakebookapi.exception.entity

class ValidationError(
    private val field: String? = null,
    private val value: Any? = null,
    private val message: String? = null,
)
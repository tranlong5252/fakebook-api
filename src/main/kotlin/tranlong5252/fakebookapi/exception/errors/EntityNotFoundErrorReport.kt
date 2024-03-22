package tranlong5252.fakebookapi.exception.errors

class EntityNotFoundErrorReport(fieldName: String, value: String, message: String = "Entity not found") : ErrorReport<Map<String, String>>(
    message, mapOf(
        "field" to fieldName,
        "value" to value
    )
)
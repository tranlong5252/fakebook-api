package tranlong5252.fakebookapi.exception.errors

class EntityNotFoundErrorReport(fieldName: String, value: String) : ErrorReport<Map<String, String>>(
    "Entity not found", mapOf(
        "field" to fieldName,
        "value" to value
    )
)
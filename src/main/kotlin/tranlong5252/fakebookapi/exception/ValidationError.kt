package tranlong5252.fakebookapi.exception

class ValidationError : Exception() {
    override val message: String = "Validation error"

    var property: String? = null

    var constraints: Map<String, String>? = null

    override fun toString(): String {
        return "ValidationError(property=$property, constraints=$constraints)"
    }
}
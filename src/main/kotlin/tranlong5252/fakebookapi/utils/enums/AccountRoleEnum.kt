package tranlong5252.fakebookapi.utils.enums

enum class AccountRoleEnum(value: String) {

    ADMIN("admin"),
    USER("user");

    private val value = value

    fun getValue() = value
}

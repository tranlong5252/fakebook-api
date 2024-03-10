package tranlong5252.fakebookapi.utils.enums

enum class AccountRoleEnum(val value: String) {

    ADMIN("admin"),
    USER("user");

    companion object {
        fun fromValue(value: String): AccountRoleEnum {
            return entries.first { it.value == value }
        }
    }
}

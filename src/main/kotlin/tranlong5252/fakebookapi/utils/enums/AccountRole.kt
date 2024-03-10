package tranlong5252.fakebookapi.utils.enums

enum class AccountRole(val value: String) {

    ADMIN("admin"),
    USER("user");

    companion object {
        fun fromOrdinal(value: Int): AccountRole {
            return entries.first { it.ordinal == value }
        }
    }
}

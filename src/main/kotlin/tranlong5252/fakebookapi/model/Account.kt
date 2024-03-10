package tranlong5252.fakebookapi.model

import jakarta.persistence.*
import tranlong5252.fakebookapi.utils.enums.AccountRole

@Entity
@Table(name = "accounts", uniqueConstraints = [
    UniqueConstraint(columnNames = ["username"])
])
class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, name = "id")
    var id: String = ""
    @Column(nullable = false, name = "username")
    var username: String = ""
    @Column(nullable = false, name = "password")
    var password: String = ""
    @Column(nullable = false, name = "email")
    var email: String? = ""
    @JoinColumn(nullable = true, name = "detail")
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var detail: AccountDetail? = null

    @Column(nullable = false, name = "role")
    var role: Int = AccountRole.USER.ordinal
}
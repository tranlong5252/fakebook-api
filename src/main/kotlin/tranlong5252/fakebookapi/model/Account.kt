package tranlong5252.fakebookapi.model

import jakarta.persistence.*

@Entity
@Table(name = "accounts", uniqueConstraints = [
    UniqueConstraint(columnNames = ["username"])
])
class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String = ""
    @Column(nullable = false, name = "username")
    var username: String = ""
    @Column(nullable = false, name = "password")
    var password: String = ""
}
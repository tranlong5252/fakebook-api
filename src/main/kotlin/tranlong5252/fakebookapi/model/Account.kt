package tranlong5252.fakebookapi.db.entities

import jakarta.persistence.*

@Entity
@Table(name = "accounts", uniqueConstraints = [
    UniqueConstraint(columnNames = ["username"])
])
class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null
    @Column(nullable = false, name = "username")
    var username: String? = null
    @Column(nullable = false, name = "password")
    var password: String? = null
}
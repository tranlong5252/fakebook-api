package tranlong5252.fakebookapi.model

import jakarta.persistence.*

@Entity
class AccountDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    var id: Int = 0
    @Column(name = "lname")
    var lname: String = ""
    @Column(name = "fname")
    var fname: String = ""
    @Column(name = "age")
    var age: Int = 0
    @Column(name = "email")
    var email: String = ""
}

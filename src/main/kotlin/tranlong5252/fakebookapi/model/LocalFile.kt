package tranlong5252.fakebookapi.model

import jakarta.persistence.*
import java.util.*

@Entity
class LocalFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, name = "id")
    val id: String = UUID.randomUUID().toString()

    @Column(nullable = false, name = "disk_path")
    var diskPath: String = ""

    @Column(nullable = false, name = "file_name")
    var fileName: String = ""

    @Column(nullable = false, name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAt: Date = Date()
}
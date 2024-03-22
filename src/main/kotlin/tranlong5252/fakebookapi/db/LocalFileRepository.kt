package tranlong5252.fakebookapi.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import tranlong5252.fakebookapi.model.LocalFile

@Transactional(readOnly = true)
interface LocalFileRepository : JpaRepository<LocalFile, String> {
}
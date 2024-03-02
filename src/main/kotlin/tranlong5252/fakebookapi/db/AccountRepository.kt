package tranlong5252.fakebookapi.db

import org.springframework.data.domain.Example
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import tranlong5252.fakebookapi.model.Account

@Transactional(readOnly = true)
interface AccountRepository : JpaRepository<Account, String> {

    fun getAccountByUsername(username: String): Account {
        return this.findAll().map { it }.first { it.username == username }
    }
}
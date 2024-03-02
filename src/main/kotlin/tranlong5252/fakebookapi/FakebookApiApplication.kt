package tranlong5252.fakebookapi.bootloader

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
class FakebookApiApplication

fun main(args: Array<String>) {
    runApplication<FakebookApiApplication>(*args)
}

package config


import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @Value("\${spring.datasource.username}")
    private var username: String = "SA"

    @Value("\${spring.datasource.password}")
    private var password: String = ""

    @Bean
    fun getDataSource(): DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.username(username)
        dataSourceBuilder.password(password)
        return dataSourceBuilder.build()
    }
}
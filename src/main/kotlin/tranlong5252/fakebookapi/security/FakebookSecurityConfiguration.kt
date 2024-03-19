package tranlong5252.fakebookapi.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import tranlong5252.fakebookapi.utils.enums.AccountRole


@Configuration
@EnableWebSecurity
class FakebookSecurityConfiguration {
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf("*")
        config.allowedMethods = listOf("*")
        config.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf {
                it.disable()
            }
            .cors {
                it.configurationSource(corsConfigurationSource())
            }
            .addFilterBefore(authenticationTokenFilterBean(), AuthorizationFilter::class.java)
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(HttpMethod.GET, "/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/accounts/**")
                    .hasAuthority(AccountRole.ADMIN.value)
                    .requestMatchers(HttpMethod.PUT, "/accounts/**")
                    .hasAnyAuthority(AccountRole.ADMIN.value, AccountRole.USER.value)
                    .requestMatchers(HttpMethod.GET, "/dashboard/**")
                    .hasAuthority(AccountRole.ADMIN.value)

            }
            .headers {
                it.frameOptions { frame ->
                    frame.disable()
                }
            }

        return http.build()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationTokenFilterBean(): FakebookSecurityFilter {
        return FakebookSecurityFilter()
    }

}
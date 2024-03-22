package tranlong5252.fakebookapi.security

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import tranlong5252.fakebookapi.exception.FakebookException
import tranlong5252.fakebookapi.exception.errors.EntityNotFoundErrorReport
import tranlong5252.fakebookapi.exception.errors.ValidationErrorReport
import tranlong5252.fakebookapi.model.Account
import java.io.Serializable


class FakebookSecurityFilter : OncePerRequestFilter() {
    
    @Autowired
    private lateinit var jwtProvider : JwtProvider

    private fun getAccessToken(request: HttpServletRequest): String {
        var token = ""
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader != null) {
            val builder = StringBuilder(authorizationHeader)
            builder.delete(0, "Bearer ".length)
            token = builder.toString()
        }
        return token
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val context = SecurityContextHolder.getContext()
        var account : Account? = null
        try {
            account = jwtProvider.verifyToken(getAccessToken(request))
        } catch (e : FakebookException) {
            // Do nothing
        }
        val authentication = FakebookAuthentication(account)
        context.authentication = authentication
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            val writer = response.writer
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            val error: Map<String, Serializable?>
            val cause = e.cause
            if (cause is FakebookException) {
                when(val report = cause.report) {
                    is ValidationErrorReport -> {
                        val data = report.data
                        val dataStr = data.joinToString {
                            "${it.message}:\n\t${it.field}: ${it.value}\n"
                        }

                        error = mapOf(
                            "message" to report.message,
                            "data" to dataStr,
                        )
                    }
                    is EntityNotFoundErrorReport -> {
                        error = mapOf(
                            "message" to report.message,
                            "field" to report.data["field"],
                            "value" to report.data["value"]
                        )
                    }
                    else -> {
                        error = mapOf(
                            "message" to report.message,
                            "stackTrace" to e.stackTrace
                        )
                    }
                }
                convertObjectToJson(error)?.let { writer.write(it) }
                return
            }
            throw e

        }
    }

    @Throws(JsonProcessingException::class)
    fun convertObjectToJson(`object`: Any?): String? {
        if (`object` == null) {
            return null
        }
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(`object`)
    }
}
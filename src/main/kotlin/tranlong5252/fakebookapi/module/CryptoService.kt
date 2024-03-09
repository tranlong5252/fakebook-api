package tranlong5252.fakebookapi.module

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Component
class CryptoService {

    @Value("\${secrets.HMAC_SECRET}")
    lateinit var hMac: String

    @OptIn(ExperimentalEncodingApi::class)
    fun crypto(input: String): String {
        val secretKeySpec = SecretKeySpec(hMac.toByteArray(), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)
        val hmac = mac.doFinal(input.toByteArray())
        return Base64.encode(hmac)
    }

    fun verify(input: String, providedHmac: String): Boolean {
        val computedHmac = crypto(input)
        return computedHmac == providedHmac
    }
}
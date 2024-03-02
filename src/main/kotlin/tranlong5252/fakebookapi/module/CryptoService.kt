package tranlong5252.fakebookapi.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@Component
class CryptoService {
//    hmacSomething(something: string) {
//        const hmac = createHmac("sha256", Env.HMAC_SECRET);
//        hmac.update(something);
//        return hmac.digest("base64");
//    }
//
//    signSomething(something: string) {
//        const sign = createSign("sha256");
//        sign.update(something);
//        return sign.sign(this.privateKey).toString("base64");
//    }
//
//    verifySomething(something: string, signature: string) {
//        const verify = createVerify("sha256");
//        verify.update(something);
//        return verify.verify(this.publicKey, Buffer.from(signature, "base64"));
//    }
//
//    signJwt(subject: string) {
//        return jwt.sign({}, Env.JWT_SECRET, {
//            subject: subject,
//            issuer: Env.JWT_ISSUER,
//            expiresIn: Env.JWT_EXPIRE
//        });
//    }
//
//    verifyJwt(token: string) {
//        const subject = jwt.verify(token, Env.JWT_SECRET, {
//            issuer: Env.JWT_ISSUER,
//        }).sub;
//        return typeof subject == "string" ? subject : "";
//    }


    @Value("\${secrets.HMAC_SECRET}")
    lateinit var hMac: String
    @Value("\${secrets.JWT_SECRET}")
    lateinit var jwtSecret: String
    @Value("\${secrets.JWT_EXPIRE}")
    lateinit var jwtExpire: String
    @Value("\${secrets.JWT_ISSUER}")
    lateinit var jwtIssuer: String

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

    fun signJwt(subject: String) : String{
        val claims = JWT.create()
            .withSubject(subject)
            .withIssuer(jwtIssuer)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtExpire.toLong()))
            .sign(Algorithm.HMAC256(jwtSecret))
        return claims
    }

    fun verifyJwt(token: String) : Boolean {
        val jwt = JWT.require(Algorithm.HMAC256(jwtSecret))
            .withIssuer(jwtIssuer)
            .build()
        val decoded = jwt.verify(token)
        return decoded.subject != null
    }
}
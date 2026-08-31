import java.util.Base64

fun main() {
    val publishableKey = "pk_test_cmVsaWV2ZWQtbWlkZ2UtNTMwNS5jbGVyay5hY2NvdW50cy5kZXYk"
    val base64Part = publishableKey.substringAfter("_").substringAfter("_").trimEnd('$')
    val decodedBytes = Base64.getUrlDecoder().decode(base64Part)
    val decodedString = String(decodedBytes, Charsets.UTF_8).trimEnd('$')
    println("https://" + decodedString + "/")
}

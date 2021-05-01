package encryption

import java.io.File
import java.math.BigInteger
import kotlin.experimental.xor

fun main(args: Array<String>) {
    Parser.main(args)
}

fun handler(encrypt: String, decrypt: String, inputFile: String, outputFile: String?) {
    val inputString = File(inputFile).readText()
    val outputStream = File(outputFile ?: inputFile).bufferedWriter()
    outputStream.use {
        val newString = if (encrypt.isNotEmpty()) {
            val byteEncrypt = BigInteger(encrypt, 16).toString().toByteArray()
            encrypt(inputString, byteEncrypt)
        } else {
            val byteDecrypt = BigInteger(decrypt, 16).toString().toByteArray()
            decrypt(inputString, byteDecrypt)
        }
        it.write(newString)
        println(if (encrypt.isNotEmpty()) "Encrypt is successful" else "Decrypt is successful")
    }
}

fun encrypt(text: String, byteKey: ByteArray): String {
    val byteText = text.toByteArray(Charsets.UTF_8)
    val result = ByteArray(byteText.size)
    for (i in byteText.indices) {
        result[i] = byteText[i] xor byteKey[i % byteKey.size]
    }
    return result.toString(Charsets.UTF_8)
}

fun decrypt(text: String, byteKey: ByteArray): String {
    val byteText = text.toByteArray(Charsets.UTF_8)
    val res = ByteArray(byteText.size)
    for (i in byteText.indices) {
        res[i] = (byteText[i] xor byteKey[i % byteKey.size])
    }
    return String(res)
}
package test

import encryption.encrypt
import encryption.main
import org.junit.Test
import junit.framework.Assert.*
import org.junit.Assert.assertThrows
import java.io.File

private fun equal (x:ByteArray, c: ByteArray): Boolean {
    for (i in x.indices) {
        if (x[i] != c[i]) return false
    }
    return true
}

private fun fileEqualityCheck(expectedFile: String, currentFile: String): Boolean {
    val expected = File(expectedFile).readText()
    val current = File(currentFile).readText()
    for (i in current.indices) {
        if (expected[i] != current[i]) return false
    }
    return true
}

class Tests {
    @Test
    fun mainTest() {
        assertThrows(IllegalArgumentException::class.java) {
            main("cipxor -c input/Encode/1_input.txt -o output/Encode/1_output.txt".split(" ").toTypedArray())
        }
        assertThrows(IllegalArgumentException::class.java) {
            main("cixor -c 168 input/Encode/1_input.txt -o output/Encode/1_output.txt".split(" ").toTypedArray())
        }
        assertThrows(IllegalArgumentException::class.java) {
            main("cipxor -c 168 -o output/Encode/1_output.txt".split(" ").toTypedArray())
        }
        assertThrows(IllegalArgumentException::class.java) {
            main("cipxor -c 168 -d 168 input/Encode/1_input.txt -o output/Encode/1_output.txt".split(" ").toTypedArray())
        }
        assertThrows(NumberFormatException::class.java) {
            main("cipxor -c 5TK input/Encode/1_input.txt -o output/Encode/1_output.txt".split(" ").toTypedArray())
        }

        main("cipxor -c 168 input/Encode/1_input.txt -o output/Encode/1_output.txt".split(" ").toTypedArray())
        assertTrue(fileEqualityCheck("expected/1.txt", "output/Encode/1_output.txt"))
        main("cipxor -d 168 input/Decode/1_input.txt -o output/Decode/1_output.txt".split(" ").toTypedArray())
        assertTrue(fileEqualityCheck("input/Encode/1_input.txt", "output/Decode/1_output.txt"))

        main("cipxor -c 1A input/Encode/2_input.txt -o output/Encode/2_output.txt".split(" ").toTypedArray())
        assertTrue(fileEqualityCheck("expected/2.txt", "output/Encode/2_output.txt"))
        main("cipxor -d 1A input/Decode/2_input.txt -o output/Decode/2_output.txt".split(" ").toTypedArray())
        assertTrue(fileEqualityCheck("input/Encode/2_input.txt", "output/Decode/2_output.txt"))

        main("cipxor -c 12B input/Encode/3_input.txt -o output/Encode/3_output.txt".split(" ").toTypedArray())
        assertTrue(fileEqualityCheck("expected/3.txt", "output/Encode/3_output.txt"))
        main("cipxor -d 12B input/Decode/3_input.txt -o output/Decode/3_output.txt".split(" ").toTypedArray())
        assertTrue(fileEqualityCheck("input/Encode/3_input.txt", "output/Decode/3_output.txt"))

        main("cipxor -c FF input/Encode/4_input.txt -o output/Encode/4_output.txt".split(" ").toTypedArray())
        assertTrue(fileEqualityCheck("expected/4.txt", "output/Encode/4_output.txt"))
        main("cipxor -d FF input/Decode/4_input.txt -o output/Decode/4_output.txt".split(" ").toTypedArray())
        assertTrue(fileEqualityCheck("input/Encode/4_input.txt", "output/Decode/4_output.txt"))
    }

    @Test
    fun encode() {
        assertEquals(true, equal(byteArrayOf(0x00), encrypt("h", byteArrayOf(0x68)).toByteArray()))
        assertEquals(true, equal(byteArrayOf(0x04, 0x07, 0x06),
            encrypt("123", byteArrayOf(0x35)).toByteArray()))
        assertEquals(true, equal(byteArrayOf(0x00, 0x0D, 0x04, 0x04, 0x07),
            encrypt("hello", byteArrayOf(0x68)).toByteArray()))
        assertEquals(true, equal(
            byteArrayOf(0x00, 0x25, 0x01, 0x3B, 0x01, 0x36, 0x01, 0x35, 0x00, 0xF, 0x01, 0x39),
            encrypt("Привет", byteArrayOf(
                0xd0.toByte(),
                0xba.toByte(),
                0xD0.toByte(),
                0xbb.toByte(),
                0xD1.toByte(),
                0x8E.toByte(),
                0xD1.toByte(),
                0x87.toByte())).toByteArray())
        )
    }

    @Test
    fun decrypt() {
        assertEquals("h", encryption.decrypt(byteArrayOf(0x00).toString(Charsets.UTF_8), byteArrayOf(0x68)))
        assertEquals("123",
            encryption.decrypt(byteArrayOf(0x04, 0x07, 0x06).toString(Charsets.UTF_8), byteArrayOf(0x35))
        )
        assertEquals("hello",
            encryption.decrypt(byteArrayOf(0x00, 0x0D, 0x04, 0x04, 0x07).toString(Charsets.UTF_8), byteArrayOf(0x68))
        )
        assertEquals("Привет", encryption.decrypt(
            byteArrayOf(
                0x00, 0x25, 0x01, 0x3B, 0x01, 0x36, 0x01, 0x35, 0x00, 0xF, 0x01, 0x39
            ).toString(Charsets.UTF_8), byteArrayOf(
                0xd0.toByte(),
                0xba.toByte(),
                0xD0.toByte(),
                0xbb.toByte(),
                0xD1.toByte(),
                0x8E.toByte(),
                0xD1.toByte(),
                0x87.toByte()
            )
        )
        )
    }
}
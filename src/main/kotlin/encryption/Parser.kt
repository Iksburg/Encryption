package encryption

import org.kohsuke.args4j.*


class Parser {
    @Option(name = "-c", usage = "file key encryption", forbids = ["-d"])
    private var encrypt: String = ""

    @Option(name = "-d", usage = "file key decryption", forbids = ["-c"])
    private var decrypt: String = ""

    @Option(name = "-o", usage = "output to this file")
    private var output: String? = null

    @Argument
    var arguments: List<String> = ArrayList()

    private fun parseArgs(args: Array<String>) {
        val parser = CmdLineParser(this)
        try {
            parser.parseArgument(*args)
            if (arguments[0] != "cipxor" || arguments.size != 2 ||
                (encrypt.isNotEmpty() && decrypt.isNotEmpty()) || (encrypt.isEmpty() && decrypt.isEmpty())) {
                System.err.println("Invalid variables entered on the line.")
                System.err.println("Correct line example: ciphxor [-c key] [-d key] inputname.txt [-o outputname.txt]")
                throw IllegalArgumentException()
            }
        } catch (e: CmdLineException) {
            System.err.println(e.message)
            System.err.println("Correct line example: ciphxor [-c key] [-d key] inputname.txt [-o outputname.txt]")
            throw IllegalArgumentException()
        }
        val input = arguments[1]
        handler(encrypt, decrypt, input, output)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Parser().parseArgs(args)
        }
    }
}
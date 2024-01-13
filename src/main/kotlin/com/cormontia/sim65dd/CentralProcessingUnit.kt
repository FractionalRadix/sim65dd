package com.cormontia.sim65dd

import java.util.*
import kotlin.reflect.KFunction1

fun main() {
    val cpu = CentralProcessingUnit()
    val memory = program()
    cpu.mainLoop(memory, 0.toUShort())
}

/*
    From http://www.emulator101.com/6502-addressing-modes.html :

   LDA #$FF
   LDY #$09
   loop:
   STA $1000,Y ; absolute indexed addressing
   DEY
   BPL loop
 */

fun program(): Array<UByte> {
    val array = Array<UByte>(65536) { 0u }

    array[0] = 0xA9.toUByte()
    array[1] = 0xFF.toUByte()
    array[2] = 0xA0.toUByte()
    array[3] = 0x09.toUByte()
    array[4] = 0x99.toUByte()
    array[5] = 0x00.toUByte()
    array[6] = 0x10.toUByte()
    array[7] = 0x88.toUByte()
    array[8] = 0x10.toUByte()
    array[9] = 0xFA.toUByte() // -4 . Need to go to step 4. Question is: WHEN is the IP increased?
    return array
}


class CentralProcessingUnit {
    var acc: UByte = 0u
    var x: UByte = 0u
    var y: UByte = 0u
    var pc: UShort = 0u
    var sp: UByte = 255u //  UByte.MAX_VALUE

    var N: Boolean = false
    var Z: Boolean = false


    fun mainLoop(memory: Array<UByte>, startIp: UShort) {

        this.pc = startIp

        var opCode = memory[pc.toInt()]
        while (opCode != 0.toUByte() /* //TODO!~ */ ) {
            opCode = memory[pc.toInt()]
            val operand1 = memory[pc.inc().toInt()]
            val operand2 = memory[pc.inc().inc().toInt()]
            when (opCode.toInt()) {
                0x10 -> { bpl(operand1) }

                0x88 -> { dey() }

                0x99 -> { sta_absolute_indexed(operand1, operand2, memory) }

                0xA0 -> { ldy_immediate(operand1) }
                0xA1 -> { lda_indexed_indirect_x(operand1, memory) }
                0xA5 -> { lda_zero_page(operand1, memory) }
                0xA9 -> { lda_immediate(operand1) }
                0xAD -> { lda_absolute(operand1, operand2, memory) }

                0xB1 -> { lda_indirect_indexed_y(operand1, memory) }
                0xB5 -> { lda_zero_page_x(operand1, memory) }
                0xBD -> { lda_absolute_x(operand1, operand2, memory) }
                0xB9 -> { lda_absolute_y(operand1, operand2, memory) }

                else -> { println("Not yet implemented, or not an existing OPCode!")}
            }
        }

        for (i in 0 ..< 65536) {
            if (memory[i] == 255.toUByte()) {
                print(" $i")
            }
        }

    }

    // Load/Store operations

    // LDA

    private fun lda_immediate(param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA #\$$operand")

        acc = param
        N = (acc and 128u > 0u)
        Z = (acc.compareTo(0u) == 0)
        pc = pc.inc().inc()
    }

    private fun lda_zero_page(param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$operand")

        acc = memory[param.toInt()]
        N = (acc and 128u > 0u)
        Z = (acc.compareTo(0u) == 0)
        pc = pc.inc().inc()
    }

    private fun lda_zero_page_x(param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$operand, X")

        val newIndex = (param + x).toUByte() // Conversion to UByte, effectively the same as giving a "modulo 256".
        acc = memory[newIndex.toInt()]

        N = (acc and 128u > 0u)
        Z = (acc.compareTo(0u) == 0)
        pc = pc.inc().inc()
    }

    private fun lda_absolute(lsb: UByte, msb: UByte, memory: Array<UByte>) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$msbHex$lsbHex")

        val location = 256 * msb.toShort() + lsb.toShort()
        acc = memory[location]
        N = (acc and 128u > 0u)
        Z = (acc.compareTo(0u) == 0)
        pc = pc.inc().inc().inc()
    }

    private fun lda_absolute_x(lsb: UByte, msb: UByte, memory: Array<UByte>) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("LDA \$$msbHex$lsbHex,X")

        val location = (256 * msb.toShort() + lsb.toShort() + x.toShort()).toShort() // Conversion to short should be the same as "mod 65536".
        acc = memory[location.toInt()]

        N = (acc and 128u > 0u)
        Z = (acc.compareTo(0u) == 0)
        pc = pc.inc().inc().inc()
    }

    private fun lda_absolute_y(lsb: UByte, msb: UByte, memory: Array<UByte>) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("LDA \$$msbHex$lsbHex,Y")

        val location = (256 * msb.toShort() + lsb.toShort() + y.toShort()).toShort() // Conversion to short should be the same as "mod 65536".
        acc = memory[location.toInt()]

        N = (acc and 128u > 0u)
        Z = (acc.compareTo(0u) == 0)
        pc = pc.inc().inc().inc()
    }

    private fun lda_indexed_indirect_x(param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$($operand, X)")

        val location1 = (param + x).toUByte()
        val zeroPageValueLsb = memory[location1.toInt()]
        val zeroPageValueMsb = memory[location1.toInt().inc()]
        val location2 = (256u * zeroPageValueMsb.toUShort() + zeroPageValueLsb.toUShort()).toUShort()
        acc = memory[location2.toInt()]

        N = (acc and 128u > 0u)
        Z = (acc.compareTo(0u) == 0)
        pc = pc.inc().inc()
    }

    private fun lda_indirect_indexed_y(param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA $($operand),Y")

        val zeroPageValueLsb = memory[param.toInt()]
        val zeroPageValueMsb = memory[(param.inc()).toInt()]
        val location = (256u * zeroPageValueMsb + zeroPageValueLsb + y).toUShort()
        acc = memory[location.toInt()]

        N = (acc and 128u > 0u)
        Z = (acc.compareTo(0u) == 0)
        pc = pc.inc().inc()
    }

    // End LDA

    private fun sta_absolute_indexed(lsb: UByte, msb: UByte, memory: Array<UByte>) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$msbHex$lsbHex, Y")

        val location = 256 * msb.toShort() + lsb.toShort()
        memory[location] = acc
        pc = pc.inc()
        pc = pc.inc()
        pc = pc.inc()
    }


    private fun ldy_immediate(param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY #\$$operand")

        y = param
        N = (y and 128u > 0u)
        Z = (y.compareTo(0u) == 0)
        pc = pc.inc()
        pc = pc.inc()
    }

    private fun bpl(param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("BPL $operand")

        pc = pc.inc() // Now points at the instruction.

        //TODO!~ There has to be a better way to add a two's complement to a UByte...
        var counter = param
        while (counter > 0u) {
            pc = pc.inc()
            counter--
        }
    }

    private fun dex() {
        println("DEX")

        x = x.dec()
        N = (x and 128u > 0u)
        Z = (x.compareTo(0u) == 0)
        pc = pc.inc()
    }

    private fun inx() {
        println("INX")

        x = x.inc()
        N = (x and 128u > 0u)
        Z = (x.compareTo(0u) == 0)
        pc = pc.inc()
    }

    private fun dey() {
        println("DEY")

        y = y.dec()
        N = (y and 128u > 0u)
        Z = (y.compareTo(0u) == 0)
        pc = pc.inc()
    }

    private fun iny() {
        println("INY")

        y = y.inc()
        N = (y and 128u > 0u)
        Z = (y.compareTo(0u) == 0)
        pc = pc.inc()
    }


    //TODO?~ Use a Map from OPCodes to Instruction?
    fun instructionSet(): List<Instruction> {
        val instructionSet = mutableListOf<Instruction>()
        instructionSet.add(Instruction(0xA9.toUByte(), "LDA", ::lda_immediate, 2, false))
        instructionSet.add(Instruction(0xA0.toUByte(), "LDY", ::ldy_immediate, 2, false))
        //TODO!+ val iny = Instruction(0xC8.toUByte(), "INY", ::iny) ... etc
        return instructionSet
    }

}

//TODO?~
data class Instruction(
    val opCode: UByte,
    val mnemonic: String,
    val function: KFunction1<UByte, Unit>, //  Function<Unit>,
    val cycles: Int,
    val moreCycles: Boolean // true iff the nr of cycles given is the minimum nr of cycles, instead of a constant.
)


// NOTES.
// Little endian. http://www.emulator101.com/6502-addressing-modes.html
// "[...] JMP $4032 will set the PC to $4032.
//  The hex for this is 4C 32 40.
//  The 6502 is a little endian machine, so any 16 bit (2 byte) value is stored with the LSB first."


// PC (Program Counter) increased first   http://www.6502.org/tutorials/6502opcodes.html#PC
// "When the 6502 is ready for the next instruction it increments the program counter before fetching the instruction.
//  Once it has the op code, it increments the program counter by the length of the operand, if any."
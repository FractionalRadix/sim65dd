package com.cormontia.sim65dd

import java.util.*
import kotlin.reflect.KFunction1

//TODO!+ Add and use the other addressing modes. (That is, Indexed Indirect and Indirect Indexed... I think I got all the others now...)
//TODO?~ Should they return Int, as apparently our memory array requires Int instead of short for addressing...?
fun absolute(lsb: UByte, msb: UByte) = 256 * msb.toShort() + lsb.toShort()
fun absoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort() + cpu.x.toShort()).toShort() // Conversion to short should be the same as "mod 65536".
fun absoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort() + cpu.y.toShort()).toShort() // Conversion to short should be the same as "mod 65536".
fun zeroPage(param: UByte) = param
fun zeroPageX(cpu: CentralProcessingUnit, param: UByte) = (param + cpu.x).toUByte() // Conversion to UByte, effectively the same as giving a "modulo 256".
fun zeroPageY(cpu: CentralProcessingUnit, param: UByte) = (param + cpu.y).toUByte() // Conversion to UByte, effectively the same as giving a "modulo 256".

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
    var C: Boolean = false
    var V: Boolean = false


    fun mainLoop(memory: Array<UByte>, startIp: UShort) {

        this.pc = startIp

        var opCode = memory[pc.toInt()]
        while (opCode != 0.toUByte() /* //TODO!~ */ ) {
            opCode = memory[pc.toInt()]
            val operand1 = memory[pc.inc().toInt()]
            val operand2 = memory[pc.inc().inc().toInt()]
            when (opCode.toInt()) {
                0x10 -> { bpl(operand1) }

                0x61 -> { AddWithCarry().adcIndirectX(this, memory, operand1) }
                0x65 -> { AddWithCarry().adcZeroPage(this, memory, operand1) }
                0x69 -> { AddWithCarry().adcImmediate(this, operand1) }
                0x6D -> { AddWithCarry().adcAbsolute(this, memory, operand1, operand2) }

                0x71 -> { AddWithCarry().adcIndirectY(this, memory, operand1) }
                0x75 -> { AddWithCarry().adcZeroPageX(this, memory, operand1) }
                0x79 -> { AddWithCarry().adcAbsoluteY(this, memory, operand1, operand2) }
                0x7D -> { AddWithCarry().adcAbsoluteX(this, memory, operand1, operand2) }

                0x81 -> { LoadStoreAccumulator().staIndexedIndirectX(this, memory, operand1) }
                0x85 -> { LoadStoreAccumulator().staZeroPage(this, memory, operand1) }
                0x84 -> { LoadStoreYRegister().styZeroPage(this, memory, operand1) }
                0x86 -> { LoadStoreXRegister().stxZeroPage(this, memory, operand1) }
                0x88 -> { dey() }
                0x8A -> { TransferRegisters().txa(this) }
                0x8C -> { LoadStoreYRegister().styAbsolute(this, memory, operand1, operand2) }
                0x8D -> { LoadStoreAccumulator().staAbsolute(this, memory, operand1, operand2) }
                0x8E -> { LoadStoreXRegister().stxAbsolute(this, memory, operand1, operand2) }

                0x91 -> { LoadStoreAccumulator().staIndirectIndexedY(this, memory, operand1) }
                0x94 -> { LoadStoreYRegister().styZeroPageX(this, memory, operand1) }
                0x95 -> { LoadStoreAccumulator().staZeroPageX(this, memory, operand1) }
                0x96 -> { LoadStoreXRegister().stxZeroPageY(this, memory, operand1) }
                0x98 -> { TransferRegisters().tya(this) }
                0x99 -> { LoadStoreAccumulator().staAbsoluteY(this, memory, operand1, operand2) }
                0x9A -> { TransferRegisters().txs(this) }
                0x9D -> { LoadStoreAccumulator().staAbsoluteX(this, memory, operand1, operand2) }

                0xA0 -> { LoadStoreYRegister().ldyImmediate(this, operand1) }
                0xA1 -> { LoadStoreAccumulator().ldaIndexedIndirectX(this, memory, operand1) }
                0xA2 -> { LoadStoreXRegister().ldxImmediate(this, operand1) }
                0xA4 -> { LoadStoreYRegister().ldyZeroPage(this, memory, operand1) }
                0xA5 -> { LoadStoreAccumulator().ldaZeroPage(this, memory, operand1) }
                0xA6 -> { LoadStoreXRegister().ldxZeroPage(this, memory, operand1) }
                0xA8 -> { TransferRegisters().tay(this) }
                0xA9 -> { LoadStoreAccumulator().ldaImmediate(this, operand1) }
                0xAA -> { TransferRegisters().tax(this) }
                0xAC -> { LoadStoreYRegister().ldyAbsolute(this, memory, operand1, operand2) }
                0xAD -> { LoadStoreAccumulator().ldaAbsolute(this, memory, operand1, operand2) }
                0xAE -> { LoadStoreXRegister().ldxAbsolute(this, memory, operand1, operand2) }

                0xB1 -> { LoadStoreAccumulator().ldaIndirectIndexedY(this, memory, operand1) }
                0xB4 -> { LoadStoreYRegister().ldyZeroPageX(this, memory, operand1) }
                0xB5 -> { LoadStoreAccumulator().ldaZeroPageX(this, memory, operand1) }
                0xB6 -> { LoadStoreXRegister().ldxZeroPageY(this, memory, operand1) }
                0xB9 -> { LoadStoreAccumulator().ldaAbsoluteY(this, memory, operand1, operand2) }
                0xBA -> { TransferRegisters().tsx(this) }
                0xBC -> { LoadStoreYRegister().ldyAbsoluteX(this, memory, operand1, operand2) }
                0xBD -> { LoadStoreAccumulator().ldaAbsoluteX(this, memory, operand1, operand2) }
                0xBE -> { LoadStoreXRegister().ldxAbsoluteY(this, memory, operand1, operand2) }

                else -> { println("Not yet implemented, or not an existing OPCode!")}
            }
        }

        for (i in 0 ..< 65536) {
            if (memory[i] == 255.toUByte()) {
                print(" $i")
            }
        }

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
        //instructionSet.add(Instruction(0xA9.toUByte(), "LDA", ::lda_immediate, 2, false))
        //instructionSet.add(Instruction(0xA0.toUByte(), "LDY", ::ldy_immediate, 2, false))
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
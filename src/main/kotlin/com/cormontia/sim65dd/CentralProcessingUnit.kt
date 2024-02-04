package com.cormontia.sim65dd

import com.cormontia.sim65dd.old_operations.*
import java.util.*
import kotlin.reflect.KFunction1

//TODO?~ Should these methods return Int, as apparently our memory array requires Int instead of short for addressing...?
fun absolute(lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort()) % 65536
fun absoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort() + cpu.x.toShort()) % 65536
fun absoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort() + cpu.y.toShort()) % 65535
fun zeroPage(param: UByte) = param.toInt()
fun zeroPageX(cpu: CentralProcessingUnit, param: UByte) = ((param + cpu.x) % 256u).toInt()
fun zeroPageY(cpu: CentralProcessingUnit, param: UByte) = ((param + cpu.y) % 256u).toInt()
fun indirectIndexedY(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte): Int {
    val zeroPageValueLsb = memory[param.toInt()]
    val zeroPageValueMsb = memory[(param.inc()).toInt()]
    val location = (256u * zeroPageValueMsb + zeroPageValueLsb + cpu.y)
    return location.toInt()
}
fun indexedIndirectX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte): Int {
    val location1 = (param + cpu.x).toUByte()
    val zeroPageValueLsb = memory[location1.toInt()]
    val zeroPageValueMsb = memory[location1.toInt().inc()]
    val location2 = (256u * zeroPageValueMsb.toUShort() + zeroPageValueLsb.toUShort())
    return location2.toInt()
}

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
                0x06 -> { OLD_AslOperation().aslZeroPage(this, memory, operand1) }
                0x0A -> { OLD_AslOperation().aslImmediate(this) }
                0x0E -> { OLD_AslOperation().aslAbsolute(this, memory, operand1, operand2) }

                0x10 -> { bpl(operand1) }
                0x16 -> { OLD_AslOperation().aslZeroPageX(this, memory, operand1) }
                0x1E -> { OLD_AslOperation().aslAbsoluteX(this, memory, operand1, operand2) }

                0x21 -> { OLD_AndOperation().andIndexedIndirectX(this, memory, operand1) }
                0x25 -> { OLD_AndOperation().andZeroPage(this, memory, operand1) }
                0x29 -> { OLD_AndOperation().andImmediate(this, operand1) }
                0x2D -> { OLD_AndOperation().andAbsolute(this, memory, operand1, operand2) }

                0x31 -> { OLD_AndOperation().andIndirectIndexedY(this, memory, operand1) }
                0x35 -> { OLD_AndOperation().andZeroPageX(this, memory, operand1) }
                0x39 -> { OLD_AndOperation().andAbsoluteY(this, memory, operand1, operand2) }
                0x3D -> { OLD_AndOperation().andAbsoluteX(this, memory, operand1, operand2) }

                0x61 -> { OLD_AddWithCarry().adcIndexedIndirectX(this, memory, operand1) }
                0x65 -> { OLD_AddWithCarry().adcZeroPage(this, memory, operand1) }
                0x69 -> { OLD_AddWithCarry().adcImmediate(this, operand1) }
                0x6D -> { OLD_AddWithCarry().adcAbsolute(this, memory, operand1, operand2) }

                0x71 -> { OLD_AddWithCarry().adcIndirectIndexedY(this, memory, operand1) }
                0x75 -> { OLD_AddWithCarry().adcZeroPageX(this, memory, operand1) }
                0x79 -> { OLD_AddWithCarry().adcAbsoluteY(this, memory, operand1, operand2) }
                0x7D -> { OLD_AddWithCarry().adcAbsoluteX(this, memory, operand1, operand2) }

                0x81 -> { OLD_LoadStoreAccumulator().staIndexedIndirectX(this, memory, operand1) }
                0x85 -> { OLD_LoadStoreAccumulator().staZeroPage(this, memory, operand1) }
                0x84 -> { OLD_LoadStoreYRegister().styZeroPage(this, memory, operand1) }
                0x86 -> { OLD_LoadStoreXRegister().stxZeroPage(this, memory, operand1) }
                0x88 -> { dey() }
                0x8A -> { TransferRegisters().txa(this) }
                0x8C -> { OLD_LoadStoreYRegister().styAbsolute(this, memory, operand1, operand2) }
                0x8D -> { OLD_LoadStoreAccumulator().staAbsolute(this, memory, operand1, operand2) }
                0x8E -> { OLD_LoadStoreXRegister().stxAbsolute(this, memory, operand1, operand2) }

                0x91 -> { OLD_LoadStoreAccumulator().staIndirectIndexedY(this, memory, operand1) }
                0x94 -> { OLD_LoadStoreYRegister().styZeroPageX(this, memory, operand1) }
                0x95 -> { OLD_LoadStoreAccumulator().staZeroPageX(this, memory, operand1) }
                0x96 -> { OLD_LoadStoreXRegister().stxZeroPageY(this, memory, operand1) }
                0x98 -> { TransferRegisters().tya(this) }
                0x99 -> { OLD_LoadStoreAccumulator().staAbsoluteY(this, memory, operand1, operand2) }
                0x9A -> { TransferRegisters().txs(this) }
                0x9D -> { OLD_LoadStoreAccumulator().staAbsoluteX(this, memory, operand1, operand2) }

                0xA0 -> { OLD_LoadStoreYRegister().ldyImmediate(this, operand1) }
                0xA1 -> { OLD_LoadStoreAccumulator().ldaIndexedIndirectX(this, memory, operand1) }
                0xA2 -> { OLD_LoadStoreXRegister().ldxImmediate(this, operand1) }
                0xA4 -> { OLD_LoadStoreYRegister().ldyZeroPage(this, memory, operand1) }
                0xA5 -> { OLD_LoadStoreAccumulator().ldaZeroPage(this, memory, operand1) }
                0xA6 -> { OLD_LoadStoreXRegister().ldxZeroPage(this, memory, operand1) }
                0xA8 -> { TransferRegisters().tay(this) }
                0xA9 -> { OLD_LoadStoreAccumulator().ldaImmediate(this, operand1) }
                0xAA -> { TransferRegisters().tax(this) }
                0xAC -> { OLD_LoadStoreYRegister().ldyAbsolute(this, memory, operand1, operand2) }
                0xAD -> { OLD_LoadStoreAccumulator().ldaAbsolute(this, memory, operand1, operand2) }
                0xAE -> { OLD_LoadStoreXRegister().ldxAbsolute(this, memory, operand1, operand2) }

                0xB1 -> { OLD_LoadStoreAccumulator().ldaIndirectIndexedY(this, memory, operand1) }
                0xB4 -> { OLD_LoadStoreYRegister().ldyZeroPageX(this, memory, operand1) }
                0xB5 -> { OLD_LoadStoreAccumulator().ldaZeroPageX(this, memory, operand1) }
                0xB6 -> { OLD_LoadStoreXRegister().ldxZeroPageY(this, memory, operand1) }
                0xB9 -> { OLD_LoadStoreAccumulator().ldaAbsoluteY(this, memory, operand1, operand2) }
                0xBA -> { TransferRegisters().tsx(this) }
                0xBC -> { OLD_LoadStoreYRegister().ldyAbsoluteX(this, memory, operand1, operand2) }
                0xBD -> { OLD_LoadStoreAccumulator().ldaAbsoluteX(this, memory, operand1, operand2) }
                0xBE -> { OLD_LoadStoreXRegister().ldxAbsoluteY(this, memory, operand1, operand2) }

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
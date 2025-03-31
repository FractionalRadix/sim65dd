package com.cormontia.sim65dd

import com.cormontia.sim65dd.memory.Memory
import com.cormontia.sim65dd.memory.MemoryAsArray
import com.cormontia.sim65dd.memory.MemoryAsMutableMap
import kotlin.reflect.KFunction1

fun main() {
    val cpu = CentralProcessingUnit()

    println("Starting test program #1.")
    val memory1 = program1()
    cpu.mainLoop(memory1, 0.toUShort()) { processor -> processor.y == 0xFF.toUByte() }

    println("Starting test program #2.")
    val memory2 = program2()
    var counter = 0
    cpu.mainLoop(memory2, 0x0100.toUShort()) { counter++; counter >= 6 }
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

fun program1(): Memory {
    val array = MemoryAsArray()

    array[0u] = 0xA9.toUByte()
    array[1u] = 0xFF.toUByte()
    array[2u] = 0xA0.toUByte()
    array[3u] = 0x09.toUByte()
    array[4u] = 0x99.toUByte()
    array[5u] = 0x00.toUByte()
    array[6u] = 0x10.toUByte()
    array[7u] = 0x88.toUByte()
    array[8u] = 0x10.toUByte()
    //TODO?~ Seems it SHOULD be 0xFA, but it works using 0xFB. Which one is correct?
    //array[9u] = 0xFA.toUByte() // -4 . Need to go to step 4. Question is: WHEN is the IP increased?
    array[9u] = 0xFB.toUByte() // -4 . Need to go to step 4. Question is: WHEN is the IP increased?
    return array
}

@OptIn(ExperimentalUnsignedTypes::class)
fun program2(): Memory {
    // Source: Duck.ai

    val instructions = ubyteArrayOf(
        0xA2u, 0x00u,        // LDX #$00
        0xE8u,               // INX
        0x10u, 0x03u,        // BPL +3 (branch if positive, 3 bytes ahead)
        0xA9u, 0xFFu,        // LDA #$FF
        0x8Du, 0x00u, 0x00u, // STA $00
        0x4Cu, 0xFFu, 0xFFu, // JMP $FF
        0xEAu,               // NOP (this is just a placeholder for the skipped instruction)
        0xA9u, 0x00u,        // LDA #$00
        0x8Du, 0x01u, 0x00u, // STA $01
    )

    val memory = MemoryAsMutableMap()
    for (instr in instructions.withIndex()) {
        val idx = (instr.index.toUShort() + 256u).toUShort()
        memory[idx] = instr.value
    }

    return memory
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


    //TODO?+ Add a termination condition?
    fun mainLoop(memory: Memory, startIp: UShort, terminationCondition: (cpu: CentralProcessingUnit) -> Boolean) {

        this.pc = startIp

        while (!terminationCondition(this)) {
            val opCode = memory[pc]

            // Prefix instruction with the value of the Program Counter.
            val pcStr = String.format("%04X", pc.toInt())
            print("[$pcStr] ")

            val operand1 = memory[pc.inc()]
            val operand2 = memory[pc.inc().inc()]
            when (opCode.toInt()) {
                0x06 -> { AslOperation().aslZeroPage(this, memory, operand1) }
                0x0A -> { AslOperation().aslImmediate(this) }
                0x0E -> { AslOperation().aslAbsolute(this, memory, operand1, operand2) }

                0x10 -> { BranchOperation().bpl(this, operand1) }
                0x16 -> { AslOperation().aslZeroPageX(this, memory, operand1) }
                0x1E -> { AslOperation().aslAbsoluteX(this, memory, operand1, operand2) }

                0x21 -> { AndOperation().andIndexedIndirectX(this, memory, operand1) }
                0x25 -> { AndOperation().andZeroPage(this, memory, operand1) }
                0x29 -> { AndOperation().andImmediate(this, operand1) }
                0x2D -> { AndOperation().andAbsolute(this, memory, operand1, operand2) }

                0x30 -> { BranchOperation().bmi(this, operand1) }
                0x31 -> { AndOperation().andIndirectIndexedY(this, memory, operand1) }
                0x35 -> { AndOperation().andZeroPageX(this, memory, operand1) }
                0x39 -> { AndOperation().andAbsoluteY(this, memory, operand1, operand2) }
                0x3D -> { AndOperation().andAbsoluteX(this, memory, operand1, operand2) }

                0x50 -> { BranchOperation().bvc(this, operand1) }

                0x61 -> { AddWithCarry().adcIndexedIndirectX(this, memory, operand1) }
                0x65 -> { AddWithCarry().adcZeroPage(this, memory, operand1) }
                0x69 -> { AddWithCarry().adcImmediate(this, operand1) }
                0x6D -> { AddWithCarry().adcAbsolute(this, memory, operand1, operand2) }

                0x70 -> { BranchOperation().bvs(this, operand1) }
                0x71 -> { AddWithCarry().adcIndirectIndexedY(this, memory, operand1) }
                0x75 -> { AddWithCarry().adcZeroPageX(this, memory, operand1) }
                0x79 -> { AddWithCarry().adcAbsoluteY(this, memory, operand1, operand2) }
                0x7D -> { AddWithCarry().adcAbsoluteX(this, memory, operand1, operand2) }

                0x81 -> { LoadStoreAccumulator().staIndexedIndirectX(this, memory, operand1) }
                0x85 -> { LoadStoreAccumulator().staZeroPage(this, memory, operand1) }
                0x84 -> { LoadStoreYRegister().styZeroPage(this, memory, operand1) }
                0x86 -> { LoadStoreXRegister().stxZeroPage(this, memory, operand1) }
                0x88 -> { RegisterInstructions().dey(this) }
                0x8A -> { RegisterInstructions().txa(this) }
                0x8C -> { LoadStoreYRegister().styAbsolute(this, memory, operand1, operand2) }
                0x8D -> { LoadStoreAccumulator().staAbsolute(this, memory, operand1, operand2) }
                0x8E -> { LoadStoreXRegister().stxAbsolute(this, memory, operand1, operand2) }

                0x90 -> { BranchOperation().bcc(this, operand1) }
                0x91 -> { LoadStoreAccumulator().staIndirectIndexedY(this, memory, operand1) }
                0x94 -> { LoadStoreYRegister().styZeroPageX(this, memory, operand1) }
                0x95 -> { LoadStoreAccumulator().staZeroPageX(this, memory, operand1) }
                0x96 -> { LoadStoreXRegister().stxZeroPageY(this, memory, operand1) }
                0x98 -> { RegisterInstructions().tya(this) }
                0x99 -> { LoadStoreAccumulator().staAbsoluteY(this, memory, operand1, operand2) }
                0x9A -> { StackInstructions().txs(this) }
                0x9D -> { LoadStoreAccumulator().staAbsoluteX(this, memory, operand1, operand2) }

                0xA0 -> { LoadStoreYRegister().ldyImmediate(this, operand1) }
                0xA1 -> { LoadStoreAccumulator().ldaIndexedIndirectX(this, memory, operand1) }
                0xA2 -> { LoadStoreXRegister().ldxImmediate(this, operand1) }
                0xA4 -> { LoadStoreYRegister().ldyZeroPage(this, memory, operand1) }
                0xA5 -> { LoadStoreAccumulator().ldaZeroPage(this, memory, operand1) }
                0xA6 -> { LoadStoreXRegister().ldxZeroPage(this, memory, operand1) }
                0xA8 -> { RegisterInstructions().tay(this) }
                0xA9 -> { LoadStoreAccumulator().ldaImmediate(this, operand1) }
                0xAA -> { RegisterInstructions().tax(this) }
                0xAC -> { LoadStoreYRegister().ldyAbsolute(this, memory, operand1, operand2) }
                0xAD -> { LoadStoreAccumulator().ldaAbsolute(this, memory, operand1, operand2) }
                0xAE -> { LoadStoreXRegister().ldxAbsolute(this, memory, operand1, operand2) }

                0xB0 -> { BranchOperation().bcs(this, operand1) }
                0xB1 -> { LoadStoreAccumulator().ldaIndirectIndexedY(this, memory, operand1) }
                0xB4 -> { LoadStoreYRegister().ldyZeroPageX(this, memory, operand1) }
                0xB5 -> { LoadStoreAccumulator().ldaZeroPageX(this, memory, operand1) }
                0xB6 -> { LoadStoreXRegister().ldxZeroPageY(this, memory, operand1) }
                0xB9 -> { LoadStoreAccumulator().ldaAbsoluteY(this, memory, operand1, operand2) }
                0xBA -> { StackInstructions().tsx(this) }
                0xBC -> { LoadStoreYRegister().ldyAbsoluteX(this, memory, operand1, operand2) }
                0xBD -> { LoadStoreAccumulator().ldaAbsoluteX(this, memory, operand1, operand2) }
                0xBE -> { LoadStoreXRegister().ldxAbsoluteY(this, memory, operand1, operand2) }

                0xC8 -> { RegisterInstructions().iny(this) }
                0xCA -> { RegisterInstructions().dex(this) }

                0xD0 -> { BranchOperation().bne(this, operand1) }

                0xE8 -> { RegisterInstructions().inx(this) }

                0xF0 -> { BranchOperation().beq(this, operand1) }

                else -> { println("Not yet implemented, or not an existing OPCode!")}
            }
        }

        for (i in 0 ..< 65536) {
            if (memory[i.toUShort()] == 255.toUByte()) {
                print(" $i")
            }
        }

        println()
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
package com.cormontia.sim65dd

import java.util.*

//TODO?~ Make these methods extension functions on CentralProcessingUnit?
class LoadStoreAccumulator {
    fun ldaImmediate(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA #\$$operand")

        cpu.acc = param
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldaZeroPage(cpu: CentralProcessingUnit, param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$operand")

        cpu.acc = memory[param.toInt()]
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun staZeroPage(cpu: CentralProcessingUnit, param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$operand")

        memory[param.toInt()] = cpu.acc
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldaZeroPageX(cpu: CentralProcessingUnit, param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$operand, X")

        val newIndex = (param + cpu.x).toUByte() // Conversion to UByte, effectively the same as giving a "modulo 256".
        cpu.acc = memory[newIndex.toInt()]

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun staZeroPageX(cpu: CentralProcessingUnit, param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$operand, X")

        val newIndex = (param + cpu.x).toUByte() // Conversion to UByte, effectively the same as giving a "modulo 256".
        memory[newIndex.toInt()] = cpu.acc

        cpu.pc = cpu.pc.inc().inc()
    }

    fun lda_absolute(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, memory: Array<UByte>) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$msbHex$lsbHex")

        val location = 256 * msb.toShort() + lsb.toShort()
        cpu.acc = memory[location]
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun staAbsolute(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, memory: Array<UByte>) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$msbHex$lsbHex")

        val location = 256 * msb.toShort() + lsb.toShort()
        memory[location] = cpu.acc
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldaAbsoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, memory: Array<UByte>) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("LDA \$$msbHex$lsbHex,X")

        val location = (256 * msb.toShort() + lsb.toShort() + cpu.x.toShort()).toShort() // Conversion to short should be the same as "mod 65536".
        cpu.acc = memory[location.toInt()]

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun staAbsoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, memory: Array<UByte>) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("STA \$$msbHex$lsbHex,X")

        val location = (256 * msb.toShort() + lsb.toShort() + cpu.x.toShort()).toShort() // Conversion to short should be the same as "mod 65536".
        memory[location.toInt()] = cpu.acc

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldaAbsoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, memory: Array<UByte>) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("LDA \$$msbHex$lsbHex,Y")

        val location = (256 * msb.toShort() + lsb.toShort() + cpu.y.toShort()).toShort() // Conversion to short should be the same as "mod 65536".
        cpu.acc = memory[location.toInt()]

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun staAbsoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, memory: Array<UByte>) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$msbHex$lsbHex, Y")

        val location = 256 * msb.toShort() + lsb.toShort()
        memory[location] = cpu.acc
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldaIndexedIndirectX(cpu: CentralProcessingUnit, param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$($operand, X)")

        val location1 = (param + cpu.x).toUByte()
        val zeroPageValueLsb = memory[location1.toInt()]
        val zeroPageValueMsb = memory[location1.toInt().inc()]
        val location2 = (256u * zeroPageValueMsb.toUShort() + zeroPageValueLsb.toUShort()).toUShort()
        cpu.acc = memory[location2.toInt()]

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun staIndexedIndirectX(cpu: CentralProcessingUnit, param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$($operand, X)")

        val location1 = (param + cpu.x).toUByte()
        val zeroPageValueLsb = memory[location1.toInt()]
        val zeroPageValueMsb = memory[location1.toInt().inc()]
        val location2 = (256u * zeroPageValueMsb.toUShort() + zeroPageValueLsb.toUShort()).toUShort()
        memory[location2.toInt()] = cpu.acc

        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldaIndirectIndexedY(cpu: CentralProcessingUnit, param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA $($operand),Y")

        val zeroPageValueLsb = memory[param.toInt()]
        val zeroPageValueMsb = memory[(param.inc()).toInt()]
        val location = (256u * zeroPageValueMsb + zeroPageValueLsb + cpu.y).toUShort()
        cpu.acc = memory[location.toInt()]

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun staIndirectIndexedY(cpu: CentralProcessingUnit, param: UByte, memory: Array<UByte>) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA $($operand),Y")

        val zeroPageValueLsb = memory[param.toInt()]
        val zeroPageValueMsb = memory[(param.inc()).toInt()]
        val location = (256u * zeroPageValueMsb + zeroPageValueLsb + cpu.y).toUShort()
        memory[location.toInt()] = cpu.acc

        cpu.pc = cpu.pc.inc().inc()
    }
}
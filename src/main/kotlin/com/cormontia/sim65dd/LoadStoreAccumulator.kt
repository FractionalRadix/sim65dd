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

    fun ldaZeroPage(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$operand")

        val location = zeroPage(param)
        cpu.acc = memory[location.toInt()]
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun staZeroPage(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$operand")

        val location = zeroPage(param)
        memory[location.toInt()] = cpu.acc
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldaZeroPageX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$operand, X")

        val location = zeroPageX(cpu, param)
        cpu.acc = memory[location.toInt()]

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun staZeroPageX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$operand, X")

        val newIndex = zeroPageX(cpu, param) // Conversion to UByte, effectively the same as giving a "modulo 256".
        memory[newIndex.toInt()] = cpu.acc

        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldaAbsolute(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$msbHex$lsbHex")

        val location = absolute(lsb, msb)
        cpu.acc = memory[location]
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun staAbsolute(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$msbHex$lsbHex")

        val location = absolute(lsb, msb)
        memory[location] = cpu.acc
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldaAbsoluteX(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("LDA \$$msbHex$lsbHex,X")

        val location = absoluteX(cpu, lsb, msb)
        cpu.acc = memory[location.toInt()]

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun staAbsoluteX(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("STA \$$msbHex$lsbHex,X")

        val location = absoluteY(cpu, lsb, msb)
        memory[location.toInt()] = cpu.acc

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldaAbsoluteY(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("LDA \$$msbHex$lsbHex,Y")

        val location = absoluteY(cpu, lsb, msb)
        cpu.acc = memory[location.toInt()]

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun staAbsoluteY(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$msbHex$lsbHex, Y")

        val location = absoluteY(cpu, lsb, msb)
        memory[location.toInt()] = cpu.acc
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldaIndexedIndirectX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
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

    fun staIndexedIndirectX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$($operand, X)")

        val location1 = (param + cpu.x).toUByte()
        val zeroPageValueLsb = memory[location1.toInt()]
        val zeroPageValueMsb = memory[location1.toInt().inc()]
        val location2 = (256u * zeroPageValueMsb.toUShort() + zeroPageValueLsb.toUShort()).toUShort()
        memory[location2.toInt()] = cpu.acc

        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldaIndirectIndexedY(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
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

    fun staIndirectIndexedY(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA $($operand),Y")

        val zeroPageValueLsb = memory[param.toInt()]
        val zeroPageValueMsb = memory[(param.inc()).toInt()]
        val location = (256u * zeroPageValueMsb + zeroPageValueLsb + cpu.y).toUShort()
        memory[location.toInt()] = cpu.acc

        cpu.pc = cpu.pc.inc().inc()
    }
}
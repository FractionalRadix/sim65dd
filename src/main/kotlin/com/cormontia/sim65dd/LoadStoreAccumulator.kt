package com.cormontia.sim65dd

import com.cormontia.sim65dd.memory.Memory
import java.util.*

class LoadStoreAccumulator {
    fun ldaImmediate(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA #\$$operand")

        cpu.acc = param
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldaZeroPage(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$operand")

        cpu.acc = memory.getZeroPage(location)
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun staZeroPage(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$operand")

        memory.setZeroPage(location, cpu.acc)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldaZeroPageX(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$operand, X")

        cpu.acc = memory.getZeroPageX(cpu, location)

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun staZeroPageX(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$operand, X")

        memory.setZeroPageX(cpu, location, cpu.acc)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldaAbsolute(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$$msbHex$lsbHex")

        cpu.acc = memory.getAbsolute(lsb, msb)
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun staAbsolute(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$msbHex$lsbHex")

        memory.setAbsolute(lsb, msb, cpu.acc)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldaAbsoluteX(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("LDA \$$msbHex$lsbHex,X")

        cpu.acc = memory.getAbsoluteX(cpu, lsb, msb)

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun staAbsoluteX(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("STA \$$msbHex$lsbHex,X")

        memory.setAbsoluteX(cpu, lsb, msb, cpu.acc)

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldaAbsoluteY(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("LDA \$$msbHex$lsbHex,Y")

        cpu.acc = memory.getAbsoluteY(cpu, lsb, msb)

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun staAbsoluteY(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$$msbHex$lsbHex, Y")

        memory.setAbsoluteY(cpu, lsb, msb, cpu.acc)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldaIndexedIndirectX(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA \$($operand, X)")

        cpu.acc = memory.getIndexedIndirectX(cpu, location)

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun staIndexedIndirectX(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA \$($operand, X)")

        memory.setIndexedIndirectX(cpu, location, cpu.acc)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldaIndirectIndexedY(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDA $($operand),Y")

        cpu.acc = memory.getIndirectIndexedY(cpu, location)

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun staIndirectIndexedY(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STA $($operand),Y")

        memory.setIndirectIndexedY(cpu, location, cpu.acc)

        cpu.pc = cpu.pc.inc().inc()
    }
}
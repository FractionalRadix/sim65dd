package com.cormontia.sim65dd

import com.cormontia.sim65dd.memory.Memory
import java.util.*

class AlternativeLoadStoreXRegister {
    fun ldxImmediate(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDX #\$$operand")

        cpu.x = param
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldxZeroPage(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDX \$$operand")

        cpu.x = memory.getZeroPage(location)
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldxZeroPageY(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDX \$$operand, Y")

        cpu.x = memory.getZeroPageY(cpu, location)

        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldxAbsolute(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDX \$$msbHex$lsbHex")

        cpu.x = memory.getAbsolute(lsb, msb)
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldxAbsoluteY(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("LDX \$$msbHex$lsbHex,Y")

        cpu.x = memory.getAbsoluteY(cpu, lsb, msb)

        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun stxZeroPage(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STX \$$operand")

        memory.setZeroPage(location, cpu.x)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun stxZeroPageY(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STX \$$operand, Y")

        memory.setZeroPageY(cpu, location, cpu.x)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun stxAbsolute(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STX \$$msbHex$lsbHex")

        memory.setAbsolute(lsb, msb, cpu.x)
        cpu.pc = cpu.pc.inc().inc().inc()
    }
}
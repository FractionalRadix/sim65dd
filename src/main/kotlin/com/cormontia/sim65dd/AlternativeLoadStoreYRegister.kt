package com.cormontia.sim65dd

import com.cormontia.sim65dd.memory.Memory
import java.util.*

class AlternativeLoadStoreYRegister {
    fun ldyImmediate(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY #\$$operand")

        cpu.y = param
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldyZeroPage(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY \$$operand")

        cpu.y = memory.getZeroPage(location)
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun styZeroPage(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STY \$$operand")

        memory.setZeroPage(location, cpu.y)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldyZeroPageX(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY \$$operand, X")

        cpu.y = memory.getZeroPageX(cpu, location)

        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun styZeroPageX(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STY \$$operand, X")

        memory.setZeroPageX(cpu, location, cpu.y)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun styAbsolute(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STY \$$msbHex$lsbHex")

        memory.setAbsolute(lsb, msb, cpu.y)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldyAbsolute(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY \$$msbHex$lsbHex")

        cpu.y = memory.getAbsolute(lsb, msb)
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldyAbsoluteX(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY \$$msbHex$lsbHex, X")

        cpu.x = memory.getAbsoluteX(cpu, lsb, msb)
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

}
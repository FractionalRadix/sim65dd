package com.cormontia.sim65dd

import java.util.*

class LoadStoreYRegister {
    fun ldyImmediate(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY #\$$operand")

        cpu.y = param
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldyZeroPage(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY \$$operand")

        val location = zeroPage(param)
        cpu.y = memory[location.toInt()]
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun styZeroPage(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STY \$$operand")

        val location = zeroPage(param)
        memory[location.toInt()] = cpu.y
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldyZeroPageX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY \$$operand, X")

        val location = zeroPageX(cpu, param)
        cpu.y = memory[location.toInt()]

        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun styZeroPageX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STY \$$operand, X")

        val location = zeroPageX(cpu, param)
        memory[location.toInt()] = cpu.y

        cpu.pc = cpu.pc.inc().inc()
    }

    fun styAbsolute(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STY \$$msbHex$lsbHex")

        val location = absolute(lsb, msb)
        memory[location] = cpu.y
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldyAbsolute(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY \$$msbHex$lsbHex")

        val location = absolute(lsb, msb)
        cpu.y = memory[location]
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldyAbsoluteX(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDY \$$msbHex$lsbHex, X")

        val location = absoluteX(cpu, lsb, msb)
        cpu.x = memory[location.toInt()]
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }
}
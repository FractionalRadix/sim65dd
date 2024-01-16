package com.cormontia.sim65dd

import java.util.*

class LoadStoreXRegister {
    fun ldxImmediate(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDX #\$$operand")

        cpu.x = param
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldxZeroPage(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDX \$$operand")

        cpu.x = memory[param.toInt()]
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldxZeroPageY(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDX \$$operand, Y")

        val newIndex = (param + cpu.y).toUByte() // Conversion to UByte, effectively the same as giving a "modulo 256".
        cpu.x = memory[newIndex.toInt()]

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc()
    }

    fun ldxAbsolute(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("LDX \$$msbHex$lsbHex")

        val location = absolute(lsb, msb)
        cpu.x = memory[location]
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun ldxAbsoluteY(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        print("LDX \$$msbHex$lsbHex,Y")

        val location = absoluteY(cpu, lsb, msb)
        cpu.x = memory[location.toInt()]

        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun stxZeroPage(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STX \$$operand")

        memory[param.toInt()] = cpu.x
        cpu.pc = cpu.pc.inc().inc()
    }

    fun stxZeroPageY(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STX \$$operand, Y")

        val newIndex = (param + cpu.y).toUByte() // Conversion to UByte, effectively the same as giving a "modulo 256".
        memory[newIndex.toInt()] = cpu.x

        cpu.pc = cpu.pc.inc().inc()
    }

    fun stxAbsolute(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("STX \$$msbHex$lsbHex")

        val location = absolute(lsb, msb)
        memory[location] = cpu.x
        cpu.pc = cpu.pc.inc().inc().inc()
    }
}
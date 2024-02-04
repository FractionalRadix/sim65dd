package com.cormontia.sim65dd

import java.util.*

class AlternativeAndOperation {

    fun andImmediate(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND #\$$operand")

        cpu.acc = cpu.acc and param
        cpu.Z = (cpu.acc == 0.toUByte())
        cpu.N = (cpu.acc and 128u > 0u)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun andZeroPage(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND \$$operand")

        val memoryValue = memory.getZeroPage(location)
        cpu.acc = cpu.acc and memoryValue
        cpu.Z = (cpu.acc == 0.toUByte())
        cpu.N = (cpu.acc and 128u > 0u)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun andZeroPageX(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND \$$operand, X")

        val memoryValue = memory.getZeroPageX(cpu, location)
        cpu.acc = cpu.acc and memoryValue
        cpu.Z = (cpu.acc == 0.toUByte())
        cpu.N = (cpu.acc and 128u > 0u)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun andAbsolute(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND \$$msbHex$lsbHex")

        val memoryValue = memory.getAbsolute(lsb, msb)
        cpu.acc = cpu.acc and memoryValue
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun andAbsoluteX(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND \$$msbHex$lsbHex, X")

        val memoryValue = memory.getAbsoluteX(cpu, lsb, msb)
        cpu.acc = cpu.acc and memoryValue
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun andAbsoluteY(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND \$$msbHex$lsbHex, Y")

        val memoryValue = memory.getAbsoluteX(cpu, lsb, msb)
        cpu.acc = cpu.acc and memoryValue
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun andIndexedIndirectX(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND (\$$operand, X)")

        val memoryValue = memory.getIndexedIndirectX(cpu, location)
        cpu.acc = cpu.acc and memoryValue
        cpu.Z = (cpu.acc == 0.toUByte())
        cpu.N = (cpu.acc and 128u > 0u)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun andIndirectIndexedY(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND (\$$operand), Y")

        val memoryValue = memory.getIndirectIndexedY(cpu, location)
        cpu.acc = cpu.acc and memoryValue
        cpu.Z = (cpu.acc == 0.toUByte())
        cpu.N = (cpu.acc and 128u > 0u)

        cpu.pc = cpu.pc.inc().inc()
    }

}
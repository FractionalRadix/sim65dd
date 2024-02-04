package com.cormontia.sim65dd

import java.util.*

class OLD_AndOperation {

    fun andImmediate(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND #\$$operand")

        cpu.acc = cpu.acc and param
        cpu.Z = (cpu.acc == 0.toUByte())
        cpu.N = (cpu.acc and 128u > 0u)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun andZeroPage(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND \$$operand")

        val location = zeroPage(param)
        val memoryValue = memory[location]
        cpu.acc = cpu.acc and memoryValue
        cpu.Z = (cpu.acc == 0.toUByte())
        cpu.N = (cpu.acc and 128u > 0u)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun andZeroPageX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND \$$operand, X")

        val location = zeroPageX(cpu, param)
        val memoryValue = memory[location]
        cpu.acc = cpu.acc and memoryValue
        cpu.Z = (cpu.acc == 0.toUByte())
        cpu.N = (cpu.acc and 128u > 0u)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun andAbsolute(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND \$$msbHex$lsbHex")

        val location = absolute(lsb, msb)
        val memoryValue = memory[location]
        cpu.acc = cpu.acc and memoryValue
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun andAbsoluteX(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND \$$msbHex$lsbHex, X")

        val location = absoluteX(cpu, lsb, msb)
        val memoryValue = memory[location]
        cpu.acc = cpu.acc and memoryValue
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun andAbsoluteY(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND \$$msbHex$lsbHex, Y")

        val location = absoluteY(cpu, lsb, msb)
        val memoryValue = memory[location]
        cpu.acc = cpu.acc and memoryValue
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun andIndexedIndirectX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND (\$$operand, X)")

        val location = indexedIndirectX(cpu, memory, param)
        val memoryValue = memory[location]
        cpu.acc = cpu.acc and memoryValue
        cpu.Z = (cpu.acc == 0.toUByte())
        cpu.N = (cpu.acc and 128u > 0u)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun andIndirectIndexedY(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("AND (\$$operand), Y")

        val location = indirectIndexedY(cpu, memory, param)
        val memoryValue = memory[location]
        cpu.acc = cpu.acc and memoryValue
        cpu.Z = (cpu.acc == 0.toUByte())
        cpu.N = (cpu.acc and 128u > 0u)

        cpu.pc = cpu.pc.inc().inc()
    }
}
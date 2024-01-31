package com.cormontia.sim65dd

import java.util.*

class AlternativeAslOperation {
    private fun performAsl(cpu: CentralProcessingUnit, mem: UByte): UByte {
        var mem1 = mem
        cpu.C = (mem1 and 128u) > 0u
        mem1 = (mem1.toInt() shl 1).toUByte()
        cpu.Z = (mem1 == 0.toUByte())
        cpu.N = (mem1 and 128u) > 0u
        return mem1
    }

    fun aslImmediate(cpu: CentralProcessingUnit) {
        println("ASL A")

        cpu.acc = performAsl(cpu, cpu.acc)

        cpu.pc = cpu.pc.inc()
    }

    fun aslZeroPage(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ASL \$$operand")

        val mem = memory.getZeroPage(location)
        memory.setZeroPage(location,  performAsl(cpu, mem))

        cpu.pc = cpu.pc.inc().inc()
    }

    fun aslZeroPageX(cpu: CentralProcessingUnit, memory: Memory, location: UByte) {
        val operand = location.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ASL \$$operand, X")

        val mem = memory.getZeroPageX(cpu, location)
        memory.setZeroPageX(cpu, location, performAsl(cpu, mem))

        cpu.pc = cpu.pc.inc().inc()
    }

    fun aslAbsolute(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ASL \$$msbHex$lsbHex")

        val mem = memory.getAbsolute(lsb, msb)
        memory.setAbsolute(lsb, msb, performAsl(cpu, mem))

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun aslAbsoluteX(cpu: CentralProcessingUnit, memory: Memory, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ASL \$$msbHex$lsbHex, X")

        val location = absoluteX(cpu, lsb, msb)
        val mem = memory.getAbsoluteX(cpu, lsb, msb)
        memory.setAbsoluteX(cpu, lsb, msb, performAsl(cpu, mem))

        cpu.pc = cpu.pc.inc().inc().inc()
    }
}
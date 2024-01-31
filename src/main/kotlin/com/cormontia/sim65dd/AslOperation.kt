package com.cormontia.sim65dd

import java.util.*

class AslOperation {

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
    }

    fun aslZeroPage(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ASL \$$operand")

        val location = zeroPage(param)
        val mem = memory[location]
        memory[location] = performAsl(cpu, mem)
    }

    fun aslZeroPageX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ASL \$$operand, X")

        val location = zeroPageX(cpu, param)
        val mem = memory[location]
        memory[location] = performAsl(cpu, mem)
    }

    fun aslAbsolute(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ASL \$$msbHex$lsbHex")

        TODO()
    }

    fun aslAbsoluteX(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ASL \$$msbHex$lsbHex, X")

        TODO()
    }

}
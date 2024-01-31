package com.cormontia.sim65dd

import java.util.*

class AddWithCarry {
    private data class AdditionResult(
        val sumValue: UByte,
        val carry: Boolean,
        val zero: Boolean,
        val negative: Boolean,
        val overflow: Boolean,
    ) {
        fun applyTo(cpu: CentralProcessingUnit) {
            cpu.acc = sumValue
            cpu.C = carry
            cpu.Z = zero
            cpu.N = negative
            cpu.V = overflow
        }
    }

    private fun addWithCary(op1: UByte, op2: UByte, carry: Boolean) : AdditionResult {
        val op1Short = op1.toUShort()
        val op2Short = op2.toUShort()
        var sum = op1Short + op2Short
        if (carry) { sum++ }
        val resultValue = sum.toUByte() // "mod 256" is implicit.
        val resultCarry = (sum > 255u)
        val resultZero = (sum == 0u)
        val resultNegative = (sum >= 128u)

        // Overflow flag. From https://stackoverflow.com/a/25926578/812149 .
        // Not clear if this is correct for the 6502/6510 though...
        //TODO!+ Check http://www.6502.org/tutorials/vflag.html and see if this code simulates the Overflow flag correctly.
        val bothParametersPositive = op1 < 128u && op2 < 128u
        val bothParametersNegative = op1 > 127u && op2 > 127u
        val resultOverflow = if (bothParametersPositive && resultNegative) {
            true
        } else if (bothParametersNegative && !resultNegative) {
            true
        } else {
            false
        }

        return AdditionResult(resultValue, resultCarry, resultZero, resultNegative, resultOverflow)
    }

    fun adcImmediate(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC #\$$operand")

        val result = addWithCary(cpu.acc, param, cpu.C)
        result.applyTo(cpu)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun adcZeroPage(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC \$$operand")

        val location = zeroPage(param)
        val toAdd = memory[location]
        val result = addWithCary(cpu.acc, toAdd, cpu.C)
        result.applyTo(cpu)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun adcZeroPageX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC \$$operand, X")

        val location = zeroPageX(cpu, param)
        val toAdd = memory[location]
        val result = addWithCary(cpu.acc, toAdd, cpu.C)
        result.applyTo(cpu)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun adcAbsolute(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC \$$msbHex$lsbHex")

        val location = absolute(lsb, msb)
        val toAdd = memory[location]
        val result = addWithCary(cpu.acc, toAdd, cpu.C)
        result.applyTo(cpu)

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun adcAbsoluteX(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC \$$msbHex$lsbHex, X")

        val location = absoluteX(cpu, lsb, msb)
        val toAdd = memory[location]
        val result = addWithCary(cpu.acc, toAdd, cpu.C)
        result.applyTo(cpu)

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun adcAbsoluteY(cpu: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC \$$msbHex$lsbHex, Y")

        val location = absoluteY(cpu, lsb, msb)
        val toAdd = memory[location]
        val result = addWithCary(cpu.acc, toAdd, cpu.C)
        result.applyTo(cpu)

        cpu.pc = cpu.pc.inc().inc().inc()
    }

    fun adcIndexedIndirectX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC (\$$operand, X)")

        val location = indexedIndirectX(cpu, memory, param)
        val toAdd = memory[location]
        val result = addWithCary(cpu.acc, toAdd, cpu.C)
        result.applyTo(cpu)

        cpu.pc = cpu.pc.inc().inc()
    }

    fun adcIndirectIndexedY(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC (\$$operand), Y")

        val location = indirectIndexedY(cpu, memory, param)
        val toAdd = memory[location]
        val result = addWithCary(cpu.acc, toAdd, cpu.C)
        result.applyTo(cpu)

        cpu.pc = cpu.pc.inc().inc()
    }
}
package com.cormontia.sim65dd

import java.util.*

class AddWithCarry {
    private fun addWithCary(op1: UByte, op2: UByte, carry: Boolean) : Pair<UByte, Boolean> {
        val op1Short = op1.toUShort()
        val op2Short = op2.toUShort()
        var sum = op1Short + op2Short
        if (carry) { sum++ }
        val resultCarry = (sum > 255u)
        val resultValue = sum.toUByte() // "mod 256" is implicit.
        return Pair(resultValue, resultCarry)
    }

    fun adcImmediate(centralProcessingUnit: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC #\$$operand")
        TODO()
    }

    fun adcZeroPage(centralProcessingUnit: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC \$$operand")

        TODO()
    }

    fun adcZeroPageX(centralProcessingUnit: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC \$$operand, X")

        TODO()
    }

    fun adcAbsolute(centralProcessingUnit: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC \$$msbHex$lsbHex")

        TODO()
    }

    fun adcAbsoluteX(centralProcessingUnit: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC \$$msbHex$lsbHex, X")

        TODO()
    }

    fun adcAbsoluteY(centralProcessingUnit: CentralProcessingUnit, memory: Array<UByte>, lsb: UByte, msb: UByte) {
        val lsbHex = lsb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        val msbHex = msb.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC \$$msbHex$lsbHex, Y")

        TODO()
    }

    fun adcIndirectX(centralProcessingUnit: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC (\$$operand, X)")

        TODO()
    }

    fun adcIndirectY(centralProcessingUnit: CentralProcessingUnit, memory: Array<UByte>, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("ADC (\$$operand), Y")

        TODO()
    }
}
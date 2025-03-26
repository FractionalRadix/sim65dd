package com.cormontia.sim65dd

import java.util.*

class BranchOperation {

    //TODO?~ Maybe these methods should be static. Put them in a companion object?

    fun bpl(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("BPL $operand")

        branch(cpu, param, !cpu.N)
        //TODO!- Remove if the refactoring is found to be correct.
        /*
        cpu.pc = cpu.pc.inc() // Now points at the instruction.

        if (!cpu.N) {

            if (param < 128u) {
                val offset = param.toUShort()
                val newAddress = cpu.pc + offset
                cpu.pc = newAddress.toUShort()
            } else {
                val biggerTwosComplement = param.toUShort() + 0xFF00.toUShort() // Note that it won't exceed 0xFFFF
                val newAddress = cpu.pc + biggerTwosComplement
                cpu.pc = newAddress.toUShort()
            }
        } else {
            cpu.pc = cpu.pc.inc() // Is a single "inc()" enough...?!? That might imply a bug!
        }
         */
    }

    fun bmi(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("BMI $operand")

        branch(cpu, param, cpu.N)
    }

    fun bvs(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("BVS $operand")

        branch(cpu, param, cpu.V)
    }

    fun bvc(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("BVC $operand")

        branch(cpu, param, !cpu.V)
    }

    fun bcc(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("BCC $operand")

        branch(cpu, param, !cpu.C)
    }

    fun bcs(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("BCS $operand")

        branch(cpu, param, cpu.C)
    }

    fun bne(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("BNE $operand")

        branch(cpu, param, !cpu.Z)
    }

    fun beq(cpu: CentralProcessingUnit, param: UByte) {
        val operand = param.toString(16).uppercase(Locale.getDefault()).padStart(2, '0')
        println("BEQ $operand")

        branch(cpu, param, cpu.Z)
    }

    private fun branch(cpu: CentralProcessingUnit, param: UByte, flag: Boolean) {
        cpu.pc = cpu.pc.inc() // Now points at the instruction.
        if (flag) {

            if (param < 128u) {
                val offset = param.toUShort()
                val newAddress = cpu.pc + offset
                cpu.pc = newAddress.toUShort()
            } else {
                val biggerTwosComplement = param.toUShort() + 0xFF00.toUShort() // Note that it won't exceed 0xFFFF
                val newAddress = cpu.pc + biggerTwosComplement
                cpu.pc = newAddress.toUShort()
            }
        } else {
            cpu.pc = cpu.pc.inc() // Is a single "inc()" enough...?!? That might imply a bug!
        }
    }

}
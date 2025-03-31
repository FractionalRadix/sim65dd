package com.cormontia.sim65dd

class StackInstructions {

    fun tsx(cpu: CentralProcessingUnit) {
        println("TSX")

        cpu.x = cpu.sp
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun txs(cpu: CentralProcessingUnit) {
        println("TXS")

        cpu.sp = cpu.x
        cpu.pc = cpu.pc.inc()
    }

    //TODO!+ Implement PHA, PLA, PHP, and PLP.
}
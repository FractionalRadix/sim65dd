package com.cormontia.sim65dd

class TransferRegisters {
    fun tax(cpu: CentralProcessingUnit) {
        cpu.x = cpu.acc
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun tay(cpu: CentralProcessingUnit) {
        cpu.y = cpu.acc
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun tsx(cpu: CentralProcessingUnit) {
        cpu.x = cpu.sp
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun txa(cpu: CentralProcessingUnit) {
        cpu.acc = cpu.x
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()    }

    fun txs(cpu: CentralProcessingUnit) {
        cpu.sp = cpu.x
        cpu.pc = cpu.pc.inc()
    }

    fun tya(cpu: CentralProcessingUnit) {
        cpu.acc = cpu.y
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }
}
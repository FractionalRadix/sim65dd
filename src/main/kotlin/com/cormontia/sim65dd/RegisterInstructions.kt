package com.cormontia.sim65dd

class RegisterInstructions {

    fun tax(cpu: CentralProcessingUnit) {
        println("TAX")

        cpu.x = cpu.acc
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun txa(cpu: CentralProcessingUnit) {
        println("TXA")

        cpu.acc = cpu.x
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun dex(cpu: CentralProcessingUnit) {
        println("DEX")

        cpu.x = cpu.x.dec()
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun inx(cpu: CentralProcessingUnit) {
        println("INX")

        cpu.x = cpu.x.inc()
        cpu.N = (cpu.x and 128u > 0u)
        cpu.Z = (cpu.x.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun tay(cpu: CentralProcessingUnit) {
        println("TAY")

        cpu.y = cpu.acc
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun tya(cpu: CentralProcessingUnit) {
        println("TYA")

        cpu.acc = cpu.y
        cpu.N = (cpu.acc and 128u > 0u)
        cpu.Z = (cpu.acc.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun dey(cpu: CentralProcessingUnit) {
        println("DEY")

        cpu.y = cpu.y.dec()
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }

    fun iny(cpu: CentralProcessingUnit) {
        println("INY")

        cpu.y = cpu.y.inc()
        cpu.N = (cpu.y and 128u > 0u)
        cpu.Z = (cpu.y.compareTo(0u) == 0)
        cpu.pc = cpu.pc.inc()
    }
}
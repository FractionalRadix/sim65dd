package com.cormontia.sim65dd

interface Memory {
    fun absolute(lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort()).toUShort()
    fun absoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort() + cpu.x.toShort()).toUShort()
    fun absoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort() + cpu.y.toShort()).toUShort()
    fun zeroPage(param: UByte) = param
    fun zeroPageX(cpu: CentralProcessingUnit, param: UByte) = (param + cpu.x).toUByte()
    fun zeroPageY(cpu: CentralProcessingUnit, param: UByte) = (param + cpu.y).toUByte()
    fun indirectIndexedY(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte): UShort {
        val zeroPageValueLsb = memory[param.toInt()]
        val zeroPageValueMsb = memory[(param.inc()).toInt()]
        val location = (256u * zeroPageValueMsb + zeroPageValueLsb + cpu.y)
        return location.toUShort()
    }
    fun indexedIndirectX(cpu: CentralProcessingUnit, memory: Array<UByte>, param: UByte): UShort {
        val location1 = (param + cpu.x).toUByte()
        val zeroPageValueLsb = memory[location1.toInt()]
        val zeroPageValueMsb = memory[location1.toInt().inc()]
        val location2 = (256u * zeroPageValueMsb.toUShort() + zeroPageValueLsb.toUShort())
        return location2.toUShort()
    }


    fun getAbsolute(lsb: UByte, msb: UByte): UByte
    fun setAbsolute(lsb: UByte, msb: UByte, value: UByte)
}

class MemoryAsArray: Memory {
    private val array = Array<UByte>(65536) { 0u }

    override fun getAbsolute(lsb: UByte, msb: UByte): UByte {
        val location = absolute(lsb, msb)
        val value = array[location.toInt()]
        return value
    }

    override fun setAbsolute(lsb: UByte, msb: UByte, value: UByte) {
        val location = absolute(lsb, msb)
        array[location.toInt()] = value
    }

    fun toMemoryAsMutableMap(): MemoryAsMutableMap {
        TODO()
    }
}

class MemoryAsMutableMap: Memory {
    private val map = mutableMapOf<UShort, UByte>()

    override fun getAbsolute(lsb: UByte, msb: UByte): UByte {
        val location = absolute(lsb, msb)
        val value = map.getOrPut(location) { 0u }
        return value
    }

    override fun setAbsolute(lsb: UByte, msb: UByte, value: UByte) {
        val location = absolute(lsb, msb)
        map[location] = value
    }

    fun toMemoryAsArray(): MemoryAsArray {
        TODO()
    }
}
package com.cormontia.sim65dd.memory

import com.cormontia.sim65dd.CentralProcessingUnit

interface Memory {
    fun absolute(lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort()).toUShort()
    fun absoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort() + cpu.x.toShort()).toUShort()
    fun absoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte) = (256 * msb.toShort() + lsb.toShort() + cpu.y.toShort()).toUShort()
    fun zeroPage(location: UByte) = location
    fun zeroPageX(cpu: CentralProcessingUnit, location: UByte) = (location + cpu.x).toUByte()
    fun zeroPageY(cpu: CentralProcessingUnit, location: UByte) = (location + cpu.y).toUByte()
    fun indirectIndexedY(cpu: CentralProcessingUnit, location: UByte): UShort {
        val zeroPageValueLsb = this.getZeroPage(location)
        val zeroPageValueMsb = this.getZeroPage(location.inc())
        val resultingLocation = (256u * zeroPageValueMsb + zeroPageValueLsb + cpu.y)
        return resultingLocation.toUShort()
    }

    //TODO?~  It's possible that this one should use an 8-bit address, while Indirect Indexed should not.
    fun indexedIndirectX(cpu: CentralProcessingUnit, location: UByte): UShort {
        val location1 = (location + cpu.x).toUByte()
        val zeroPageValueLsb = this.getZeroPage(location1)
        val zeroPageValueMsb = this.getZeroPage(location1.inc())
        val resultingLocation = (256u * zeroPageValueMsb.toUShort() + zeroPageValueLsb.toUShort())
        return resultingLocation.toUShort()
    }


    //TODO?~ Copy constructor?
    fun copy(target: Memory) {
        for (i in 0..0xFFFF) {
            target[i.toUShort()] = this[i.toUShort()]
        }
    }
    operator fun set(location: UShort, value: UByte)
    operator fun get(location: UShort): UByte


    fun getAbsolute(lsb: UByte, msb: UByte): UByte
    fun setAbsolute(lsb: UByte, msb: UByte, value: UByte)

    fun getAbsoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte): UByte
    fun setAbsoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, value: UByte)

    fun getAbsoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte): UByte
    fun setAbsoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, value: UByte)

    fun getZeroPage(location: UByte): UByte
    fun setZeroPage(location: UByte, value: UByte)

    fun getZeroPageX(cpu: CentralProcessingUnit, location: UByte): UByte
    fun setZeroPageX(cpu: CentralProcessingUnit, location: UByte, value: UByte)

    fun getZeroPageY(cpu: CentralProcessingUnit, location: UByte): UByte
    fun setZeroPageY(cpu: CentralProcessingUnit, location: UByte, value: UByte)

    fun getIndirectIndexedY(cpu: CentralProcessingUnit, location: UByte): UByte
    fun setIndirectIndexedY(cpu: CentralProcessingUnit, location: UByte, value: UByte)
    fun getIndexedIndirectX(cpu: CentralProcessingUnit, location: UByte): UByte
    fun setIndexedIndirectX(cpu: CentralProcessingUnit, location: UByte, value: UByte)
}

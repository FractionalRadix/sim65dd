package com.cormontia.sim65dd.memory

import com.cormontia.sim65dd.CentralProcessingUnit

class MemoryAsMutableMap: Memory {
    private val map = mutableMapOf<UShort, UByte>()

    // Auxiliary functions.

    override operator fun get(location: UShort): UByte {
        return map.getOrDefault(location, 0u)
    }

    override operator fun set(location: UShort, value: UByte) {
        map[location] = value
    }

    private fun getter8bit(
        cpu: CentralProcessingUnit,
        zeroPageLocation: UByte,
        getterFunction: (CentralProcessingUnit, UByte) -> UByte,
    ): UByte {
        val location = getterFunction(cpu, zeroPageLocation)
        return map.getOrPut(location.toUShort()) { 0u }
    }
    private fun setter8bit(
        cpu: CentralProcessingUnit,
        zeroPageLocation: UByte,
        value: UByte, setterFunction: (CentralProcessingUnit, UByte) -> UByte,
    ) {
        val location = setterFunction(cpu, zeroPageLocation)
        map[location.toUShort()] = value
    }
    private fun getter16bit(
        cpu: CentralProcessingUnit,
        lsb: UByte,
        msb: UByte,
        locationCalculator: (CentralProcessingUnit, UByte, UByte) -> UShort,
    ): UByte {
        val location = locationCalculator(cpu, lsb, msb)
        return map.getOrPut(location) { 0u }
    }
    private fun setter16bit(
        cpu: CentralProcessingUnit,
        lsb: UByte,
        msb: UByte,
        value: UByte,
        locationCalculator: (CentralProcessingUnit, UByte, UByte) -> UShort,
    ) {
        val location = locationCalculator(cpu, lsb, msb)
        map[location] = value
    }

    // interface implementation.

    override fun getAbsolute(lsb: UByte, msb: UByte): UByte {
        val location = absolute(lsb, msb)
        val value = map.getOrPut(location) { 0u }
        return value
    }

    override fun setAbsolute(lsb: UByte, msb: UByte, value: UByte) {
        val location = absolute(lsb, msb)
        map[location] = value
    }

    override fun getAbsoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte) =
        getter16bit(cpu, lsb, msb, ::absoluteX)

    override fun setAbsoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, value: UByte) {
        setter16bit(cpu, lsb, msb, value, ::absoluteX)
    }

    override fun getAbsoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte) =
        getter16bit(cpu, lsb, msb, ::absoluteY)

    override fun setAbsoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, value: UByte) {
        setter16bit(cpu, lsb, msb, value, ::absoluteY)
    }

    override fun getZeroPage(location: UByte): UByte {
        return map.getOrPut(location.toUShort()) { 0u }
    }

    override fun setZeroPage(location: UByte, value: UByte) {
        map[location.toUShort()] = value
    }

    override fun getZeroPageX(cpu: CentralProcessingUnit, location: UByte) =
        getter8bit(cpu, location, ::zeroPageX)

    override fun setZeroPageX(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        setter8bit(cpu, location, value, ::zeroPageX)
    }

    override fun getZeroPageY(cpu: CentralProcessingUnit, location: UByte) =
        getter8bit(cpu, location, ::zeroPageY)

    override fun setZeroPageY(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        setter8bit(cpu, location, value, ::zeroPageY)
    }

    override fun getIndirectIndexedY(cpu: CentralProcessingUnit, location: UByte): UByte =
        getter8bit(cpu, location, ::getIndirectIndexedY)

    override fun setIndirectIndexedY(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        setter8bit(cpu, location, value, ::getIndirectIndexedY)
    }

    override fun getIndexedIndirectX(cpu: CentralProcessingUnit, location: UByte): UByte =
        getter8bit(cpu, location, ::getIndexedIndirectX)

    override fun setIndexedIndirectX(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        setter8bit(cpu, location, value, ::getIndexedIndirectX)
    }
}
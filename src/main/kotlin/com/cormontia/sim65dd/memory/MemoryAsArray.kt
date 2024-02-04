package com.cormontia.sim65dd.memory

import com.cormontia.sim65dd.CentralProcessingUnit

class MemoryAsArray: Memory {
    private val array = Array<UByte>(65536) { 0u }

    override fun getAbsolute(lsb: UByte, msb: UByte): UByte {
        val address = absolute(lsb, msb)
        val value = array[address.toInt()]
        return value
    }

    override fun setAbsolute(lsb: UByte, msb: UByte, value: UByte) {
        val address = absolute(lsb, msb)
        array[address.toInt()] = value
    }

    override fun getAbsoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte): UByte {
        val address = absoluteX(cpu, lsb, msb)
        val value = array[address.toInt()]
        return value
    }

    override fun setAbsoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, value: UByte) {
        val address = absoluteX(cpu, lsb, msb)
        array[address.toInt()] = value
    }

    override fun getAbsoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte): UByte {
        val address = absoluteY(cpu, lsb, msb)
        val value = array[address.toInt()]
        return value
    }

    override fun setAbsoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, value: UByte) {
        val address = absoluteY(cpu, lsb, msb)
        array[address.toInt()] = value
    }

    override fun getZeroPage(location: UByte): UByte {
        val address = getZeroPage(location)
        val value = array[address.toInt()]
        return value
    }

    override fun setZeroPage(location: UByte, value: UByte) {
        val address = getZeroPage(location)
        array[address.toInt()] = value
    }

    override fun getZeroPageX(cpu: CentralProcessingUnit, location: UByte): UByte {
        val address = getZeroPageX(cpu, location)
        val value = array[address.toInt()]
        return value
    }

    override fun setZeroPageX(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        val address = getZeroPageX(cpu, location)
        array[address.toInt()] = value
    }

    override fun getZeroPageY(cpu: CentralProcessingUnit, location: UByte): UByte {
        val address = getZeroPageY(cpu, location)
        val value = array[address.toInt()]
        return value
    }

    override fun setZeroPageY(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        val address = getZeroPageY(cpu, location)
        array[address.toInt()] = value
    }

    override fun getIndirectIndexedY(cpu: CentralProcessingUnit, location: UByte): UByte {
        val address = getIndirectIndexedY(cpu, location)
        val value = array[address.toInt()]
        return value
    }

    override fun setIndirectIndexedY(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        val address = getIndirectIndexedY(cpu, location)
        array[address.toInt()] = value
    }

    override fun getIndexedIndirectX(cpu: CentralProcessingUnit, location: UByte): UByte {
        val address = getIndexedIndirectX(cpu, location)
        val value = array[address.toInt()]
        return value
    }

    override fun setIndexedIndirectX(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        val address = getIndexedIndirectX(cpu, location)
        array[address.toInt()] = value
    }

    fun toMemoryAsMutableMap(): MemoryAsMutableMap {
        val memoryAsMutableMap = MemoryAsMutableMap()
        for (i in array.indices) {
            //TODO?- Maybe the condition should be removed, it is based upon the assumption that a non-defined entry will always be treated as 0.
            if (array[i] != 0.toUByte()) {
                memoryAsMutableMap[i.toUShort()] = array[i]
            }
        }
        return memoryAsMutableMap
    }

    operator fun set(index: UShort, value: UByte) {
        array[index.toInt()] = value
    }

}
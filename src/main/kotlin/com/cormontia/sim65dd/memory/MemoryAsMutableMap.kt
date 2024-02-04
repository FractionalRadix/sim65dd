package com.cormontia.sim65dd.memory

import com.cormontia.sim65dd.CentralProcessingUnit

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

    override fun getAbsoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte): UByte {
        TODO("Not yet implemented")
    }

    override fun setAbsoluteX(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, value: UByte) {
        TODO("Not yet implemented")
    }

    override fun getAbsoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte): UByte {
        TODO("Not yet implemented")
    }

    override fun setAbsoluteY(cpu: CentralProcessingUnit, lsb: UByte, msb: UByte, value: UByte) {
        TODO("Not yet implemented")
    }

    override fun getZeroPage(location: UByte): UByte {
        TODO("Not yet implemented")
    }

    override fun setZeroPage(location: UByte, value: UByte) {
        TODO("Not yet implemented")
    }

    override fun getZeroPageX(cpu: CentralProcessingUnit, location: UByte): UByte {
        TODO("Not yet implemented")
    }

    override fun setZeroPageX(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        TODO("Not yet implemented")
    }

    override fun getZeroPageY(cpu: CentralProcessingUnit, location: UByte): UByte {
        TODO("Not yet implemented")
    }

    override fun setZeroPageY(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        TODO("Not yet implemented")
    }

    override fun getIndirectIndexedY(cpu: CentralProcessingUnit, location: UByte): UByte {
        TODO("Not yet implemented")
    }

    override fun setIndirectIndexedY(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        TODO("Not yet implemented")
    }

    override fun getIndexedIndirectX(cpu: CentralProcessingUnit, location: UByte): UByte {
        TODO("Not yet implemented")
    }

    override fun setIndexedIndirectX(cpu: CentralProcessingUnit, location: UByte, value: UByte) {
        TODO("Not yet implemented")
    }

    fun toMemoryAsArray(): MemoryAsArray {
        val memoryAsArray = MemoryAsArray()
        map.forEach {
            memoryAsArray[it.key] = it.value
        }
        return memoryAsArray
    }

    operator fun set(index: UShort, value: UByte) {
        map[index] = value
    }

}
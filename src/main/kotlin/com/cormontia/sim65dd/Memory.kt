package com.cormontia.sim65dd

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
    fun indexedIndirectX(cpu: CentralProcessingUnit, location: UByte): UShort {
        val location1 = (location + cpu.x).toUByte()
        val zeroPageValueLsb = this.getZeroPage(location1)
        val zeroPageValueMsb = this.getZeroPage(location1.inc())
        val resultingLocation = (256u * zeroPageValueMsb.toUShort() + zeroPageValueLsb.toUShort())
        return resultingLocation.toUShort()
    }


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
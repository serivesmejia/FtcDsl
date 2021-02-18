package com.github.serivesmejia.ftcdsl.builder.hardware

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap

abstract class RobotBuilder {
    abstract fun build(hardwareMap: HardwareMap)

    inline fun <reified T : HardwareDevice> HardwareMap.get(name: String) = get(T::class.java, name)
}
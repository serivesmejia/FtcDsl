package com.github.serivesmejia.ftcdsl.extension.hardware

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap

object HardwareMapExt {
    inline fun <reified T : HardwareDevice> HardwareMap.getDevice(name: String): T = get(T::class.java, name)!!
}
package com.github.serivesmejia.ftcdsl.test

import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.qualcomm.robotcore.hardware.HardwareMap

inline fun <reified R : RobotBuilder> mockDslOpMode(opMode: DslOpMode<R>): DslOpMode<R> {
    opMode.hardwareMap = HardwareMap(null)
    return opMode
}
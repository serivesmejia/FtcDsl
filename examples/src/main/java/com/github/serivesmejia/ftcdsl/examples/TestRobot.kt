package com.github.serivesmejia.ftcdsl.examples

import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class TestRobot : RobotBuilder() {
    lateinit var intakeMotor: DcMotor

    override fun build(hdw: HardwareMap) {
        intakeMotor = hdw.get<DcMotor>("hello")
    }
}
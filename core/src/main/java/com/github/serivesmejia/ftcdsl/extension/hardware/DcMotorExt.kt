package com.github.serivesmejia.ftcdsl.extension.hardware

import com.qualcomm.robotcore.hardware.DcMotor

object DcMotorExt {

    infix fun DcMotor.set(power: Double) {
        this.power = power
    }

}
package com.github.serivesmejia.ftcdsl

import com.github.serivesmejia.ftcdsl.Dsl.linearOpMode
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor

class Test {

    fun foo() {
        linearOpMode {
            run {
                val motor = dcMotor("helo")

                motor set 1.0
                delay(1000)
            }
        }
    }

}

private infix fun DcMotor.set(i: Double) {
    power = i
}
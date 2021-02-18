package com.github.serivesmejia.ftcdsl

import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.gamepad.ButtonState
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class IterativeTest : DslOpMode<TestRobot>({
    robot = TestRobot()

    gamepad1 {
        button(A) {
            when(it) {
                ButtonState.PRESSED -> {

                }
                else -> {

                }
            }
        }


    }

    iterative {
        loop {
            if(gamepad1.a) {
                robot?.intakeMotor?.power = 1.0
            } else {
                robot?.intakeMotor?.power = 0.0
            }
        }
    }
})
package com.github.serivesmejia.ftcdsl.examples

import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class IterativeTest : DslOpMode<TestRobot>({
    robot = TestRobot()

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
package com.github.serivesmejia.ftcdsl

import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="helo")
class LinearTest : DslOpMode<TestRobot>({
    robot = TestRobot()

    linear {
        waitForStart()

        whileActive {
            if(gamepad1.a) {
                robot?.intakeMotor?.power = 1.0
            } else {
                robot?.intakeMotor?.power = 0.0
            }
        }
    }
})
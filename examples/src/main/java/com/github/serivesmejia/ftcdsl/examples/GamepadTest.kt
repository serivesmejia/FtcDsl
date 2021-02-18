package com.github.serivesmejia.ftcdsl.examples

import com.github.serivesmejia.ftcdsl.extension.hardware.DcMotorExt.set
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class GamepadTest : DslOpMode<TestRobot>({
    robot = TestRobot()

    gamepad1 {
        button(B) {
            pressed {
                //...
            }
            released {
                //...
            }
            pressing {
                //...
            }
        }
    }

    gamepad2 { //this shorter syntax is also valid:
        button(A).pressed {
            robot?.intakeMotor?.set(1.0)
        }
    }
})
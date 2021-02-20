package com.github.serivesmejia.ftcdsl.examples.pushbot

import com.github.serivesmejia.ftcdsl.extension.hardware.DcMotorExt.power
import com.github.serivesmejia.ftcdsl.extension.hardware.ServoExt.position
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.qualcomm.robotcore.util.Range

class TeleOpTank_Iterative : DslOpMode<PushBotRobot>({
    robot = PushBotRobot()

    val CLAW_SPEED = 0.02
    var clawOffset = 0.0

    gamepad1 {
        // Use gamepad left & right bumpers to open and close the claw
        button(rightBumper).pressing {
            clawOffset += CLAW_SPEED
        }
        button(leftBumper).pressing {
            clawOffset -= CLAW_SPEED
        }

        buttons(Y, A) {
            pressing {
                if(`is`(Y))
                    robot.leftArm power robot.ARM_UP_POWER
                else if(`is`(A))
                    robot.leftArm power robot.ARM_DOWN_POWER
            }

            released {
                robot.leftArm power 0.0
            }
        }

        update {
            // Running wheels in tank mode (note that the joystick
            // goes negative when pushed forwards, so negate it)
            val left = -gamepad1.left_stick_y.toDouble()
            val right = -gamepad1.right_stick_y.toDouble()

            robot.leftDrive power left
            robot.rightDrive power right

            // Move both servos to new position. Assume
            // servos are mirror image of each other
            val clawOffset = Range.clip(clawOffset, -0.5, 0.5)
            robot.leftClaw position robot.MID_SERVO + clawOffset
            robot.rightClaw position robot.MID_SERVO - clawOffset

            telemetry {
                data("claw", "Offset = %.2f", clawOffset)
                data("left", "%.2f", left)
                data("right", "%.2f", right)
            }
        }
    }

    iterative.init {
        //Send telemetry message to signify robot waiting;
        telemetry { data("Say", "Hello Driver") }
    }

})
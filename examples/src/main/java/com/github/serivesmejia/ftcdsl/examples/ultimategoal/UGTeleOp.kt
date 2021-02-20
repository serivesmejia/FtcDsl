package com.github.serivesmejia.ftcdsl.examples.ultimategoal

import com.github.serivesmejia.ftcdsl.extension.hardware.DcMotorExt.power
import com.github.serivesmejia.ftcdsl.extension.hardware.ServoExt.position
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import kotlin.math.abs

class UGTeleOp : DslOpMode<UGRobot>({

    robot = UGRobot()

    gamepad1 {
        //servo flick when pressing A button
        button(A).pressing {
            robot.flickServo()
        }

        //mecanum drive code
        update {
            //get values from joysticks
            val y1 = -gamepad1.left_stick_y
            val x1 = gamepad1.left_stick_x
            val x2 = gamepad1.right_stick_x

            //calculate unnormalized powers
            var fl = y1 - x2 - x1
            var fr = y1 - x2 + x1
            var bl = y1 + x2 + x1
            var br = y1 + x2 - x1

            //get max value from the powers
            var max = abs(fl).coerceAtLeast(abs(bl))
                max = abs(fr).coerceAtLeast(max)
                max = abs(br).coerceAtLeast(max)

            //normalize values
            if(max > 1.0) {
                fl /= max
                fr /= max
                bl /= max
                br /= max
            }

            //set power to drive motors
            robot.frontLeft  power fl.toDouble()
            robot.frontRight power fr.toDouble()
            robot.backLeft   power bl.toDouble()
            robot.backRight  power br.toDouble()

            telemetry {
                data("frontLeft", fl)
                data("frontRight", fr)
                data("backLeft", bl)
                data("backRight", br)
            }
        }
    }

    gamepad2 {
        //close claw with X button
        button(X).pressed {
            robot.wobbleClaw position 1.0
        }
        //open claw with Y button
        button(Y).pressed {
            robot.wobbleClaw position 0.0
        }

        //controlling intake with A & B buttons for in and out respectively
        buttons(A, B) {
            pressing {
                if(`is`(A)) {
                    robot.intake power 1.0
                } else if(`is`(B)) {
                    robot.intake power -1.0
                }
            }

            released { robot.intake power 0.0 }
        }
    }

    iterative.init {
        telemetry { data("[Status]", "Ready") }
    }

})
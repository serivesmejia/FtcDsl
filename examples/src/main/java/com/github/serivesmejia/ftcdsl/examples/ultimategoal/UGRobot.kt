package com.github.serivesmejia.ftcdsl.examples.ultimategoal

import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.extension.hardware.DcMotorExt.direction
import com.github.serivesmejia.ftcdsl.extension.hardware.DcMotorExt.mode
import com.github.serivesmejia.ftcdsl.extension.hardware.DcMotorExt.reverse
import com.github.serivesmejia.ftcdsl.extension.hardware.DcMotorExt.usingEncoder
import com.github.serivesmejia.ftcdsl.extension.hardware.ServoExt.position
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo

class UGRobot : RobotBuilder() {

    lateinit var frontLeft: DcMotor
    lateinit var frontRight: DcMotor
    lateinit var backLeft: DcMotor
    lateinit var backRight: DcMotor

    lateinit var intake: DcMotor
    lateinit var wobbleArm: DcMotor

    lateinit var wobbleClaw: Servo
    lateinit var shooterFlicker: Servo

    var servoMoving = false

    override fun build() {
        frontLeft  = device("frontleft")
        frontRight = device("frontright")
        backLeft   = device("backleft")
        backRight  = device("backright")

        intake     = device("intake")
        wobbleArm  = device("wobblearm")

        wobbleClaw     = device("wobbleclaw")
        shooterFlicker = device("flicker")

        frontRight direction reverse
        backRight direction reverse

        frontLeft mode usingEncoder
        frontRight mode usingEncoder
        backLeft mode usingEncoder
        backRight mode usingEncoder

        shooterFlicker position 0.0
    }

    fun flickServo() = withOpMode {
        if(!servoMoving) {
            servoMoving = true

            shooterFlicker position 1.0

            timeout(500.0) {
                shooterFlicker position 0.0
                servoMoving = false
            }
        }
    }

}
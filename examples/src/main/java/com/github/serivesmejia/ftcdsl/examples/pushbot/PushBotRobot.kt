package com.github.serivesmejia.ftcdsl.examples.pushbot

import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class PushBotRobot : RobotBuilder() {

    lateinit var leftDrive: DcMotor
    lateinit var rightDrive: DcMotor

    lateinit var leftArm: DcMotor

    lateinit var leftClaw: Servo
    lateinit var rightClaw: Servo

    val MID_SERVO = 0.5
    val ARM_UP_POWER = 0.5
    val ARM_DOWN_POWER = 0.5

    override fun build() {
        leftDrive = device("left_drive")
        rightDrive = device("right_drive")
        leftArm = device("left_arm")

        rightDrive.direction = DcMotorSimple.Direction.FORWARD

        leftClaw = device("left_hand")
        rightClaw = device("right_hand")

        leftClaw.position = MID_SERVO
        rightClaw.position = MID_SERVO
    }

}
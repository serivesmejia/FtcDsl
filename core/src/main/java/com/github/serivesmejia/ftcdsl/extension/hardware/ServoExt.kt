package com.github.serivesmejia.ftcdsl.extension.hardware

import com.qualcomm.robotcore.hardware.Servo

object ServoExt {

    val forward = Servo.Direction.FORWARD
    val reverse = Servo.Direction.REVERSE

    infix fun Servo.position(position: Double) = setPosition(position)
    infix fun Servo.direction(direction: Servo.Direction) = setDirection(direction)

}
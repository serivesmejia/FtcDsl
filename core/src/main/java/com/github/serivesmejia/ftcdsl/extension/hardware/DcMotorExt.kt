package com.github.serivesmejia.ftcdsl.extension.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

object DcMotorExt {

    val forward = DcMotorSimple.Direction.FORWARD
    val reverse = DcMotorSimple.Direction.REVERSE

    val withoutEncoder = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    val usingEncoder = DcMotor.RunMode.RUN_USING_ENCODER
    val toPosition = DcMotor.RunMode.RUN_TO_POSITION

    val float = DcMotor.ZeroPowerBehavior.FLOAT
    val brake = DcMotor.ZeroPowerBehavior.BRAKE

    infix fun DcMotorSimple.power(power: Double) = setPower(power)
    infix fun DcMotor.run(runMode: DcMotor.RunMode) = setMode(runMode)
    infix fun DcMotorSimple.direction(direction: DcMotorSimple.Direction) = setDirection(direction)
    infix fun DcMotor.onStop(behavior: DcMotor.ZeroPowerBehavior) = setZeroPowerBehavior(behavior)

    infix fun DcMotorEx.velocity(velocity: Double) = setVelocity(velocity)

}
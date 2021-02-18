package com.github.serivesmejia.ftcdsl.opmode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo

open class DslLinearOpMode(private val run: DslLinearOpMode.() -> Unit) : LinearOpMode() {

    override fun runOpMode() = run()

    inline fun <reified T> device(name: String): T = hardwareMap.get(T::class.java, name)!!

    fun dcMotor(name: String) = device<DcMotor>(name)
    fun dcMotorEx(name: String) = device<DcMotorEx>(name)

    fun servo(name: String) = device<Servo>(name)

    infix fun delay(millis: Long) {
        sleep(millis)
    }

}
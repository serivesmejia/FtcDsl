package com.github.serivesmejia.ftcdsl.builder.hardware

import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.qualcomm.robotcore.hardware.*

abstract class RobotBuilder {

    lateinit var hardwareMap: HardwareMap
    lateinit var opMode: DslOpMode<*>

    fun internalBuild(opMode: DslOpMode<*>) {
        this.opMode = opMode
        this.hardwareMap = opMode.hardwareMap
        build()
    }

    inline fun <reified T> withOpMode(callback: DslOpMode<*>.() -> T): T = callback(opMode)

    abstract fun build()

    inline fun <reified T : HardwareDevice> device(name: String) = hardwareMap.get(T::class.java, name)!!

    infix fun dcMotor(name: String) = device<DcMotor>(name)
    infix fun dcMotorEx(name: String) = device<DcMotorEx>(name)

    infix fun servo(name: String) = device<Servo>(name)
}

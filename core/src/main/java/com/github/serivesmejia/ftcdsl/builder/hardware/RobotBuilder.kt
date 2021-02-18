package com.github.serivesmejia.ftcdsl.builder.hardware

import com.qualcomm.robotcore.hardware.*

abstract class RobotBuilder {

    lateinit var hardwareMap: HardwareMap

    fun internalBuild(hardwareMap: HardwareMap) {
        this.hardwareMap = hardwareMap
        build()
    }

    abstract fun build()

    inline fun <reified T : HardwareDevice> device(name: String) = hardwareMap.get(T::class.java, name)!!

    infix fun dcMotor(name: String) = device<DcMotor>(name)
    infix fun dcMotorEx(name: String) = device<DcMotorEx>(name)

    infix fun servo(name: String) = device<Servo>(name)
}

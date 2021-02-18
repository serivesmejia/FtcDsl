package com.github.serivesmejia.ftcdsl.opmode

import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.DslOpModeBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.*

open class DslOpMode<R: RobotBuilder>(buildCallback: DslOpModeBuilder<R>.() -> Unit) : LinearOpMode() {

    private var builder: DslOpModeBuilder<R> = DslOpModeBuilder()

    init {
        buildCallback(builder)
    }

    var robot: R? = null
        set(value) {
            field?.let {
                throw IllegalAccessException("robot variable cannot be modified twice")
            }
            field = value
        }

    override fun runOpMode() = builder.execute(this)

    fun whileActive(callback: () -> Unit) {
        while(!Thread.currentThread().isInterrupted) {
            callback()
        }
    }
    
    fun updateGamepads() = builder.executeGamepads()

    inline fun <reified T : HardwareDevice> device(name: String): T = hardwareMap.get(T::class.java, name)!!

    infix fun dcMotor(name: String) = device<DcMotor>(name)
    infix fun dcMotorEx(name: String) = device<DcMotorEx>(name)

    infix fun servo(name: String) = device<Servo>(name)

}
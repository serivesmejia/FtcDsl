package com.github.serivesmejia.ftcdsl.opmode

import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.DslOpModeBuilder
import com.github.serivesmejia.ftcdsl.builder.dsl.telemetry.DslTelemetryBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.util.ElapsedTime

open class DslOpMode<R: RobotBuilder>(buildCallback: DslOpModeBuilder<R>.() -> Unit) : LinearOpMode() {

    private var builder: DslOpModeBuilder<R> = DslOpModeBuilder()
    lateinit var robot: R

    private var timer = ElapsedTime()

    init {
        buildCallback(builder)
    }

    override fun runOpMode() = builder.execute(this)

    fun telemetry(callback: DslTelemetryBuilder.() -> Unit) {
        val builder = DslTelemetryBuilder(telemetry)
        callback(builder)

        builder.execute()
    }

    fun whileActive(callback: () -> Unit) {
        while(!Thread.currentThread().isInterrupted) {
            callback()
        }
    }

    fun whileTime(millis: Double, callback: (elapsedMillis: Double) -> Unit) {
        timer.reset()

        while(timer.milliseconds() < millis) {
            callback(timer.milliseconds())
        }
    }
    
    fun updateGamepads() = builder.executeGamepads()

    inline fun <reified T : HardwareDevice> device(name: String): T = hardwareMap.get(T::class.java, name)!!

    infix fun dcMotor(name: String) = device<DcMotor>(name)
    infix fun dcMotorEx(name: String) = device<DcMotorEx>(name)

    infix fun servo(name: String) = device<Servo>(name)

}
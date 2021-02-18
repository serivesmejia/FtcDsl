package com.github.serivesmejia.ftcdsl.opmode

import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.OpModeDslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.*

open class DslOpMode<R: RobotBuilder>(buildCallback: OpModeDslBuilder<R>.() -> Unit) : LinearOpMode() {

    private val builder = OpModeDslBuilder(this)

    var robot: R? = null
        set(value) {
            field?.let {
                throw IllegalAccessException("robot variable cannot be modified at this point")
            }
            field = value
        }

    init {
        buildCallback(builder)
    }

    override fun runOpMode() {
        robot?.build(hardwareMap)
        builder.execute()
    }

    fun whileActive(callback: () -> Unit) {
        while(!Thread.currentThread().isInterrupted) {
            callback()
        }
    }

    inline fun <reified T : HardwareDevice> device(name: String): T = hardwareMap.get(T::class.java, name)!!

    infix fun dcMotor(name: String) = device<DcMotor>(name)
    infix fun dcMotorEx(name: String) = device<DcMotorEx>(name)

    infix fun servo(name: String) = device<Servo>(name)

}
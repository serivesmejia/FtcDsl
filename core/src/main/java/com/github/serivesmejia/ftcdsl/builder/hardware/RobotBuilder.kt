package com.github.serivesmejia.ftcdsl.builder.hardware

import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.qualcomm.robotcore.hardware.*

abstract class RobotBuilder {

    lateinit var hardwareMap: HardwareMap
        private set
    var opMode: DslOpMode<*>? = null
        private set

    fun internalBuild(opMode: DslOpMode<*>) {
        this.opMode = opMode
        this.hardwareMap = opMode.hardwareMap
        build()
    }

    inline fun <reified T> withOpMode(callback: DslOpMode<*>.() -> T): T {
        opMode?.let {
            return callback(it)
        }
        throw IllegalStateException("Cannot call withOpMode before the OpMode has actually started")
    }

    abstract fun build()

    inline fun <reified T : HardwareDevice> device(name: String) = hardwareMap.get(T::class.java, name)!!

}

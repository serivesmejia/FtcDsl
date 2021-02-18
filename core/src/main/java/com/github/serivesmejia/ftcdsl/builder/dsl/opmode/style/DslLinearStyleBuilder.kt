package com.github.serivesmejia.ftcdsl.builder.dsl.opmode.style

import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class DslLinearStyleBuilder<R : RobotBuilder>(private var callback: DslOpMode<R>.() -> Unit) : DslOpModeStyleBuilder<R>() {
    override fun execute() {
        opMode?.let {
            callback(it)
        }
    }
}
package com.github.serivesmejia.ftcdsl.builder.dsl.opmode

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

open class LinearDslBuilder<R : RobotBuilder>(private val opMode: DslOpMode<R>,
                                              private var callback: DslOpMode<R>.() -> Unit) : DslBuilder {
    override fun execute() {
        callback(opMode)
    }
}
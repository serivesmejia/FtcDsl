package com.github.serivesmejia.ftcdsl.builder.dsl.opmode.style

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

abstract class DslOpModeStyleBuilder<R : RobotBuilder> : DslBuilder {
    internal var opMode: DslOpMode<R>? = null
}
package com.github.serivesmejia.ftcdsl.builder.dsl.opmode.gamepad

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class GamepadButtonDslBuilder<R : RobotBuilder>(private val button: Button,
                                                private val gamepadDslBuilder: GamepadDslBuilder<R>,
                                                private val opMode: DslOpMode<R>) : DslBuilder {

    fun pressed(callback: DslOpMode<R>.() -> Unit) {

    }

    fun released(callback: DslOpMode<R>.() -> Unit) {

    }

    fun pressing(callback: DslOpMode<R>.() -> Unit) {

    }

    override fun execute() {
        gamepadDslBuilder.gamepad?.let {

        }
    }

}
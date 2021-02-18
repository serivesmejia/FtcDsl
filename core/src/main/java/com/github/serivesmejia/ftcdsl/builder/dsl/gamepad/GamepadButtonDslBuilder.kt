package com.github.serivesmejia.ftcdsl.builder.dsl.gamepad

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.extension.hardware.GamepadExt.get
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class GamepadButtonDslBuilder<R : RobotBuilder>(private val button: Button,
                                                private val gamepadDslBuilder: GamepadDslBuilder<R>) : DslBuilder {

    internal var opMode: DslOpMode<R>? = null

    private var pressedCall: (DslOpMode<R>.() -> Unit)? = null
    private var releasedCall: (DslOpMode<R>.() -> Unit)? = null
    private var pressingCall: (DslOpMode<R>.() -> Unit)? = null

    fun pressed(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(pressedCall, "released")
        pressedCall = callback
    }

    fun released(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(pressingCall, "released")
        releasedCall = callback
    }

    fun pressing(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(pressingCall, "pressing")
        pressingCall = callback
    }

    var beforePressed = false

    override fun execute() {
        gamepadDslBuilder.gamepad?.let { gamepad ->
            val pressing = gamepad.get(button)

            if(!beforePressed && pressing) {
                call(pressedCall)
            } else if(!pressing && beforePressed) {
                call(releasedCall)
            }

            if(pressing) call(pressingCall)

            beforePressed = pressing
        }
    }

    private fun checkCallDeclared(callback: (DslOpMode<R>.() -> Unit)?, name: String) {
        callback?.let {
            throw IllegalStateException("Callback for $name has been already declared once")
        }
    }

    private fun call(callback: (DslOpMode<R>.() -> Unit)?) {
        callback?.let{
            opMode?.let {
                callback(it)
            }
        }
    }

}
package com.github.serivesmejia.ftcdsl.builder.dsl.gamepad

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.extension.hardware.GamepadExt.get
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

/**
 * Represents a gamepad button to be built inside
 * a GamepadDslBuilder and executed in a DslOpMode.
 * @see GamepadDslBuilder
 */
class GamepadButtonDslBuilder<R : RobotBuilder>(private val button: Button,
                                                private val gamepadDslBuilder: GamepadDslBuilder<R>) : DslBuilder {

    internal var opMode: DslOpMode<R>? = null

    private var pressedCall: (DslOpMode<R>.() -> Unit)? = null
    private var releasedCall: (DslOpMode<R>.() -> Unit)? = null
    private var pressingCall: (DslOpMode<R>.() -> Unit)? = null

    /**
     * Registers a callback that will be called ONCE when this button is pressed
     * @param callback the callback to be called on press
     * @throws IllegalStateException if this callback for this button has been already registered
     */
    fun pressed(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(pressedCall, "released")
        pressedCall = callback
    }

    /**
     * Registers a callback that will be called ONCE when this button is released
     * @param callback the callback to be called on press
     * @throws IllegalStateException if this callback for this button has been already registered
     */
    fun released(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(pressingCall, "released")
        releasedCall = callback
    }

    /**
     * Registers a callback that will be called REPETITIVELY
     * when a button is pressed until is released
     * @param callback the callback to be called repetitively during pressing
     * @throws IllegalStateException if this callback for this button has been already registered
     */
    fun pressing(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(pressingCall, "pressing")
        pressingCall = callback
    }

    fun `is`(button: Button) = gamepadDslBuilder.gamepad?.get(button) ?: false

    var beforePressed = false

    /**
     * Updates/executes this button. This is where the registered callbacks will be executed
     */
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
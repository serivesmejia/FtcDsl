package com.github.serivesmejia.ftcdsl.builder.dsl.gamepad

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.qualcomm.robotcore.hardware.Gamepad

class GamepadDslBuilder<R : RobotBuilder>() : DslBuilder {

    val A = Button.A
    val B = Button.B
    val X = Button.X
    val Y = Button.Y

    val dpadUp = Button.DPAD_UP
    val dpadDown = Button.DPAD_DOWN
    val dpadLeft = Button.DPAD_LEFT
    val dpadRight = Button.DPAD_RIGHT

    private val buttonBuilders = HashMap<Button, GamepadButtonDslBuilder<R>>()

    var gamepad: Gamepad? = null
    var opMode: DslOpMode<R>? = null

    fun button(button: Button, callback: GamepadButtonDslBuilder<R>.() -> Unit) {
        callback(button(button))
    }

    fun button(button: Button): GamepadButtonDslBuilder<R> {
        if(buttonBuilders.containsKey(button)) {
            throw IllegalStateException("Callback has been already registered for button $button")
        }

        val builder = GamepadButtonDslBuilder(button, this)
        buttonBuilders[button] = builder

        return builder
    }

    override fun execute() = buttonBuilders.values.forEach {
        it.opMode = opMode
        it.execute()
    }

}
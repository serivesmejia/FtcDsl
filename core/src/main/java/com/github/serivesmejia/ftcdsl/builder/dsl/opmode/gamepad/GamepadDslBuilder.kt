package com.github.serivesmejia.ftcdsl.builder.dsl.opmode.gamepad

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.qualcomm.robotcore.hardware.Gamepad

class GamepadDslBuilder<R : RobotBuilder>(private val opMode: DslOpMode<R>) : DslBuilder {

    val A = Button.A
    val B = Button.B
    val X = Button.X
    val Y = Button.Y

    private val buttonBuilders = HashMap<Button, GamepadButtonDslBuilder<R>>()

    var gamepad: Gamepad? = null

    fun button(button: Button, callback: GamepadButtonDslBuilder<R>.() -> Unit) {
        if(buttonBuilders.containsKey(button)) {
            throw IllegalStateException("Callback already registered for button $button")
        }

        val builder = GamepadButtonDslBuilder(opMode)
        callback(builder)

        buttonBuilders[button] = builder
    }

    override fun execute() {
        for(builder : buttonBuilders) {
            builder.execute()
        }
    }

}
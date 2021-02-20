package com.github.serivesmejia.ftcdsl.builder.dsl.gamepad

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.qualcomm.robotcore.hardware.Gamepad

/**
 * Represents a gamepad to be built inside a DslOpMode Builder.
 * It's the parent builder of GamepadButtonDslBuilder's
 * @see GamepadButtonDslBuilder
 * @see DslOpModeBuilder
 */
class GamepadDslBuilder<R : RobotBuilder> : DslBuilder {

    val A = Button.A
    val B = Button.B
    val X = Button.X
    val Y = Button.Y

    val dpadUp = Button.DPAD_UP
    val dpadDown = Button.DPAD_DOWN
    val dpadLeft = Button.DPAD_LEFT
    val dpadRight = Button.DPAD_RIGHT

    val leftBumper = Button.LEFT_BUMPER
    val rightBumper = Button.RIGHT_BUMPER
    val leftTrigger = Button.LEFT_TRIGGER
    val rightTrigger = Button.RIGHT_TRIGGER

    val leftJoystick = Button.LEFT_JOYSTICK
    val rightJoystick = Button.RIGHT_JOYSTICK

    val start = Button.START
    val back = Button.BACK

    private val buttonBuilders = HashMap<Button, GamepadButtonDslBuilder<R>>()

    var gamepad: Gamepad? = null
    var opMode: DslOpMode<R>? = null

    private var updateCall: (DslOpMode<R>.() -> Unit)? = null

    /**
     * Builds a single gamepad button with a builder callback.<br/>
     * Allows for the following syntax:
     * <pre>
     * {@code
     *  gamepad1 {
     *      button(A) {
     *          pressed { ... }
     *          //...
     *      }
     *  }
     * }
     * </pre>
     * @throws IllegalStateException if the specified button has already been built
     */
    fun button(button: Button, callback: GamepadButtonDslBuilder<R>.() -> Unit) {
        callback(button(button))
    }

    /**
    * Builds multiple gamepad buttons with a builder callback
    * Allows for the following syntax:
    * <pre>
    * {@code
    *  gamepad1 {
    *      buttons(A, B) {
    *          pressed { ... }
    *          //...
    *      }
    *  }
    * }
    * </pre>
    * @throws IllegalStateException if one or more of the specified buttons has already been built
    */
    fun buttons(vararg buttons: Button, callback: GamepadButtonDslBuilder<R>.() -> Unit) {
        for(button in buttons) {
            button(button, callback)
        }
    }

    /**
     * Returns a button builder for the specified button.
     * Allows for the following syntax:
     * <pre>
     * {@code
     *  gamepad1 {
     *      button(A).pressed {
     *          //...
     *      }
     *  }
     * }
     * </pre>
     * @throws IllegalStateException if one or more of the specified buttons has already been built
     */
    fun button(button: Button): GamepadButtonDslBuilder<R> {
        if(buttonBuilders.containsKey(button)) {
            throw IllegalStateException("Callback has been already registered for button $button")
        }

        val builder = GamepadButtonDslBuilder(button, this)
        buttonBuilders[button] = builder

        return builder
    }

    /**
     * Registers a callback that will be called repetitively
     * every time this gamepad is executed
     */
    fun update(callback: DslOpMode<R>.() -> Unit) {
        updateCall?.let {
            throw IllegalStateException("Only one update callback for gamepad DSL is allowed")
        }
        updateCall = callback
    }

    /**
     * Executes/updates this gamepad. This is where all the user callbacks will get executed
     */
    override fun execute() = buttonBuilders.values.forEach {
        opMode?.let { opMode ->
            updateCall?.let { call ->
                call(opMode)
            }

            it.opMode = opMode
            it.execute()
        }
    }

}
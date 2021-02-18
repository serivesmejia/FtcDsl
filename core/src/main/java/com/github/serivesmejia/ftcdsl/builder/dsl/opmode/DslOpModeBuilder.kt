package com.github.serivesmejia.ftcdsl.builder.dsl.opmode

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.dsl.gamepad.GamepadDslBuilder
import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.style.DslOpModeStyleBuilder
import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.style.DslIterativeStyleBuilder
import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.style.DslLinearStyleBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class DslOpModeBuilder<R : RobotBuilder> : DslBuilder {

    private var builder: DslOpModeStyleBuilder<R>? = null

    var robot: R? = null
        set(value) {
            field?.let {
                throw IllegalAccessException("robot variable cannot be set more than once")
            }
            field = value
        }

    private val gamepad1 = GamepadDslBuilder<R>()
    private val gamepad2 = GamepadDslBuilder<R>()

    fun iterative(callback: DslIterativeStyleBuilder<R>.() -> Unit) {
        checkBuilderDeclared()
        builder = DslIterativeStyleBuilder()

        //call the callback which will build the DSL
        callback(builder!! as DslIterativeStyleBuilder<R>)
    }

    fun linear(callback: DslOpMode<R>.() -> Unit) {
        checkBuilderDeclared()
        builder = DslLinearStyleBuilder(callback)
        //no need to call callback here since we're not building anything else for linear
    }

    fun gamepad1(callback: GamepadDslBuilder<R>.() -> Unit) = callback(gamepad1)

    fun gamepad2(callback: GamepadDslBuilder<R>.() -> Unit) = callback(gamepad2)

    fun executeGamepads() {
        gamepad1.execute()
        gamepad2.execute()
    }

    //simple check to throw an exception if user is
    //trying to register more than one callback...
    private fun checkBuilderDeclared() {
        builder?.let {
            throw IllegalStateException("Can't define more than 1 callback for the root DSL (only 1 linear or iterative allowed)")
        }
    }

     fun execute(opMode: DslOpMode<R>?) {
         //setting the dsl gamepads the sdk instances
         gamepad1.opMode = opMode
         gamepad1.gamepad = opMode?.gamepad1

         gamepad2.opMode = opMode
         gamepad2.gamepad = opMode?.gamepad2

         opMode?.robot = robot
         opMode?.hardwareMap?.let { robot?.internalBuild(it) }

         //build mini dsl here if user is just using the gamepad stuff
         if(builder == null) {
            linear {
                waitForStart()
                whileActive {
                    updateGamepads()
                }
            }
        }

        builder!!.opMode = opMode

        //actually execute the user dsl
        builder!!.execute()
    }

    override fun execute() = execute(null)

}
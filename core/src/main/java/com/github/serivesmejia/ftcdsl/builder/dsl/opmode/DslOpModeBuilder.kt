package com.github.serivesmejia.ftcdsl.builder.dsl.opmode

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.dsl.gamepad.GamepadDslBuilder
import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.style.DslOpModeStyleBuilder
import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.style.DslIterativeStyleBuilder
import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.style.DslLinearStyleBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.EmptyRobot
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.builder.misc.TimerBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.github.serivesmejia.ftcdsl.util.Timer

class DslOpModeBuilder<R : RobotBuilder>(private val timerBuilder: TimerBuilder) : DslBuilder {

    private var styleBuilder: DslOpModeStyleBuilder<R>? = null

    var opMode: DslOpMode<R>? = null

    var robot: R? = null
        set(value) {
            field?.let {
                throw IllegalAccessException("robot variable cannot be set more than once")
            }
            field = value
        }

    private var gamepad1: GamepadDslBuilder<R>? = null
    private var gamepad2: GamepadDslBuilder<R>? = null

    val iterative : DslIterativeStyleBuilder<R>
        get() {
            checkBuilderDeclared()
            //declare in different val to have iterative type
            //and not having to cast later when returning
            val builder = DslIterativeStyleBuilder<R>()
            this.styleBuilder = builder

            return builder
        }

    fun iterative(callback: DslIterativeStyleBuilder<R>.() -> Unit) {
        //call the callback which will build the DSL
        callback(iterative)
    }

    fun linear(callback: DslOpMode<R>.() -> Unit) {
        checkBuilderDeclared()
        styleBuilder = DslLinearStyleBuilder(callback)
        //no need to call callback here since we're not building anything else for linear
    }

    fun gamepad1(callback: GamepadDslBuilder<R>.() -> Unit) {
        gamepad1?.let {
            throw IllegalStateException("Can't define more than 1 callback for gamepad1")
        }

        gamepad1 = GamepadDslBuilder()
        callback(gamepad1!!)
    }

    fun gamepad2(callback: GamepadDslBuilder<R>.() -> Unit) {
        gamepad2?.let {
            throw IllegalStateException("Can't define more than 1 callback for gamepad2")
        }

        gamepad2 = GamepadDslBuilder()
        callback(gamepad1!!)
    }

    fun timeout(millis: Double, call: (Timer) -> Unit) = timerBuilder.setTimeout(millis, call)

    fun interval(millis: Double, call: (Timer) -> Unit) = timerBuilder.setInterval(millis, call)

    /**
     * Runs a block of code in the context of an OpMode<br/>
     * Useful when declaring functions in this DslOpModeBuilder context
     */
    inline fun <reified T> withOpMode(callback: DslOpMode<R>.() -> T): T {
        opMode?.let {
            return callback(it)
        }
        throw IllegalStateException("Cannot call withOpMode before the OpMode has actually started")
    }

    fun executeGamepads() {
        gamepad1?.execute()
        gamepad2?.execute()
    }

    //simple check to throw an exception if user is
    //trying to register more than one callback...
    private fun checkBuilderDeclared() {
        styleBuilder?.let {
            throw IllegalStateException("Can't define more than 1 callback for the root DSL (only 1 linear or iterative allowed)")
        }
    }

     fun execute(opMode: DslOpMode<R>) {
         //setting the dsl gamepads the sdk instances
         this.opMode = opMode

         gamepad1?.opMode = opMode
         gamepad1?.gamepad = opMode.gamepad1

         gamepad2?.opMode = opMode
         gamepad2?.gamepad = opMode.gamepad2

         if(robot == null) {
             robot = EmptyRobot as R
         }

         opMode.robot = robot!!
         robot!!.internalBuild(opMode)

         //build mini dsl here if user is just using the gamepad
         //stuff or didn't declared a style for any reason
         if(styleBuilder == null) {
            linear {
                waitForStart()
                whileActive {
                    update() //simply update
                }
            }
         }

         styleBuilder!!.opMode = opMode

         //actually execute the dsl
         styleBuilder!!.execute()
    }

    override fun execute() {
        throw UnsupportedOperationException("execute() without any parameters isn't available for DslOpModeBuilder (need to pass a DslOpMode instance)")
    }

}
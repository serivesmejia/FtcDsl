package com.github.serivesmejia.ftcdsl.builder.dsl.opmode

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class OpModeDslBuilder<R : RobotBuilder>(private val opMode: DslOpMode<R>) : DslBuilder {

    private var builder: DslBuilder? = null

    var robot: R? = null
        set(value) {
            field?.let {
                throw IllegalAccessException("robot variable cannot be set more than once")
            }
            field = value
            opMode.robot = value
        }

    fun iterative(callback: IterativeDslBuilder<R>.() -> Unit) {
        checkBuilderDeclared()
        builder = IterativeDslBuilder(opMode)

        //call the callback which will build the DSL
        callback(builder!! as IterativeDslBuilder<R>)
    }

    fun linear(callback: DslOpMode<R>.() -> Unit) {
        checkBuilderDeclared()
        builder = LinearDslBuilder(opMode, callback)
        //no need to call callback here since we're not building anything else for linear
    }

    //simple check to throw an exception if user is
    //trying to register more than one callback...
    private fun checkBuilderDeclared() {
        builder?.let {
            throw IllegalStateException("Can't define more than 1 callback for the DSL (only 1 linear or iterative)")
        }
    }

    override fun execute() {
        builder?.execute()
    }

}
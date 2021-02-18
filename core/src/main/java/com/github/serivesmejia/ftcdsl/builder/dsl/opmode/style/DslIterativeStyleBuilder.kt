package com.github.serivesmejia.ftcdsl.builder.dsl.opmode

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class IterativeDslBuilder<R : RobotBuilder>(private val opMode: DslOpMode<R>) : DslBuilder {

    private var initCall: (DslOpMode<R>.() -> Unit)? = null
    private var initLoopCall: (DslOpMode<R>.() -> Unit)? = null
    private var startCall: (DslOpMode<R>.() -> Unit)? = null
    private var loopCall: (DslOpMode<R>.() -> Unit)? = null
    private var stopCall: (DslOpMode<R>.() -> Unit)? = null

    fun init(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(initCall, "init")
        initCall = callback
    }

    fun initLoop(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(initLoopCall, "initLoop")
        initLoopCall = callback
    }

    fun start(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(startCall, "start")
        startCall = callback
    }

    fun loop(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(loopCall, "loop")
        loopCall = callback
    }

    fun stop(callback: DslOpMode<R>.() -> Unit) {
        checkCallDeclared(stopCall, "stop")
        stopCall = callback
    }

    override fun execute() {
        call(initCall)

        while(!opMode.isStarted && !opMode.isStopRequested) {
            call(initLoopCall)
            opMode.updateGamepads()
        }

        call(startCall)

        while(!Thread.currentThread().isInterrupted) {
            call(loopCall)
            opMode.updateGamepads()
        }

        call(stopCall)
    }

    private fun call(call: (DslOpMode<R>.() -> Unit)?) {
        call?.let { it(opMode) }
    }

    private fun checkCallDeclared(callback: (DslOpMode<R>.() -> Unit)?, name: String) {
        callback?.let {
            throw IllegalStateException("Callback for $name has been already declared once")
        }
    }

}
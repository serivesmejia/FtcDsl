package com.github.serivesmejia.ftcdsl.builder.dsl.opmode.style

import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class DslIterativeStyleBuilder<R : RobotBuilder> : DslOpModeStyleBuilder<R>() {

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
        opMode?.let { opMode ->
            call(initCall)

            while (!opMode.isStarted && !opMode.isStopRequested) {
                call(initLoopCall)
            }

            //exit if user pressed stop after init
            if(opMode.isStopRequested) return

            call(startCall)

            while (!Thread.currentThread().isInterrupted) {
                call(loopCall)
                opMode.update()
            }

            call(stopCall)
        }
    }

    private fun call(call: (DslOpMode<R>.() -> Unit)?) {
        call?.let { itCall ->
            opMode?.let { itOpMode ->
                itCall(itOpMode)
            }
        }
    }

    private fun checkCallDeclared(callback: (DslOpMode<R>.() -> Unit)?, name: String) {
        callback?.let {
            throw IllegalStateException("Callback for $name has been already declared once")
        }
    }

}
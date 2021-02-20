package com.github.serivesmejia.ftcdsl.opmode

import com.github.serivesmejia.ftcdsl.builder.dsl.opmode.DslOpModeBuilder
import com.github.serivesmejia.ftcdsl.builder.dsl.telemetry.DslTelemetryBuilder
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.builder.misc.TimerBuilder
import com.github.serivesmejia.ftcdsl.util.Timer
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.util.ElapsedTime

/**
 * DslOpMode, the main part of the FtcDsl library.
 *
 *
 *
 * @param R the type of the RobotBuilder, or EmptyRobot if none
 * @param buildCallback the callback, executed in the context of a DslOpModeBuilder, which will build this DslOpMode
 */
open class DslOpMode<R: RobotBuilder>(buildCallback: DslOpModeBuilder<R>.() -> Unit) : LinearOpMode() {

    private val timerBuilder = TimerBuilder()
    private var builder = DslOpModeBuilder<R>(timerBuilder)

    lateinit var robot: R

    private var breakWhileActive = false

    init {
        buildCallback(builder)
    }

    override fun runOpMode() = builder.execute(this)

    /**
     * Updates registered gamepad(s) and timer(s).
     *
     * This function is implicitly called in iterative style<br/>
     * on the loop block (after the OpMode has started), but<br/>
     * needs to be manually called when using linear style.
     */
    fun update() {
        builder.executeGamepads()

        if(Thread.currentThread().isInterrupted) {
            timerBuilder.cancelAll()
        }

        timerBuilder.update()
    }

    /**
     * Build a single or multiple telemetry messages
     * and implicitly calls Telemetry#update after building<br/>
     *
     * Allows for the following syntax:
     * ```
     * telemetry {
     *  data("Say", "Hello Driver")
     *  data("Motor Power", "%.2f", motor.power)
     *  line("This is a line (without caption)")
     * }
     * ```
     * @see DslTelemetryBuilder
     */
    fun telemetry(callback: DslTelemetryBuilder.() -> Unit) {
        val builder = DslTelemetryBuilder(telemetry)
        callback(builder)

        builder.execute()
    }

    /**
     * Executes a code block in a loop
     * until the OpMode stops
     */
    fun whileActive(callback: () -> Unit) {
        while(!Thread.currentThread().isInterrupted && !breakWhileActive) {
            callback()
        }

        breakWhileActive = false
    }

    /**
     * Breaks out of the whileActive loop
     */
    fun breakWhile() { breakWhileActive = true }

    /**
     * Executes a code block in a loop during the
     * specified millis, or until the OpMode stops.
     */
    fun whileTime(millis: Double, callback: (elapsedMillis: Double) -> Unit) {
        val timer = ElapsedTime()

        while(timer.milliseconds() < millis && !Thread.currentThread().isInterrupted) {
            callback(timer.milliseconds())
        }
    }

    /**
     * Executes a code block after the specified time has passed.<br/>
     *
     * update() has to be repetitively called in linear style for
     * the timers to work, while in iterative it's implicitly called
     * on loop block (after the OpMode has started)
     *
     * @param millis the time in milliseconds in which the code block will be executed
     * @param call the block to be called when the time runs up.
     * @returns a Timer object handling this timeout
     */
    fun timeout(millis: Double, call: (Timer) -> Unit) = timerBuilder.setTimeout(millis, call)

    /**
     * Executes a code block after the specified time has passed,
     * repeating the execution continuously.<br/>
     *
     * update() has to be repetitively called in linear style for
     * the timers to work, while in iterative it's implicitly called
     * on loop block (after the OpMode has started)
     *
     * @param millis the time in milliseconds in which the code block will be executed every time
     * @param call the block to be called when the time runs up.
     * @returns a Timer object handling this timeout
     */
    fun interval(millis: Double, call: (Timer) -> Unit) = timerBuilder.setInterval(millis, call)

    /**
     * A short-hand for getting a HardwareDevice from the HardwareMap
     * @oaram name the device name (entered in the robot config)
     * @param T the Type of the required HardwareDevice.
     * @return the HardwareDevice if it exists.
     */
    inline fun <reified T : HardwareDevice> device(name: String): T = hardwareMap.get(T::class.java, name)!!

}
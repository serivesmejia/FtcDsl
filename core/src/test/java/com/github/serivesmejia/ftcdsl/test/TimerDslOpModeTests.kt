package com.github.serivesmejia.ftcdsl.test

import com.github.serivesmejia.ftcdsl.builder.hardware.EmptyRobot
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.qualcomm.robotcore.util.ElapsedTime

import org.junit.Assert.*
import org.junit.Test

class TimerDslOpModeTests {

    class CountRobot : RobotBuilder() {
        var count = 0

        override fun build() {}
    }

    class SingleTimeoutOpModeTest : DslOpMode<EmptyRobot>({
        timeout(1000.0) {
            println("timeout in TimeoutOpModeTest!")
        }
    })

    class SingleIntervalOpModeTest : DslOpMode<CountRobot>({
        robot = CountRobot()

        interval(500.0) {
            println("interval in IntervalOpModeTest!")

            withOpMode {
                robot.count++
                if (robot.count >= 4) it.cancel()
            }
        }
    })

    @Test
    fun TestSingleTimeoutDslOpMode() {
        val opMode = mockDslOpMode(SingleTimeoutOpModeTest())
        opMode.start()

        val timer = ElapsedTime()
        opMode.runOpMode()

        assertEquals(1000.0, timer.milliseconds(), 150.0)
    }

    @Test
    fun TestSingleIntervalDslOpMode() {
        val opMode = mockDslOpMode(SingleIntervalOpModeTest())
        opMode.start()

        val timer = ElapsedTime()
        opMode.runOpMode()

        assertEquals(2000.0, timer.milliseconds(), 150.0)
        assertEquals(4, opMode.robot.count)
    }

    class ConcurrentTimeoutIntervalOpModeTest : DslOpMode<CountRobot>({

        robot = CountRobot()

        var timeouted = false

        val i = interval(250.0) {
            println("250ms interval in ConcurrentTimeoutIntervalOpModeTest!")

            withOpMode { robot.count++ }
        }

        interval(500.0) {
            println("500ms interval in ConcurrentTimeoutIntervalOpModeTest!")

            withOpMode {
                if(robot.count == 4) {
                    assertTrue(timeouted)
                }

                if (robot.count >= 6) {
                    it.cancel()
                    i.cancel()
                }
            }
        }

        timeout(1000.0) {
            timeouted = true
            println("timeout in ConcurrentTimeoutIntervalOpModeTest!")
        }

    })

    @Test
    fun TestConcurrentTimeoutIntervalDslOpMode() {
        val opMode = mockDslOpMode(ConcurrentTimeoutIntervalOpModeTest())
        opMode.start()

        val timer = ElapsedTime()
        opMode.runOpMode()

        assertEquals(2000.0, timer.milliseconds(), 150.0)
        assertEquals(7, opMode.robot.count)
    }

}
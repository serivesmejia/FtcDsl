package com.github.serivesmejia.ftcdsl.test

import com.github.serivesmejia.ftcdsl.builder.hardware.EmptyRobot
import com.github.serivesmejia.ftcdsl.builder.hardware.RobotBuilder
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.ElapsedTime
import org.junit.*
import org.junit.Assert.*

class BasicDslOpModeTests {

    class BasicTestRobot : RobotBuilder() {
        var a = 0

        override fun build() {
            a = 5
        }

        fun withOpModeTest() = withOpMode<Int> {
            a += 5
            sleep(1000)

            return a
        }
    }

    class LinearBasicTestOpMode : DslOpMode<BasicTestRobot>({
        robot = BasicTestRobot()

        var millis = 0.0

        linear {
            whileTime(2000.0) {
                millis = it
            }

            assertEquals(millis, 2000.0, 100.0)
            assertEquals(robot.a, 5)
        }
    })

    class IterativeBasicTestOpMode : DslOpMode<BasicTestRobot>({
        robot = BasicTestRobot()

        val timer = ElapsedTime()

        iterative {
            init {
                timer.reset()
                assertEquals(robot.a, 5)

                whileTime(1000.0) {
                    start()
                }
            }

            start {
                assertEquals(timer.milliseconds(), 1000.0, 100.0)
                timer.reset()
            }

            loop {
                sleep(2000)
                Thread.currentThread().interrupt()
            }
        }
    })


    @Test
    fun TestLinearBasicDslOpMode() {
        val opMode = mockDslOpMode(LinearBasicTestOpMode())

        val timer = ElapsedTime()
        opMode.runOpMode()

        assertEquals(2000.0, timer.milliseconds(), 100.0)
    }

    @Test
    fun TestIterativeBasicDslOpMode() {
        val opMode = mockDslOpMode(IterativeBasicTestOpMode())

        val timer = ElapsedTime()
        opMode.runOpMode()

        assertEquals(3000.0, timer.milliseconds(), 100.0)
    }

    class IllegalTwoStylesBasicTestOpMode : DslOpMode<EmptyRobot>({
        //illegal! we can't have both iterative and linear styles in one opmode
        linear {}
        iterative {}
    })

    class IllegalIterativeBasicTestOpMode : DslOpMode<EmptyRobot>({
        iterative {
            init {}
            //illegal! we can't create two callbacks of the same thing in iterative
            loop {}
            loop {}
        }
    })

    class IllegalGamepadsBasicTestOpMode : DslOpMode<EmptyRobot>({
        gamepad1 {}
        gamepad2 {}
        gamepad1 {}
    })

    class IllegalNoRobotBasicTestOpMode : DslOpMode<BasicTestRobot>({
        //illegal! we shouldn't have a robot type parameter that's not EmptyRobot and
        //completely forget about instantiating and setting it in the main builder
        linear {
            robot.a = 0 //this will throw an exception...
            sleep(1000)
            //:thonk:
        }
    })

    @Test
    fun TestIllegalTwoStylesBasicDslOpMode() {
        val invalid = try {
            IllegalTwoStylesBasicTestOpMode()
            false
        } catch(e: IllegalStateException) {
            println(e.message)
            true
        }

        assertTrue(invalid)
    }

    @Test
    fun TestIllegalIterativeBasicDslOpMode() {
        val invalid = try {
            IllegalIterativeBasicTestOpMode()
            false
        } catch(e: IllegalStateException) {
            println(e.message)
            true
        }

        assertTrue(invalid)
    }

    @Test
    fun TestIllegalNoRobotBasicDslOpMode() {
        val invalid = try {
            val opMode = mockDslOpMode(IllegalNoRobotBasicTestOpMode())

            opMode.runOpMode()
            false
        } catch(e: ClassCastException) {
            println(e.message)
            true
        }

        assertTrue(invalid)
    }

    @Test
    fun TestIllegalGamepadBasicDslOpMode() {
        val invalid = try {
            val opMode = mockDslOpMode(IllegalGamepadsBasicTestOpMode())

            opMode.runOpMode()
            false
        } catch(e: IllegalStateException) {
            println(e.message)
            true
        }

        assertTrue(invalid)
    }

    class WithOpModeBasicTestOpMode : DslOpMode<EmptyRobot>({
        val a = 1

        fun withOpModeTest() = withOpMode<Int> {
            sleep(1000)
            return a + 5
        }

        linear {
            assertEquals(a + 5, withOpModeTest())
        }
    })

    class RobotWithOpModeBasicTestOpMode : DslOpMode<BasicTestRobot>({
        robot = BasicTestRobot()

        linear {
            robot.withOpModeTest()
            assertEquals(robot.a, 10)
        }
    })

    @Test
    fun TestWithOpModeBasicDslOpMode() {
        val opMode = mockDslOpMode(WithOpModeBasicTestOpMode())

        val timer = ElapsedTime()
        opMode.runOpMode()

        assertEquals(1000.0, timer.milliseconds(), 100.0)
    }

    @Test
    fun TestRobotWithOpModeBasicDslOpMode() {
        val opMode = mockDslOpMode(RobotWithOpModeBasicTestOpMode())

        val timer = ElapsedTime()
        opMode.runOpMode()

        assertEquals(1000.0, timer.milliseconds(), 100.0)
    }

}
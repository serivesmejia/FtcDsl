package com.github.serivesmejia.ftcdsl.test

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

    @Test
    fun TestLinearBasicDslOpMode() {
        val opMode = LinearBasicTestOpMode()
        opMode.hardwareMap = HardwareMap(null)

        val timer = ElapsedTime()
        opMode.runOpMode()

        assertEquals(timer.milliseconds(), 2000.0, 100.0)
    }

}
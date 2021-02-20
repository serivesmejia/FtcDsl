package com.github.serivesmejia.ftcdsl.test

import com.github.serivesmejia.ftcdsl.builder.hardware.EmptyRobot
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class TimerDslOpModeTests {

    class TimeoutOpModeTest : DslOpMode<EmptyRobot>({
        timeout(1000.0) {

        }
    })

}
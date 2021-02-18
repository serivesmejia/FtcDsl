package com.github.serivesmejia.ftcdsl.opmode

class DslOpMode(c_init: DslLinearOpMode.() -> Unit,
                c_init_loop: DslLinearOpMode.() -> Unit = {},
                c_start: DslLinearOpMode.() -> Unit = {},
                c_loop: DslLinearOpMode.() -> Unit,
                c_stop: DslLinearOpMode.() -> Unit = {}) : DslLinearOpMode({
    c_init()

    while(!isStarted && !isStopRequested) {
        c_init_loop();
    }

    c_start()

    while(opModeIsActive()) {
        c_loop()
    }

    c_stop()
})
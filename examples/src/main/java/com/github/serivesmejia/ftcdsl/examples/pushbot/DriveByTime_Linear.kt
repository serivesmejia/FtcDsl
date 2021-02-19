package com.github.serivesmejia.ftcdsl.examples.pushbot

import com.github.serivesmejia.ftcdsl.extension.hardware.ServoExt.position
import com.github.serivesmejia.ftcdsl.opmode.DslOpMode

class DriveByTime_Linear : DslOpMode<PushBotRobot>({
    robot = PushBotRobot()

    val FORWARD_SPEED = 0.6
    val TURN_SPEED = 0.5

    linear {
        telemetry.addData("Status", "Ready to run")
        telemetry.update()

        // Wait for the game to start (driver presses PLAY)
        waitForStart()

        // Step 1: Drive forward for 3 seconds
        robot.driveTime(3.0, FORWARD_SPEED, FORWARD_SPEED)

        // Step 2: Spin right for 1.3 seconds
        robot.driveTime(1.3, TURN_SPEED, -TURN_SPEED)

        // Step 3: Drive Backwards for 1 second
        robot.driveTime(1.0, -FORWARD_SPEED, -FORWARD_SPEED)

        // Step 4: Stop and close the claw
        robot.driveTime(0.0, 0.0, 0.0)

        robot.leftClaw position 1.0
        robot.rightClaw position 0.0

        telemetry.addData("Path", "Complete")
        telemetry.update()

        sleep(1000)
    }
})
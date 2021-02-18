package com.github.serivesmejia.ftcdsl.extension.hardware

import com.github.serivesmejia.ftcdsl.builder.dsl.gamepad.Button
import com.github.serivesmejia.ftcdsl.builder.dsl.gamepad.Button.*
import com.qualcomm.robotcore.hardware.Gamepad

object GamepadExt {

    /**
     * Gets if a button is pressed mapping FtcDsl's Button enum to the SDK buttons
     * @param button the button to check for
     * @return Boolean indicating whether the button is pressed or not
     */
    fun Gamepad.get(button: Button): Boolean {
        return when(button) {
            A -> a
            B -> b
            X -> x
            Y -> y

            DPAD_UP -> dpad_up
            DPAD_DOWN -> dpad_down
            DPAD_LEFT -> dpad_left
            DPAD_RIGHT -> dpad_right

            LEFT_BUMPER -> left_bumper
            RIGHT_BUMPER -> right_bumper

            LEFT_TRIGGER -> left_trigger >= 0.5
            RIGHT_TRIGGER -> right_trigger >= 0.5
            LEFT_JOYSTICK -> left_stick_button
            RIGHT_JOYSTICK -> right_stick_button

            START -> start
            BACK -> back
        }
    }

}
package com.github.serivesmejia.ftcdsl.builder.misc

import com.github.serivesmejia.ftcdsl.util.Timer

class TimerBuilder {

    private val timers = ArrayList<Timer>()

    fun update() {
        for(timer in timers.toTypedArray()) {
            timer.update()
            if(timer.finished) timers.remove(timer)
        }
    }

    fun setTimeout(millis: Double, callback: (Timer) -> Unit): Timer {
        val timer = Timer(millis)
        timer.onTimeout(callback)

        return timer
    }

    fun setInterval(millis: Double, callback: (Timer) -> Unit): Timer {
        val timer = Timer(millis, true)
        timer.onTimeout(callback)

        return timer
    }

    fun cancelAll() = timers.forEach { it.cancel() }

}
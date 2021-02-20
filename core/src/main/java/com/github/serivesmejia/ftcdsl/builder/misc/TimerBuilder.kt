package com.github.serivesmejia.ftcdsl.builder.misc

import com.github.serivesmejia.ftcdsl.util.Timer

class TimerBuilder {

    private val timers = ArrayList<Timer>()

    val busy: Boolean
        get() = timers.size > 0

    fun update() {
        for(timer in timers.toTypedArray()) {
            timer.update()
            if(timer.finished) timers.remove(timer)
        }
    }

    fun setTimeout(millis: Double, callback: (Timer) -> Unit) = addTimer(Timer(millis), callback)

    fun setInterval(millis: Double, callback: (Timer) -> Unit) = addTimer(Timer(millis, true), callback)

    fun addTimer(timer: Timer, callback: ((Timer) -> Unit)? = null): Timer {
        timers.add(timer)
        callback?.let { timer.onTimeout(it) }

        return timer
    }

    fun cancelAll() = timers.forEach { it.cancel() }

}
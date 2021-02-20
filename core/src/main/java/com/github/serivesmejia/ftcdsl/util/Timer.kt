package com.github.serivesmejia.ftcdsl.util

import com.qualcomm.robotcore.util.ElapsedTime

class Timer(private val millis: Double, private val static: Boolean = false) {

    private val timeoutCallbacks = ArrayList<(Timer) -> Unit>()
    private val timer = ElapsedTime()

    var started = false
        private set

    var finished = false
        private set

    var cancelRequested = false
        private set

    /**
     * Adds a callback which will get
     * executed once the timer runs out
     */
    fun onTimeout(callback: (Timer) -> Unit) = timeoutCallbacks.add(callback)

    /**
     * Updates this timer. If cancel requested, it will simply
     * finish here. If the time is out, it will execute all the callbacks
     */
    fun update() {
        //if we haven't "started", meaning that this
        //is the first time user calls update()
        if(!started) {
            //reset ElapsedTime since we don't wanna
            //start counting before starting this timer
            timer.reset()
            started = true
        }

        //if we have been requested to cancel, finish right here
        if(cancelRequested) finished = true
        if(finished) return //if finished, don't go any further.

        //if time runs out
        if(timer.milliseconds() > millis) {
            //execute all the callbacks
            timeoutCallbacks.forEach { it(this) }

            //if we aren't a static timer, finish here
            if(!static) finished = true

            //reset timer in case we're a static timer
            timer.reset()
        }
    }

    /**
     * Awaits for this Timer to be finished.
     * update() will be constantly called
     * while awaiting.
     */
    fun await() {
        while(!finished) update()
    }

    /**
     * Request to cancel this timer
     */
    fun cancel() {
        cancelRequested = true
    }

}
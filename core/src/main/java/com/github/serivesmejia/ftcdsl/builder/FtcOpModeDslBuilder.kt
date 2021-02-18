package com.github.serivesmejia.ftcdsl.builder

class FtcOpModeDslBuilder {
    fun linearOpMode(callback: DslLinearOpModeBuilder.() -> Unit) {
        val builder = DslLinearOpModeBuilder()
        callback(builder)
    }
}
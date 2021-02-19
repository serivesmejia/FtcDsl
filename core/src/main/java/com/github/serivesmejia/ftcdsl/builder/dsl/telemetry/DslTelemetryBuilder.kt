package com.github.serivesmejia.ftcdsl.builder.dsl.telemetry

import com.github.serivesmejia.ftcdsl.builder.dsl.DslBuilder
import org.firstinspires.ftc.robotcore.external.Telemetry

class DslTelemetryBuilder(private val telemetry: Telemetry) : DslBuilder {

    fun data(caption: String, value: Any) = telemetry.addData(caption, value)

    fun data(caption: String, format: String, vararg values: Any) = telemetry.addData(caption, format, values)

    fun line(value: String) = telemetry.addLine(value)

    override fun execute() {
        telemetry.update()
    }

}
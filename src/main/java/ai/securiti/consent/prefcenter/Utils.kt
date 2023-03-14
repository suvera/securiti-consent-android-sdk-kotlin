package ai.securiti.consent.prefcenter

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class Utils {
    companion object {
        var mapper = jacksonObjectMapper()
    }

    init {
        mapper.configOverride(String.Companion::class.java).setterInfo = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY)
        mapper.configOverride(Int.Companion::class.java).setterInfo = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY)
    }
}
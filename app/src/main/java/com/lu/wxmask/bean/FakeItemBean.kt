package com.lu.wxmask.bean

import androidx.annotation.Keep
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.lu.magic.util.GsonUtil
import com.lu.wxmask.Constrant
import com.lu.wxmask.util.ext.toJsonObject
import org.json.JSONObject

@Keep
class FakeItemBean(
    var fakeId: String,
    var fakeName: String = ""
) {

    companion object {
        fun fromJson(jsonText: String): FakeItemBean {
            val json = try {
                JSONObject(jsonText)
            } catch (e: Exception) {
                JSONObject()
            }
            return FakeItemBean(
                fakeId = json.optString("fakeId", ""),
                fakeName = json.optString("fakeName", ""),
            )
        }
    }
}


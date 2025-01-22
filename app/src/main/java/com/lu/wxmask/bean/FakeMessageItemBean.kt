package com.lu.wxmask.bean

import androidx.annotation.Keep
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.lu.magic.util.GsonUtil
import com.lu.wxmask.Constrant
import com.lu.wxmask.util.ext.toJsonObject
import org.json.JSONObject
import java.util.Date

@Keep
class FakeMessageItemBean(
    val fakeId: String,
    var fakeType: Int?,
    var msgId: String,
    var msgDate: Long?,
    var fakeText: String?,
    var msgText: String?,
    var isSend: Boolean =false,
    var isOpen:Boolean=true
) {
    constructor(fakeId: String):this(fakeId,Constrant.WX_FAKE_TYPE_ADD,(System.currentTimeMillis()/1000).toString(),System.currentTimeMillis(),"","",false,true)
    companion object {
        fun fromJson(jsonText: String): FakeMessageItemBean {
            val json = try {
                JSONObject(jsonText)
            } catch (e: Exception) {
                JSONObject()
            }
            return FakeMessageItemBean(
                msgId = json.optString("msgId", ""),
                fakeId = json.optString("fakeId", ""),
                msgDate = json.optLong("msgDate", System.currentTimeMillis()),
                fakeType = json.optInt("fakeType", Constrant.WX_FAKE_TYPE_ADD),
                fakeText = json.optString("fakeText", ""),
                msgText = json.optString("msgText", ""),
                isSend = json.optBoolean("isSend", false),
                isOpen = json.optBoolean("isSend", true),
                )
        }
    }
}


package com.lu.wxmask.bean

import androidx.annotation.Keep
import org.json.JSONObject

@Keep
class OptionData private constructor(
    var hideMainSearch: Boolean,
    var enableMapConversation: Boolean,
    var hideSingleSearch: Boolean,
    var hideMainSearchStrong: Boolean,
    var viewWxDbPw: Boolean,
    var enableCopyTextInfo: Boolean,
    var enableFakeMsg: Boolean
) {

    companion object {
        fun fromJson(jsonText: String): OptionData {
            val json = try {
                JSONObject(jsonText)
            } catch (e: Exception) {
                JSONObject()
            }
            return OptionData(
                hideMainSearch = json.optBoolean("hideMainSearch", true),
                enableMapConversation = json.optBoolean("enableMapConversation", false),
                hideSingleSearch = json.optBoolean("hideSingleSearch", true),
                hideMainSearchStrong = json.optBoolean("hideMainSearchStrong", false),
                viewWxDbPw = json.optBoolean("viewWxDbPw", false),
                enableCopyTextInfo = json.optBoolean("enableCopyTextInfo",false),
                enableFakeMsg = json.optBoolean("enableCopyTextInfo",false)
            )
        }
        fun toJson(data: OptionData): String {
            return JSONObject().apply {
                put("hideMainSearch", data.hideMainSearch)
                put("enableMapConversation", data.enableMapConversation)
                put("hideSingleSearch", data.hideSingleSearch)
                put("hideMainSearchStrong", data.hideMainSearchStrong)
                put("viewWxDbPw", data.viewWxDbPw)
                put("enableCopyTextInfo",data.enableCopyTextInfo)
            }.toString()
        }

    }
}


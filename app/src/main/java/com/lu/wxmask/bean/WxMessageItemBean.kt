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
class WxMessageItemBean(
    val userId: String,
    val userName: String?,
    val msgId: String,
    val msgDate: Long,
    val msgText: String,
    val isSend: Boolean ,
) {

}


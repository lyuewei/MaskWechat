package com.lu.wxmask.util

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.text.TextUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lu.magic.util.GsonUtil
import com.lu.magic.util.kxt.toElseString
import com.lu.magic.util.log.LogUtil
import com.lu.wxmask.bean.BaseTemporary
import com.lu.wxmask.bean.FakeItemBean
import com.lu.wxmask.bean.FakeMessageItemBean
import com.lu.wxmask.bean.MaskItemBean
import com.lu.wxmask.bean.OptionData
import com.lu.wxmask.util.ext.toJson
import org.json.JSONArray
import org.json.JSONObject

class ConfigUtil {
    companion object {
        val KEY_MASK_LIST = "maskList"
        val KEY_TEMPORARY = "temporary"
        val KEY_OPTIONS = "options"
        val KEY_FAKE_LIST = "fakeList"
        val KEY_FAKE_MSG_LIST = "fakeMsgList_"

        val sp by lazy {
            //偏好文件变更监听
            LocalKVUtil.getTable("mask_wechat_config").apply {
                registerOnSharedPreferenceChangeListener { _share, _key ->
                    LogUtil.d("onSharedPreferenceChanged", _key)
                    notifyConfigSetObserverChanged()
                }
            }
        }

        @JvmStatic
        private val dataSetObserverList = arrayListOf<ConfigSetObserver>()

        fun getMaskList(): ArrayList<MaskItemBean> {
            return try {
                getMaskListInternal()
            } catch (e: Throwable) {
                LogUtil.e(e)
                arrayListOf()
            }
        }

        private fun getMaskListInternal(): ArrayList<MaskItemBean> {
            val result = ArrayList<MaskItemBean>()
            try {
                val jsonText = sp.getString(KEY_MASK_LIST, "[]")
                val jsonArr = JSONArray(jsonText)
                for (i in 0 until jsonArr.length()) {
                    val json = jsonArr.optString(i)
                    if (json.isNullOrBlank()) {
                        continue
                    }
                    var bean = MaskItemBean.fromJson(json)
                    if (TextUtils.isEmpty(bean.maskId)) {
                        continue
                    }
                    result.add(bean)
                }
            } catch (e: Exception) {
                LogUtil.w("getMaskList fail", e)
            }
            return result
        }

        fun setMaskList(data: List<MaskItemBean>) {
            GsonUtil.toJson(data).let {
                sp.edit().putString(KEY_MASK_LIST, it).apply()
            }
//            notifyConfigSetObserverChanged()
        }

        fun addMaskList(item: MaskItemBean) {
            val maskList = try {
                getMaskListInternal()
            } catch (e: Exception) {
                LogUtil.e(item.toJson(), e)
                null
            } ?: return
            maskList.add(item)
            GsonUtil.toJson(maskList).let {
                sp.edit().putString(KEY_MASK_LIST, it).apply()
            }
//            notifyConfigSetObserverChanged()
        }


        /**伪造用户列表*/
        fun getFakeList(): ArrayList<FakeItemBean> {
            return try {
                getFakeListInternal()
            } catch (e: Throwable) {
                LogUtil.e(e)
                arrayListOf()
            }
        }

        private fun getFakeListInternal(): ArrayList<FakeItemBean> {
            val result = ArrayList<FakeItemBean>()
            try {
                val jsonText = sp.getString(KEY_FAKE_LIST, "[]")
                val jsonArr = JSONArray(jsonText)
                for (i in 0 until jsonArr.length()) {
                    val json = jsonArr.optString(i)
                    if (json.isNullOrBlank()) {
                        continue
                    }
                    var bean = FakeItemBean.fromJson(json)
                    if (TextUtils.isEmpty(bean.fakeId)) {
                        continue
                    }
                    result.add(bean)
                }
            } catch (e: Exception) {
                LogUtil.w("getFakeList fail", e)
            }
            return result
        }

        /**
         * 设置伪造用户
         */
        fun setFakeList(data: List<FakeItemBean>) {
            GsonUtil.toJson(data).let {
                sp.edit().putString(KEY_FAKE_LIST, it).apply()
            }
//            notifyConfigSetObserverChanged()
        }

        /**
         * 添加伪造用户
         */
        fun addFakeList(item: FakeItemBean) {
            val fakeList = try {
                getFakeListInternal()
            } catch (e: Exception) {
                LogUtil.e(item.toJson(), e)
                null
            } ?: return
            fakeList.add(item)
            GsonUtil.toJson(fakeList).let {
                sp.edit().putString(KEY_FAKE_LIST, it).apply()
            }
//            notifyConfigSetObserverChanged()
        }


        /**伪造消息列表*/
        fun getFakeMsgList(wxid:String): ArrayList<FakeMessageItemBean> {
            return try {
                getFakeMsgListInternal(wxid)
            } catch (e: Throwable) {
                LogUtil.e(e)
                arrayListOf()
            }
        }

        private fun getFakeMsgListInternal(wxid:String): ArrayList<FakeMessageItemBean> {
            val result = ArrayList<FakeMessageItemBean>()
            try {
                val jsonText = sp.getString(KEY_FAKE_MSG_LIST+wxid, "[]")
                val jsonArr = JSONArray(jsonText)
                for (i in 0 until jsonArr.length()) {
                    val json = jsonArr.optString(i)
                    if (json.isNullOrBlank()) {
                        continue
                    }
                    var bean = FakeMessageItemBean.fromJson(json)
                    if (TextUtils.isEmpty(bean.fakeId)) {
                        continue
                    }
                    result.add(bean)
                }
            } catch (e: Exception) {
                LogUtil.w("getFakeMsgList fail", e)
            }
            return result
        }

        /**
         * 设置伪造消息
         */
        fun setFakeMsgList(wxid:String,data: List<FakeMessageItemBean>) {
            GsonUtil.toJson(data).let {
                sp.edit().putString(KEY_FAKE_MSG_LIST+wxid, it).apply()
            }
//            notifyConfigSetObserverChanged()
        }

        /**
         * 添加伪造消息
         */
        fun addFakeMsgList(wxid:String,item: FakeMessageItemBean) {
            val msgList = try {
                getFakeMsgListInternal(wxid)
            } catch (e: Exception) {
                LogUtil.e(item.toJson(), e)
                null
            } ?: return
            msgList.add(item)
            GsonUtil.toJson(msgList).let {
                sp.edit().putString(KEY_FAKE_MSG_LIST+wxid, it).apply()
            }
//            notifyConfigSetObserverChanged()
        }





        fun getTemporaryJson(): JsonObject? {
            val text = sp.getString(KEY_TEMPORARY, null) ?: return null
            return try {
                GsonUtil.fromJson(text, JsonObject::class.java)
            } catch (e: Exception) {
                null
            }
        }

        fun <T : BaseTemporary> setTemporary(data: T) {
            try {
                sp.edit().putString(KEY_TEMPORARY, data.toJson()).apply()
//                notifyConfigSetObserverChanged()
            } catch (e: Exception) {
                LogUtil.w("save temporary fail", e)
            }
        }

        fun getOptionData(): OptionData {
            return OptionData.fromJson(sp.getString(KEY_OPTIONS, "{}").toElseString("{}"))
        }

        fun setOptionData(data: OptionData) {
            try {
                sp.edit().putString(KEY_OPTIONS, OptionData.toJson(data)).apply()
//                notifyConfigSetObserverChanged()
            } catch (e: Exception) {
                LogUtil.w("setOptionJson fail", e)
            }
        }

        fun clearData() {
            try {
                val result = sp.edit().clear().commit()
                if (!result) {
                    LogUtil.w("clear sp data fail$result")
                }
//                notifyConfigSetObserverChanged()
            } catch (e: Exception) {
                LogUtil.w("clear sp data fail", e)
            }
        }

        fun registerConfigSetObserver(observer: ConfigSetObserver) {
            dataSetObserverList.add(observer)
        }

        fun unregisterConfigSetObserver(observer: ConfigSetObserver) {
            dataSetObserverList.remove(observer)
        }

        fun hasRegisterConfigSetObserver(observer: ConfigSetObserver): Boolean {
            return dataSetObserverList.contains(observer)
        }

        private fun notifyConfigSetObserverChanged() {
            dataSetObserverList.forEach {
                it.onConfigChange()
            }
        }
    }

    fun interface ConfigSetObserver {
        fun onConfigChange()
    }
}

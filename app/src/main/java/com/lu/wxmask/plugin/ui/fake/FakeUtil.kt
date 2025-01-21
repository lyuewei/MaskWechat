package com.lu.wxmask.plugin.ui.fake

import com.lu.wxmask.bean.FakeItemBean
import com.lu.wxmask.bean.WxMessageItemBean


class FakeUtil {

    companion object {
        @JvmField
         var wxMsgList :List<WxMessageItemBean> = TODO()

        @JvmStatic
        fun checkExitFakeId(lst: List<FakeItemBean>, fakeId: String): Boolean {
            return lst.indexOfFirst { it.fakeId == fakeId } > -1
        }

        @JvmStatic
        fun findIndex(lst: List<FakeItemBean>, fakeId: String): Int {
            return lst.indexOfFirst { fakeId == it.fakeId }
        }

        @JvmStatic
        fun copyMessage(){
                // TODO 复制
        }

        @JvmStatic
        fun getWxMsgList():List<WxMessageItemBean>{
            return wxMsgList
        }

        @JvmStatic
        fun  cleanWxMsgList(){
            // TODO 清空
        }
    }


}
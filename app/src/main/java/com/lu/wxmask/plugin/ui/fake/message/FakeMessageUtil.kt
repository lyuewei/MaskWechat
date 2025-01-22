package com.lu.wxmask.plugin.ui.fake.message


import com.lu.wxmask.bean.FakeMessageItemBean


class FakeMessageUtil {

    companion object {
        @JvmStatic
        fun checkExitMsgId(lst: List<FakeMessageItemBean>, msgId: String): Boolean {
            return lst.indexOfFirst { it.msgId == msgId } > -1
        }

        @JvmStatic
        fun findIndex(lst: List<FakeMessageItemBean>, msgId: String): Int {
            return lst.indexOfFirst { msgId == it.msgId }
        }
    }


}
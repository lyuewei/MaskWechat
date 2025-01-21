package com.lu.wxmask.plugin.ui.fake.message


import com.lu.wxmask.bean.FakeMessageItemBean


class FakeMessageUtil {

    companion object {
        @JvmStatic
        fun checkExitFakeId(lst: List<FakeMessageItemBean>, fakeId: String): Boolean {
            return lst.indexOfFirst { it.msgId == fakeId } > -1
        }

        @JvmStatic
        fun findIndex(lst: List<FakeMessageItemBean>, fakeId: String): Int {
            return lst.indexOfFirst { fakeId == it.msgId }
        }
    }


}
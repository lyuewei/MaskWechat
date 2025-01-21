package com.lu.wxmask.plugin.ui.fake

import com.lu.wxmask.bean.FakeItemBean


class FakeUtil {

    companion object {
        @JvmStatic
        fun checkExitFakeId(lst: List<FakeItemBean>, fakeId: String): Boolean {
            return lst.indexOfFirst { it.fakeId == fakeId } > -1
        }

        @JvmStatic
        fun findIndex(lst: List<FakeItemBean>, fakeId: String): Int {
            return lst.indexOfFirst { fakeId == it.fakeId }
        }
    }


}
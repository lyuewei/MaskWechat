package com.lu.wxmask.plugin.part.fake

import com.lu.wxmask.bean.FakeMessageItemBean

class FakeMessageConfig_8_0_47 : IFakeMessageConfig {
     override fun msgClass(): String {
       return "com.tencent.mm.storage.d9"
     }

     override fun msgContentMethod(): String {
         return "getContent"
     }

     override fun msgAdapterClass(): String {
         return "com.tencent.mm.ui.chatting.adapter.i0"
     }
     override fun msgAdapterDataListMethod(): String {
       return "Y"
     }

     override fun msgAdapterDataChangeMethod(): String {
         return "j"
     }
    override fun addFakeMessage(fakeMessageItemBean: FakeMessageItemBean):Any? {
        return null
     }

    override  fun readMsgToFaskMsg(msg: Any): FakeMessageItemBean? {
       return null
     }
 }
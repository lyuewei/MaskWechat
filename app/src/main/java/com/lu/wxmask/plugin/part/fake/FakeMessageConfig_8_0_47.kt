package com.lu.wxmask.plugin.part.fake

import com.lu.lposed.api2.XposedHelpers2
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
    override fun foundFakeMessage( clazz: Class<*>,fakeMessageItemBean: FakeMessageItemBean):Any {
       val msgObj= XposedHelpers2.newInstance(clazz)
        //MsgId
        XposedHelpers2.callMethod<Any>(msgObj,"setMsgId",fakeMessageItemBean.msgId)
        //field_msgSvrId
        XposedHelpers2.callMethod<Any>(msgObj,"U0",fakeMessageItemBean.msgId+1000)
        //Type
        XposedHelpers2.callMethod<Any>(msgObj,"setType",1)
        //field_status
        XposedHelpers2.callMethod<Any>(msgObj,"d1",if (fakeMessageItemBean.isSend) 2 else 3)
        //field_isSend
        XposedHelpers2.callMethod<Any>(msgObj,"O0", if (fakeMessageItemBean.isSend) 1 else 0)
        //field_isShowTimer
        XposedHelpers2.callMethod<Any>(msgObj,"P0",0)
        //CreateTime
        XposedHelpers2.callMethod<Any>(msgObj,"G0",fakeMessageItemBean.msgDate)
        //field_talker
        XposedHelpers2.callMethod<Any>(msgObj,"e1",fakeMessageItemBean.fakeId)
        //Content
        XposedHelpers2.callMethod<Any>(msgObj,"F0",fakeMessageItemBean.fakeText)
        //field_imgPath
        XposedHelpers2.callMethod<Any>(msgObj,"N0","")
        //field_reserved
        XposedHelpers2.callMethod<Any>(msgObj,"Y0","")
        //field_lvbuffer
        XposedHelpers2.callMethod<Any>(msgObj,"Q0", byteArrayOf(123,0,0,0,0,0,0,0,-82,60,109,115,103,115,111,117,114,99,101,62,10,9,60,112,117,97,62,49,60,47,112,117,97,62,10,9,60,101,103,103,73,110,99,108,117,100,101,100,62,49,60,47,101,103,103,73,110,99,108,117,100,101,100,62,10,9,60,115,105,103,110,97,116,117,114,101,62,86,49,95,85,67,55,69,56,75,51,86,124,118,49,95,85,67,55,69,56,75,51,86,60,47,115,105,103,110,97,116,117,114,101,62,10,9,60,116,109,112,95,110,111,100,101,62,10,9,9,60,112,117,98,108,105,115,104,101,114,45,105,100,62,60,47,112,117,98,108,105,115,104,101,114,45,105,100,62,10,9,60,47,116,109,112,95,110,111,100,101,62,10,60,47,109,115,103,115,111,117,114,99,101,62,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,125
        ))
        //field_transContent
        XposedHelpers2.callMethod<Any>(msgObj,"o1","")
        XposedHelpers2.setObjectField(msgObj,"E","")
        //XposedHelpers2.setObjectField(msgObj,"f360882r",true)



        XposedHelpers2.callMethod<Any>(msgObj,"R0",0)

        XposedHelpers2.callMethod<Any>(msgObj,"J2"," <msgsource>\n" +
                "        <pua>1</pua>\n" +
                "        <eggIncluded>1</eggIncluded>\n" +
                "</msgsource>")
        return msgObj
     }

    override  fun readMsgToFaskMsg(msg: Any): FakeMessageItemBean? {
       return null
     }
 }
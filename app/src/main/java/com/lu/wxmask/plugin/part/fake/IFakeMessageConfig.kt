package com.lu.wxmask.plugin.part.fake

import com.lu.wxmask.bean.FakeMessageItemBean

interface IFakeMessageConfig {
    fun msgClass(): String
    fun msgContentMethod(): String
    fun msgAdapterClass(): String
    fun msgAdapterDataListMethod(): String
    fun msgAdapterDataChangeMethod(): String
    fun addFakeMessage(fakeMessageItemBean: FakeMessageItemBean):Any?
    fun readMsgToFaskMsg(msg: Any): FakeMessageItemBean?
}
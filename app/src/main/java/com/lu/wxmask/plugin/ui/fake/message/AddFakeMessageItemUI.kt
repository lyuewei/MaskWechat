package com.lu.wxmask.plugin.ui.fake.message

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.lu.magic.util.kxt.toElseEmptyString
import com.lu.wxmask.Constrant
import com.lu.wxmask.bean.FakeMessageItemBean
import com.lu.wxmask.util.ConfigUtil

class AddFakeMessageItemUI(
    private val context: Context,
    private val lst: List<FakeMessageItemBean>,
) {
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var configListener: ((DialogInterface, FakeMessageItemBean) -> Unit)? = null
    private var onFreeButtonListener: DialogInterface.OnClickListener? = null
    private var fakeId = ""
    private var msgId = ""
    private var fakeText = ""
    private var fakeType = ""
    private var msgDate = ""

    //空闲的按钮的文字
    private var freeButtonText: CharSequence? = null

    fun setFakeId(fakeId: String?): AddFakeMessageItemUI {
        this.fakeId = fakeId?:""
        return this
    }

    fun setMsgId(msgId: String?): AddFakeMessageItemUI {
        this.msgId = msgId?:""
        return this
    }
    fun setFakeText(fakeText: String?): AddFakeMessageItemUI {
        this.fakeText = fakeText?:""
        return this
    }
    fun setFakeType(fakeType: String?): AddFakeMessageItemUI {
        this.fakeType = fakeType?:""
        return this
    }
    fun setMsgDate(msgDate: String?): AddFakeMessageItemUI {
        this.msgDate = msgDate?:""
        return this
    }

    fun setConfirmListener(listener: (DialogInterface, FakeMessageItemBean) -> Unit): AddFakeMessageItemUI {
        this.configListener = listener
        return this
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener): AddFakeMessageItemUI {
        onDismissListener = listener
        return this
    }

    fun setFreeButton(text: CharSequence, listener: DialogInterface.OnClickListener?): AddFakeMessageItemUI {
        freeButtonText = text
        onFreeButtonListener = listener
        return this
    }

    fun show() {
        val ui = FakeMessageItemUIController(context, FakeMessageItemBean(fakeId))

        AlertDialog.Builder(context)
            .setTitle("添加消息")
            .setIcon(context.applicationInfo.icon)
            .setView(ui.root)
            .setNegativeButton("关闭", null)
            .setPositiveButton("确定", null)
            .setNeutralButton(freeButtonText, onFreeButtonListener)
            .setOnDismissListener(onDismissListener)
            .show()
            .also { dialog ->
                //重写确定按钮监听，不消失对话框
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val fakeId = ui.tvFakeId.text.toElseEmptyString()
                    val msgDate = ui.etMsgDate.text.toElseEmptyString()
                    val fakeText = ui.etFakeText.text.toElseEmptyString()
                    val isSend = ui.cbSend.isChecked()

                    FakeMessageItemBean(fakeId).let {
                        it.isSend=isSend
                        it.fakeText=fakeText
                        it.fakeType=Constrant.WX_FAKE_TYPE_ADD
                        it.msgId=(System.currentTimeMillis()/1000).toString()
                        it.msgDate=msgDate.toLong()
                        ConfigUtil.addFakeMsgList(fakeId,it)
                        configListener?.invoke(dialog, it)
                    }
                    dialog.dismiss()
                }
            }
    }
}


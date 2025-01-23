package com.lu.wxmask.plugin.ui.fake.message

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.lu.magic.util.ToastUtil
import com.lu.magic.util.kxt.toElseEmptyString
import com.lu.wxmask.Constrant
import com.lu.wxmask.bean.FakeMessageItemBean
import com.lu.wxmask.util.ConfigUtil
import com.lu.wxmask.util.ext.toLongElse

class EditFakeMessageItemUI(
    private val context: Context,
    private val lst: MutableList<FakeMessageItemBean>,
    private val position: Int
) {
    companion object {
        val MODE_CONFIG_UPDATE = 0
        val MODE_CONFIG_REMOVE = 1
    }


    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var onConfigChangeListener: ((DialogInterface, FakeMessageItemBean, Int) -> Unit)? = null
    private var onFreeButtonListener: DialogInterface.OnClickListener? = null

    private var freeButtonText: CharSequence? = null

    fun setOnConfigChangeListener(listener: (dialog: DialogInterface, mask: FakeMessageItemBean, mode: Int) -> Unit): EditFakeMessageItemUI {
        onConfigChangeListener = listener
        return this
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener): EditFakeMessageItemUI {
        onDismissListener = listener
        return this
    }

    fun setFreeButton(text: CharSequence, listener: DialogInterface.OnClickListener?): EditFakeMessageItemUI {
        freeButtonText = text
        onFreeButtonListener = listener
        return this
    }

    fun show() {
        val fakeMsgItemBean = lst[position]
        val ui = FakeMessageItemUIController(context, fakeMsgItemBean)

        AlertDialog.Builder(context)
            .setTitle("编辑消息（${fakeMsgItemBean.msgId}）")
            .setIcon(context.applicationInfo.icon)
            .setView(ui.root)
            .setPositiveButton("确定", null)
            .setNegativeButton("取消", null)
            .setNeutralButton(freeButtonText, onFreeButtonListener)
            .setOnDismissListener(onDismissListener)
            .show()
            .also { dialog ->
                //重写确定按钮监听，不消失对话框
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val fakeId =fakeMsgItemBean.fakeId
                    val fakeText = ui.etFakeText.text.toElseEmptyString()

                    val fakeType= ui.spinnerFakeTypeList[ui.spinnerFakeType.selectedItemPosition].first
                    fakeMsgItemBean.isOpen = ui.cbOpen.isChecked
                    if (fakeMsgItemBean.fakeType==Constrant.WX_FAKE_TYPE_ADD){
                        fakeMsgItemBean.msgDate=ui.msgDate
                        fakeMsgItemBean.fakeText=fakeText
                        fakeMsgItemBean.isSend=ui.cbSend.isChecked
                    }else if (fakeType==Constrant.WX_FAKE_TYPE_UPDATE){
                        fakeMsgItemBean.fakeText=fakeText
                    }
                    lst[position]=fakeMsgItemBean
                    ConfigUtil.setFakeMsgList(fakeId,lst)
                    ToastUtil.show("已更新！")
                    onConfigChangeListener?.invoke(dialog, fakeMsgItemBean, MODE_CONFIG_UPDATE)
                    dialog.dismiss()
                }
            }


    }
}
package com.lu.wxmask.plugin.ui.fake

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.lu.magic.util.GsonUtil
import com.lu.magic.util.kxt.toElseEmptyString
import com.lu.wxmask.bean.FakeItemBean
import com.lu.wxmask.util.ConfigUtil

class AddFakeItemUI(
    private val context: Context,
) {
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var configListener: ((DialogInterface, FakeItemBean) -> Unit)? = null
    private var onFreeButtonListener: DialogInterface.OnClickListener? = null
    private var fakeId = ""
    private var fakeName = ""


    fun setConfirmListener(listener: (DialogInterface, FakeItemBean) -> Unit): AddFakeItemUI {
        this.configListener = listener
        return this
    }

    fun show() {
        val ui = FakeItemUIController(context, FakeItemBean(fakeId, fakeName))

        AlertDialog.Builder(context)
            .setTitle("添加配置")
            .setIcon(context.applicationInfo.icon)
            .setView(ui.root)
            .setNegativeButton("关闭", null)
            .setPositiveButton("确定", null)
           // .setOnDismissListener(onDismissListener)
            .show()
            .also { dialog ->
                //重写确定按钮监听，不消失对话框
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val fakeId = ui.etFakeId.text.toElseEmptyString()

                    val fakeName = ui.etFakeName.text.toElseEmptyString()

                    FakeItemBean(fakeId, fakeName).let {

                        ConfigUtil.addFakeList(it)
                        configListener?.invoke(dialog, it)
                    }
                    dialog.dismiss()
                }
            }
    }
}


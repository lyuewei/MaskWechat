package com.lu.wxmask.plugin.ui.fake.message

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.lu.magic.util.ToastUtil
import com.lu.magic.util.kxt.toElseEmptyString
import com.lu.wxmask.bean.FakeMessageItemBean
import com.lu.wxmask.util.ConfigUtil

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
        val fakeItemBean = lst[position]
        val ui = FakeMessageItemUIController(context, fakeItemBean)

        AlertDialog.Builder(context)
            .setTitle("编辑配置")
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
                    val fakeId = ui.etFakeId.text.toElseEmptyString()
                    val fakeName = ui.etFakeName.text.toElseEmptyString()
                    //编辑需要确保已变更，且不在列表中，而新增则不存在是否变更的问题
                    if (fakeId.isNotEmpty() && fakeId != fakeItemBean.fakeId && FakeMessageUtil.checkExitFakeId(lst, fakeId)) {
                        ToastUtil.show("配置已存在！")
                        return@setOnClickListener
                    }
                    if (fakeId.isEmpty()) {
                        //删除
                        lst.removeAt(position)
                        ConfigUtil.setFakeList(lst)
                        ToastUtil.show("已删除！")
                        onConfigChangeListener?.invoke(dialog, fakeItemBean, MODE_CONFIG_REMOVE)
                    } else {
                        fakeItemBean.fakeId = fakeId
                        fakeItemBean.fakeName = fakeName
                        ConfigUtil.setFakeList(lst)
                        ToastUtil.show("已更新！")
                        onConfigChangeListener?.invoke(dialog, fakeItemBean, MODE_CONFIG_UPDATE)
                    }
                    dialog.dismiss()
                }
            }


    }
}
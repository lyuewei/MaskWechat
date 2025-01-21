package com.lu.wxmask.plugin.ui.fake.message

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams.MATCH_PARENT
import android.view.ViewGroup.MarginLayoutParams.WRAP_CONTENT
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.setPadding
import com.lu.magic.util.SizeUtil
import com.lu.wxmask.Constrant
import com.lu.wxmask.bean.FakeMessageItemBean
import com.lu.wxmask.ui.adapter.SpinnerListAdapter
import com.lu.wxmask.util.ext.dp

internal class FakeMessageItemUIController(private val context: Context, private val message: FakeMessageItemBean) {
    private val viewId: MutableMap<String, View> = mutableMapOf()

    val dp24 = SizeUtil.dp2px(context.resources, 24f).toInt()
    var root: LinearLayout = LinearLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT
        ).also {
            orientation = LinearLayout.VERTICAL
        }
        setPadding(dp24)
    }
    var tvFakeId: TextView = TextView(context).also {
        it.hint = "微信Id"
        it.setText(message.fakeId)
    }
   val spinnerFakeTypeList= arrayListOf(
        Constrant.WX_FAKE_TYPE_ADD to "添加",
        Constrant.WX_FAKE_TYPE_UPDATE to "修改",
        Constrant.WX_FAKE_TYPE_HIDE to "隐藏"
    )

    var spinnerFakeType:Spinner = Spinner(context).also {
        it.layoutParams = LinearLayout.LayoutParams(
            MATCH_PARENT, WRAP_CONTENT
        )

        it.adapter = SpinnerListAdapter(spinnerFakeTypeList)
        it.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val itemData = spinnerFakeTypeList[position]
                when (itemData.first) {
                    Constrant.WX_FAKE_TYPE_UPDATE -> {
                        tvMsgText.visibility = View.VISIBLE
                    }
                    Constrant.WX_FAKE_TYPE_HIDE,Constrant.WX_FAKE_TYPE_ADD  -> {
                        tvMsgText.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    var tvMsgId = TextView(context).also {
        it.hint = "消息ID"
        if (Constrant.WX_FAKE_TYPE_ADD == message.fakeType){
            it.setText(message.msgId)
        }else {
            it.setText((System.currentTimeMillis()/1000).toString())
        }

    }
    var etMsgDate = EditText(context).also {
        it.hint = "日期"
        //TODO 日期转换
        it.setText(message.msgDate.toString())
    }
    var etFakeText = EditText(context).also {
        it.hint = "伪造内容"
        it.setText(message.fakeText)
    }
    var tvMsgText = TextView(context).also {
        it.hint = "原内容"
        it.setText(message.msgText)
    }
    var cbSend = CheckBox(context).also {
        it.hint = "是否发送"
        it.isChecked = message.isSend
    }



    init {
        LinearLayout.LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT
        ).apply {
            topMargin = 4.dp
        }
        addView(tvFakeId,tvFakeId.hint.toString() )
        addView(spinnerFakeType,"伪造类型" )
        addView(tvMsgId,tvMsgId.hint.toString() )
        addView(etMsgDate,etMsgDate.hint.toString() )
        addView(etFakeText,etFakeText.hint.toString() )
        if (Constrant.WX_FAKE_TYPE_ADD!= message.fakeType) {
            addView(tvMsgText, tvMsgText.hint.toString())
        }
        addView(cbSend,cbSend.hint.toString() )

    }

    fun addView(view: View,title:String) {
        root.addView(FrameLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
            addView(TextView(context).also { it.text = title })
            addView(view)
        },MATCH_PARENT, WRAP_CONTENT)
    }

}

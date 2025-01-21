package com.lu.wxmask.plugin.ui.fake

import android.content.Context
import android.view.View
import android.view.ViewGroup.MarginLayoutParams.MATCH_PARENT
import android.view.ViewGroup.MarginLayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.lu.magic.util.SizeUtil
import com.lu.wxmask.bean.FakeItemBean
import com.lu.wxmask.util.ext.dp

internal class FakeItemUIController(private val context: Context, private val fake: FakeItemBean) {
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
    var etFakeId: EditText = EditText(context).also {
        it.hint = "微信Id（抓取获得）"
        it.setText(fake.fakeId)
    }
    var etFakeName = EditText(context).also {
        it.hint = "昵称（可空，仅用于显示）"
        it.setText(fake.fakeName)
    }


    init {
        LinearLayout.LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT
        ).apply {
            topMargin = 4.dp
        }
        root.addView(etFakeId, MATCH_PARENT, WRAP_CONTENT)
        root.addView(etFakeName, MATCH_PARENT, WRAP_CONTENT)
    }


}

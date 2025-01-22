package com.lu.wxmask.plugin.ui.fake.message

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewGroup.MarginLayoutParams.MATCH_PARENT
import android.view.ViewGroup.MarginLayoutParams.WRAP_CONTENT
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.setPadding
import com.lu.magic.util.SizeUtil
import com.lu.magic.util.ripple.RectangleRippleBuilder
import com.lu.magic.util.ripple.RippleApplyUtil
import com.lu.wxmask.Constrant
import com.lu.wxmask.bean.FakeMessageItemBean
import com.lu.wxmask.plugin.ui.Theme
import com.lu.wxmask.ui.adapter.SpinnerListAdapter
import com.lu.wxmask.util.ext.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
    var msgDate :Long =message.msgDate ?: 0
    var tvFakeId: TextView = TextView(context).also {
        it.hint = "微信Id"
        it.text = message.fakeId
    }
   val spinnerFakeTypeList= arrayListOf(
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
            it.text = message.msgId
        }else {
            it.text = (System.currentTimeMillis()/1000).toString()
        }
    }


    var tvMsgDate = TextView(context).also {
        it.hint = "日期"
        it.setOnClickListener { showDateTimePicker(it as TextView) }
        it.text =formatTimestamp(msgDate)
    }
    var etFakeText = EditText(context).also {
        it.hint = "伪造内容"
        it.setText(message.fakeText)
        if (Constrant.WX_FAKE_TYPE_HIDE == message.fakeType){
            it.visibility = View.GONE
        }else {
            it.visibility = View.VISIBLE
        }
    }
    var tvMsgText = TextView(context).also {
        it.hint = "原内容"
        it.text = message.msgText
        if (Constrant.WX_FAKE_TYPE_ADD == message.fakeType){
            it.visibility = View.GONE
        }else {
            it.visibility = View.VISIBLE
        }
    }
    var cbSend = CheckBox(context).also {
        //it.hint = "是否发送"
        it.isChecked = message.isSend
        if (Constrant.WX_FAKE_TYPE_ADD == message.fakeType){
            it.isEnabled=true
        }else{
            it.isEnabled=false
        }
    }

    var cbOpen = CheckBox(context).also {
        //it.hint = "是否发送"
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
        addView(tvMsgText, tvMsgText.hint.toString())
        addView(tvMsgDate,tvMsgDate.hint.toString() )
        if (Constrant.WX_FAKE_TYPE_ADD!= message.fakeType) {
            addView(spinnerFakeType, "伪造类型")
            addView(tvMsgId, tvMsgId.hint.toString())
        }
        if (Constrant.WX_FAKE_TYPE_HIDE!= message.fakeType) {
            addView(etFakeText, etFakeText.hint.toString())
        }
        if (Constrant.WX_FAKE_TYPE_ADD== message.fakeType) {
            addView(cbSend,"是否发送")
        }
        addView(cbOpen,"是否应用")


    }

    fun addView(view: View,title:String) {
        val linearLayout = LinearLayout(context).also {
            it.layoutParams = MarginLayoutParams(
                MarginLayoutParams.MATCH_PARENT,
                MarginLayoutParams.WRAP_CONTENT
            )
            it.orientation = LinearLayout.HORIZONTAL
            it.gravity = Gravity.CENTER_VERTICAL
        }
        val textView = TextView(context).also {
            it.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            it.text = title
            RippleApplyUtil.apply(it, RectangleRippleBuilder(Color.TRANSPARENT, Theme.Color.bgRippleColor))
        }

        linearLayout.addView(textView)
        linearLayout.addView(view)


        root.addView(linearLayout)
    }
    private fun showDateTimePicker(textView: TextView) {
        // 获取当前日期和时间
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // 创建 DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                // 创建 TimePickerDialog
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        // 格式化日期和时间
                        val selectedCalendar = Calendar.getInstance().apply {
                            set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
                        }
                        val formattedDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(selectedCalendar.time)
                        textView.text = formattedDateTime
                        msgDate=selectedCalendar.timeInMillis
                    },
                    hour,
                    minute,
                    true
                )
                timePickerDialog.show()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    @SuppressLint("SimpleDateFormat")
    private fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        dateFormat.timeZone = TimeZone.getDefault()
        return dateFormat.format(Date(timestamp))
    }
}

package com.lu.wxmask.plugin.ui.fake.message

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.core.view.setPadding
import com.lu.magic.util.SizeUtil
import com.lu.magic.util.ripple.RectangleRippleBuilder
import com.lu.magic.util.ripple.RippleApplyUtil
import com.lu.wxmask.Constrant
import com.lu.wxmask.adapter.AbsListAdapter
import com.lu.wxmask.adapter.CommonListAdapter
import com.lu.wxmask.bean.FakeItemBean
import com.lu.wxmask.bean.FakeMessageItemBean
import com.lu.wxmask.plugin.ui.IConfigManagerUI
import com.lu.wxmask.plugin.ui.Theme
import com.lu.wxmask.plugin.ui.view.BottomPopUI
import com.lu.wxmask.util.ConfigUtil
import com.lu.wxmask.util.ext.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


// PopWindow全屏+返回键监听弹窗，暂不需要，没有那么多配置
internal class FakeMessageManagerUI(private val context: Activity,private val fakeItem:FakeItemBean) : IConfigManagerUI {
    private lateinit var listAdapter: CommonListAdapter<FakeMessageItemBean, AbsListAdapter.ViewHolder>
    private val popwindow: BottomPopUI
    private lateinit var listView: ListView

    init {
        popwindow = BottomPopUI(onCreateView())
        popwindow.needScrollChild = listView
    }


    override fun dismiss() {
        popwindow.dismiss()
    }

    override fun show() {
        popwindow.show()
    }


    override fun onCreateView(): View {
        return LinearLayout(context).apply {
            layoutParams = MarginLayoutParams(MATCH_PARENT, 480.dp)
            setPadding(24.dp)
            orientation = LinearLayout.VERTICAL
            // 强制适配夜间模式，系统底层直接修改的颜色，不一定生效
            // isForceDarkAllowed = true
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                val originalColor = Theme.Color.bgPrimary(context)
                val fadedColor = ColorUtils.blendARGB(originalColor, Color.WHITE, 0.2f) // 0.5f 是混合比例，可以根据需要调整
                setColor(fadedColor)
                cornerRadii = floatArrayOf(16f.dp, 16f.dp, 16f.dp, 16f.dp, 0f, 0f, 0f, 0f)
            }

            addView(initTopLayout())
            addView(initFakeMessageListView())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initTopLayout(): FrameLayout {

        return FrameLayout(context).apply {

            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
            addView(TextView(context).apply {
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.CENTER
                }
                setTextColor(context.getColor(android.R.color.tab_indicator_text))
                textSize = 16f
                text = "消息管理（${fakeItem.fakeName?.let { fakeItem.fakeId }}）"
            })
            addView(TextView(context).apply {
                text = "+"
                textSize = SizeUtil.sp2px(context.resources, 8f)
                setTextColor(context.getColor(android.R.color.tab_indicator_text))
                setOnClickListener {
                    showAddFakeMessageItemDialog()
                }
                RippleApplyUtil.apply(this, RectangleRippleBuilder(Color.TRANSPARENT, Theme.Color.bgRippleColor, 4))
                val size = (textSize * 1.5).toInt()
                layoutParams = FrameLayout.LayoutParams(size, size).apply {
                    gravity = Gravity.END
                }
                this.gravity = Gravity.CENTER
            })

        }
    }

    private fun initFakeMessageListView(): ListView {
        initListAdapter()
        return ListView(context).apply {
            listView = this
            isVerticalScrollBarEnabled = false
            layoutParams = LinearLayout.LayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT).apply {
                topMargin = 16.dp
            }
            divider = null
            selector = ColorDrawable(Color.TRANSPARENT)
            adapter = listAdapter
        }

    }

    private fun initListAdapter() {
        listAdapter = object : CommonListAdapter<FakeMessageItemBean, AbsListAdapter.ViewHolder>() {
            init {
                //去重
                val dataListTemp = ConfigUtil.getFakeMsgList(fakeItem.fakeId).let {
                    val keyMap = LinkedHashMap<Long, FakeMessageItemBean>()
                    //去重
                    it.forEach { bean ->
                        keyMap[bean.msgId] = bean
                    }
                    keyMap.values.toList()
                }
                setData(dataListTemp)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

                val itemView = LinearLayout(context).also {
                    it.layoutParams = MarginLayoutParams(
                        MarginLayoutParams.MATCH_PARENT,
                        MarginLayoutParams.WRAP_CONTENT
                    )
                    it.orientation = LinearLayout.VERTICAL
                    it.setPadding(6.dp)
                    RippleApplyUtil.apply(it, RectangleRippleBuilder(Color.TRANSPARENT, Theme.Color.bgRippleColor))
               }

                val tvFakeText= TextView(context).also {
                    it.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    it.setPadding(1.dp)
                 }
                val tvMsgText= TextView(context).also {
                    it.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    it.setPadding(1.dp)
                }
                val tvDateText= TextView(context).also {
                    it.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    it.setPadding(1.dp)
                }
                val tvOtherText= TextView(context).also {
                    it.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    it.setPadding(1.dp)
                }

    itemView.addView(tvFakeText)
                itemView.addView(tvMsgText)
                itemView.addView(tvDateText)
                itemView.addView(tvOtherText)

                return object : ViewHolder(itemView) {
                    init {

                        itemView.setOnLongClickListener {
                            showDeleteFakeMessageItemDialog(layoutPosition)
                            return@setOnLongClickListener false
                        }
                        itemView.setOnClickListener {
                            showEditFakeMessageItemDialog(layoutPosition)
                        }
                    }
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(vh: ViewHolder, position: Int, parent: ViewGroup) {
                val itemView = vh.itemView as LinearLayout
                val itemModel = dataList[position]
                // TODO 消息列表VieHolder
                val tvFakeText = itemView.getChildAt(0) as TextView
                tvFakeText.text="新内容：${itemModel.fakeText}"
                val tvMsgText = itemView.getChildAt(1) as TextView
                tvMsgText.text="原内容：${itemModel.msgText}"
                if (itemModel.fakeType!=Constrant.WX_FAKE_TYPE_UPDATE){
                    tvMsgText.visibility=View.GONE
                }
                val tvDateText = itemView.getChildAt(2) as TextView
                tvDateText.text="时间：${itemModel.msgDate?.let { formatTimestamp(it) }}"
                val tvOtherText = itemView.getChildAt(3) as TextView
                tvOtherText.text="类型：${itemModel.fakeType?.let { fakeTypeStr(it) }}  接收：${itemModel.isSend}  ID：${itemModel.msgId}"
            }

        }

    }

    private fun showDeleteFakeMessageItemDialog(position: Int) {
        AlertDialog.Builder(context)
            .setTitle("是否删除？")
            .setNegativeButton("确定") { _, _ ->
                listAdapter.removeAt(position)
                ConfigUtil.setFakeMsgList(fakeItem.fakeId,listAdapter.getData())
                listAdapter.notifyDataSetChanged()
            }
            .setNeutralButton("取消") { _, _ ->

            }
            .show()
    }

    private fun showAddFakeMessageItemDialog() {
        AddFakeMessageItemUI(context,fakeItem)
            .setConfirmListener { _, messageItemBean ->
                listAdapter.addData(messageItemBean)
                listAdapter.notifyDataSetChanged()
            }.show()
    }

    private fun showEditFakeMessageItemDialog(position: Int) {
        EditFakeMessageItemUI(context, listAdapter.getData(), position)
            .setOnConfigChangeListener { _, _, mode ->
//                when (mode) {
//                    EditMaskItemUI.MODE_CONFIG_UPDATE -> listAdapter.notifyItemChanged(position)
//                    EditMaskItemUI.MODE_CONFIG_REMOVE -> listAdapter.notifyItemRemoved(position)
//                }
                listAdapter.notifyDataSetChanged()
            }
            .show()
    }
    fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        dateFormat.timeZone = TimeZone.getDefault()
        return dateFormat.format(Date(timestamp))
    }

    fun fakeTypeStr(type:Int):String{
        when(type){
            Constrant.WX_FAKE_TYPE_ADD->{
                return "添加"
            }
            Constrant.WX_FAKE_TYPE_UPDATE->{
                return "修改"
            }
            Constrant.WX_FAKE_TYPE_HIDE->{
                return "隐藏"
            }
        }
        return "未知"
    }

}

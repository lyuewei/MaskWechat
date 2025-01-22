package com.lu.wxmask.plugin.ui.fake

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
import androidx.core.view.allViews
import androidx.core.view.setPadding
import com.lu.magic.util.SizeUtil
import com.lu.magic.util.ToastUtil
import com.lu.magic.util.ripple.RectangleRippleBuilder
import com.lu.magic.util.ripple.RippleApplyUtil
import com.lu.wxmask.Constrant
import com.lu.wxmask.adapter.AbsListAdapter
import com.lu.wxmask.adapter.CommonListAdapter
import com.lu.wxmask.bean.FakeItemBean
import com.lu.wxmask.bean.FakeMessageItemBean
import com.lu.wxmask.bean.WxMessageItemBean
import com.lu.wxmask.plugin.ui.IConfigManagerUI
import com.lu.wxmask.plugin.ui.Theme
import com.lu.wxmask.plugin.ui.fake.message.FakeMessageManagerUI
import com.lu.wxmask.plugin.ui.fake.message.FakeMessageUtil
import com.lu.wxmask.plugin.ui.view.BottomPopUI
import com.lu.wxmask.util.ConfigUtil
import com.lu.wxmask.util.ext.dp




// PopWindow全屏+返回键监听弹窗，暂不需要，没有那么多配置
internal class FakeManagerUI(private val context: Activity) : IConfigManagerUI {
    private lateinit var listAdapter: CommonListAdapter<FakeItemBean, AbsListAdapter.ViewHolder>
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
                val fadedColor = ColorUtils.blendARGB(originalColor, Color.WHITE, 0.1f) // 0.5f 是混合比例，可以根据需要调整
                setColor(fadedColor)
                cornerRadii = floatArrayOf(16f.dp, 16f.dp, 16f.dp, 16f.dp, 0f, 0f, 0f, 0f)
            }

            addView(initTopLayout())
            addView(initFakeListView())
        }
    }

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
                text = "消息配置管理"
            })
            addView(TextView(context).apply {
                text = "+"
                textSize = SizeUtil.sp2px(context.resources, 8f)
                setTextColor(context.getColor(android.R.color.tab_indicator_text))
                setOnClickListener {
                 showAddFakeItemDialog()
                }
                RippleApplyUtil.apply(this, RectangleRippleBuilder(Color.TRANSPARENT, Theme.Color.bgRippleColor, 4))
                val size = (textSize * 1.5).toInt()
                layoutParams = FrameLayout.LayoutParams(size, size).apply {
                    gravity = Gravity.END
                }
                this.gravity = Gravity.CENTER
            })
            addView(TextView(context).apply {
                text = "贴"
                textSize = SizeUtil.sp2px(context.resources, 6f)
                setTextColor(context.getColor(android.R.color.tab_indicator_text))
                setOnClickListener {
                    readCopyMessage()
                }
                RippleApplyUtil.apply(this, RectangleRippleBuilder(Color.TRANSPARENT, Theme.Color.bgRippleColor, 4))
                val size = (textSize * 1.5).toInt()
                layoutParams = FrameLayout.LayoutParams(size, size).apply {
                    gravity = Gravity.START
                }
                this.gravity = Gravity.CENTER
            })
        }
    }

    private fun initFakeListView(): ListView {
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
        listAdapter = object : CommonListAdapter<FakeItemBean, AbsListAdapter.ViewHolder>() {
            init {
                //去重
                val dataListTemp = ConfigUtil.getFakeList().let {
                    val keyMap = LinkedHashMap<String, FakeItemBean>()
                    //去重
                    it.forEach { bean ->
                        keyMap[bean.fakeId] = bean
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
                    it.orientation = LinearLayout.HORIZONTAL
                    it.gravity = Gravity.CENTER_VERTICAL
                }

                val textView = TextView(context).also {
                    it.layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    it.setPadding(6.dp)
                    RippleApplyUtil.apply(it, RectangleRippleBuilder(Color.TRANSPARENT, Theme.Color.bgRippleColor))
                }

                val deleteView = TextView(context).also {
                    it.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    it.text = "删除"
                    it.setPadding(6.dp)
                    RippleApplyUtil.apply(it, RectangleRippleBuilder(Color.TRANSPARENT, Theme.Color.bgRippleColor))
                }

                itemView.addView(textView)
                itemView.addView(deleteView)

                return object : ViewHolder(itemView) {
                    init {
                        itemView.getChildAt(0).setOnLongClickListener {
                           FakeMessageManagerUI(context,dataList[layoutPosition]).show()
                            return@setOnLongClickListener true
                        }
                        itemView.getChildAt(0).setOnClickListener {
                            showEditFakeItemDialog(layoutPosition)
                        }
                        itemView.getChildAt(1).setOnClickListener {
                            showDeleteFakeItemDialog(layoutPosition)
                        }
                    }
                }
            }

            override fun onBindViewHolder(vh: ViewHolder, position: Int, parent: ViewGroup) {
                val itemView = vh.itemView as LinearLayout
                itemView.allViews
                val itemModel = dataList[position]

                val textView = itemView.getChildAt(0) as TextView
                textView.text = if (itemModel.fakeName.isEmpty()) {
                    itemModel.fakeId
                } else {
                    "${itemModel.fakeId} (${itemModel.fakeName})"
                }
            }

        }

    }

    private fun showDeleteFakeItemDialog(position: Int) {
        AlertDialog.Builder(context)
            .setTitle("是否删除？")
            .setNegativeButton("确定") { _, _ ->
                listAdapter.removeAt(position)
                ConfigUtil.setFakeList(listAdapter.getData())
                listAdapter.notifyDataSetChanged()
            }
            .setNeutralButton("取消") { _, _ ->

            }
            .show()
    }

    private fun showAddFakeItemDialog() {
        AddFakeItemUI(context, listAdapter.getData())
            .setConfirmListener { _, fakeItemBean ->
                listAdapter.addData(fakeItemBean)
                listAdapter.notifyDataSetChanged()
            }.show()
    }

    private fun showEditFakeItemDialog(position: Int) {
        EditFakeItemUI(context, listAdapter.getData(), position)
            .setOnConfigChangeListener { _, _, mode ->
//                when (mode) {
//                    EditMaskItemUI.MODE_CONFIG_UPDATE -> listAdapter.notifyItemChanged(position)
//                    EditMaskItemUI.MODE_CONFIG_REMOVE -> listAdapter.notifyItemRemoved(position)
//                }
                listAdapter.notifyDataSetChanged()
            }
            .show()
    }

    private  fun readCopyMessage(){
       val wxMsgList= FakeUtil.getWxMsgList()
        val userMap: Map<String, List<WxMessageItemBean>> = wxMsgList.groupBy { it.userId }
        for ((userId, msgList) in userMap) {
            if (msgList.isNotEmpty()){
                val fakeList=ConfigUtil.getFakeList()
                val fakeName = msgList.firstOrNull()?.userName ?: ""
                //把用户添加到配置中，如果存在就不添加了
               if( !FakeUtil.checkExitFakeId(fakeList, userId)){
                   FakeItemBean(userId, fakeName).let {
                       ConfigUtil.addFakeList(it)
                   }
               }
                //把消息添加到配置中，如果存在就不添加了
                val fakeMsgList=ConfigUtil.getFakeMsgList(userId)
                for (msg in msgList) {
                    if (!FakeMessageUtil.checkExitMsgId(fakeMsgList, msg.msgId)) {
                        FakeMessageItemBean(
                            userId,
                            Constrant.WX_FAKE_TYPE_UPDATE,
                            msg.msgId,
                            msg.msgDate,
                            "",
                            msg.msgText,
                            msg.isSend
                        ).let {
                            ConfigUtil.addFakeMsgList(userId, it)
                        }
                    }
                }

            }

        }
        listAdapter.notifyDataSetChanged()
        FakeUtil.cleanWxMsgList()
        ToastUtil.show("已配置消息")
    }


}

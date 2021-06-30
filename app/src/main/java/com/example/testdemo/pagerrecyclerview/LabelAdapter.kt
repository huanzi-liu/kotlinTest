package com.example.testdemo.pagerrecyclerview

import android.app.Activity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.testdemo.R

class LabelAdapter(val context: Activity, layoutIds:Int, data: MutableList<HomeLabelBean>) : BaseQuickAdapter<HomeLabelBean, BaseViewHolder>(layoutIds,data) {

    override fun convert(helper: BaseViewHolder, item: HomeLabelBean) {
        helper.setImageResource(R.id.ivLabelName,item.labelName)
        val ivLabelImg = helper.getView<AppCompatImageView>(R.id.ivLabelImg)

        val options: RequestOptions = RequestOptions()
                .error(item.labelDefImg)
                .placeholder(item.labelDefImg)
        if (!context.isDestroyed) {
            Glide.with(context).load(item.labelImg).apply(options).into(ivLabelImg)
        }
    }
}
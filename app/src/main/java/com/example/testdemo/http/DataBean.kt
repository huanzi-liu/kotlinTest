package com.example.testdemo.http

import com.example.testdemo.utils.GsonUtil
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseBean {
    override fun toString(): String {
        return GsonUtil.getBeanToJson(this)
    }
}

data class DataBean<out Data>(
        var code: Int, var msg: String?, val data: Data?
):BaseBean(){
    override fun toString(): String {
        return GsonUtil.getBeanToJson(this)
    }
}

data class UserConfigBean(
        @SerializedName("readMultipleTime")
        var readMultipleTime: Float?,
        @SerializedName("goldConsumeConfigAppDTO")
        var goldConsumeConfigAppDTO: GoldConsumeConfigAppDTO? = GoldConsumeConfigAppDTO(),
        @SerializedName("versionInfoList")
        var versionInfoList: MutableList<DictionaryBean> = ArrayList(),
        @SerializedName("screenProximityAlertsSystemVersionList")
        var screenProximityAlertsSystemVersionList: MutableList<DictionaryBean> = ArrayList()

) : BaseBean(), Serializable

data class GoldConsumeConfigAppDTO(
        @SerializedName("skipRead")
        var skipRead: Boolean = true,
        @SerializedName("skipVideo")
        var skipVideo: Boolean = true,
        @SerializedName("mallOrder")
        var mallOrder: Boolean = true) : BaseBean(), Serializable

data class DictionaryBean(
        @SerializedName("itemText")
        var itemText: String,
        @SerializedName("itemValue")
        var itemValue: String,
        @SerializedName("itemCode")
        var itemCode: String): BaseBean(), Serializable

class SayingBean : BaseBean(), Serializable {
    @SerializedName("en")
    var en: List<String>? = null

    @SerializedName("cn")
    var cn: List<String>? = null
}
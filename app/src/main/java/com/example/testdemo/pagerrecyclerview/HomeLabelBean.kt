package com.example.testdemo.pagerrecyclerview

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HomeLabelBean(
        @SerializedName("labelDefImg")
        var labelDefImg: Int,
        @SerializedName("labelImg")
        var labelImg: Any,
        @SerializedName("labelName")
        var labelName: Int,
        @SerializedName("labelId")
        var labelId: String,
        @SerializedName("number")
        var number: Int
) : Serializable
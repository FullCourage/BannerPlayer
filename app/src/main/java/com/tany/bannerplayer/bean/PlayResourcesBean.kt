package com.tany.bannerplayer.bean
import androidx.annotation.Keep

@Keep
data class PlayResourcesBean(
    /**
     * 图片或者视频路径
     */
    val url: String? = "",
    val isImg:Boolean,
    val title: String?="",
)
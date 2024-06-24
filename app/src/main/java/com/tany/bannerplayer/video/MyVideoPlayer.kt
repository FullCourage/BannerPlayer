package com.tany.bannerplayer.video

import android.content.Context
import android.util.AttributeSet
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

class MyVideoPlayer(context: Context, attrs: AttributeSet?) : StandardGSYVideoPlayer(context,attrs){


    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        hide()
    }

    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
        hide()
    }

    override fun changeUiToError() {
        super.changeUiToError()
        hide()
    }

    override fun changeUiToCompleteShow() {
        super.changeUiToCompleteShow()
        hide()
    }

    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
        hide()
    }

    override fun changeUiToPrepareingClear() {
        super.changeUiToPrepareingClear()
        hide()
    }

    override fun changeUiToPlayingClear() {
        super.changeUiToPlayingClear()
        hide()
    }

    override fun changeUiToPauseClear() {
        super.changeUiToPauseClear()
        hide()
    }

    override fun changeUiToPlayingBufferingClear() {
        super.changeUiToPlayingBufferingClear()
        hide()
    }

    override fun changeUiToClear() {
        super.changeUiToClear()
        hide()
    }

    override fun changeUiToCompleteClear() {
        super.changeUiToCompleteClear()
        hide()
    }

    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        hide()

    }

    override fun changeUiToNormal() {
        super.changeUiToNormal()
        hide()
    }
    private fun hide() {
        setViewShowState(mTopContainer, INVISIBLE)
        setViewShowState(mBottomContainer, INVISIBLE)
        setViewShowState(mStartButton, INVISIBLE)
        setViewShowState(mLoadingProgressBar, INVISIBLE)
        setViewShowState(mThumbImageViewLayout, INVISIBLE)
        setViewShowState(mBottomProgressBar, INVISIBLE)
        setViewShowState(mLockScreen, GONE)
    }


    override fun onVideoPause() {
        super.onVideoPause()
        mPauseBeforePrepared=false //是否准备完成前调用了暂停，避免再次暂停
    }
    override fun touchDoubleUp() {
        //双击会暂停，重载清除
    }
}
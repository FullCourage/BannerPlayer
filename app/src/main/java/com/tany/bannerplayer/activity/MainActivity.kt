package com.tany.bannerplayer.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.tany.bannerplayer.R
import com.tany.bannerplayer.adapter.PlayAdapter
import com.tany.bannerplayer.bean.PlayResourcesBean
import com.youth.banner.Banner
import com.youth.banner.listener.OnPageChangeListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity:AppCompatActivity() {
    val loopTime=6000L

    //列表+前后两页过渡页的坐标
    private var currentPos= 1

    private val taskHandler by lazy{
        Handler(Looper.getMainLooper())
    }

    private val adapter by lazy{
        PlayAdapter(this,resourcesList, this)
    }

    private val resourcesList by lazy{
        arrayListOf<PlayResourcesBean>().apply {
            add(PlayResourcesBean(isImg = true, title = "图片", url = "https://upload-images.jianshu.io/upload_images/5809200-a99419bb94924e6d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240"))
            add(PlayResourcesBean(isImg = true,title = "图片",url = "https://s10.mogucdn.com/mlcdn/c45406/170831_479g0ifl6f2i313feb5ech46kek21_778x440.jpg"))
            add(PlayResourcesBean(isImg = false, title = "视频",url = "https://media.w3.org/2010/05/sintel/trailer.mp4"))
            add(PlayResourcesBean(isImg = true,title = "图片",url = "https://s10.mogucdn.com/mlcdn/c45406/170831_7gee6d620i774ec3l5bfh55cfaeab_778x440.jpg"))
            add(PlayResourcesBean(isImg = false,title = "视频",url = "https://media.w3.org/2010/05/sintel/trailer.mp4"))
            add(PlayResourcesBean(isImg = true,title = "图片",url = "https://s10.mogucdn.com/mlcdn/c45406/170829_59ia6fd99ghkdkd9603kblha21h5b_778x440.jpg"))
            add(PlayResourcesBean(isImg = true,title = "图片",url = "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg"))
            add(PlayResourcesBean(isImg = false,title = "视频",url = "https://media.w3.org/2010/05/sintel/trailer.mp4"))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBanner()
    }

    private fun initBanner() {

        // 添加生命周期管理，确保在适当的生命周期内开始和停止轮播
        banner.addBannerLifecycleObserver(this)
            .setAdapter(adapter,true) //是否开启无限循环
            .setLoopTime(loopTime) //轮播时间


        // 设置轮播图的点击事件监听器
        banner.addOnPageChangeListener(object :
            OnPageChangeListener {

            /**
             * 滑动中的监听，当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法会一直得到调用
             */
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            /**
             * 监听滑动到对应索引值的页面，第一个页面不执行
             */
            override fun onPageSelected(position: Int) {
                if (position == banner.realCount - 1) {
                    currentPos=banner.realCount
                } else if (position == 0) {
                    currentPos = 1
                } else {
                    currentPos = position + 1
                }
            }
            /**
             * 滑动状态监听
             */
            override fun onPageScrollStateChanged(state: Int) {
                // Banner跳转之后再去控制视频播放
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    if (banner.isInfiniteLoop) {
                        taskHandler.post(task)
                    }
                }
            }
        })

        //重新设置banner数据
        //banner.setDatas(resourcesList)

    }


    val task = Runnable {
        //可能是首尾切换页，两个页面循环跳转
        if(adapter.getVHMap().containsKey(currentPos)){
            if(adapter.getVHMap()[currentPos] is PlayAdapter.VideoHolder){
                val holder = (adapter.getVHMap()[currentPos] as PlayAdapter.VideoHolder)
                GSYVideoManager.onPause()
                holder.video.startPlayLogic()
                banner.isAutoLoop(true)
                banner.stop()
                banner.isAutoLoop(false)
            }else{
                GSYVideoManager.onPause()

                banner.isAutoLoop(true)
                banner.start()
            }
        }else{
            GSYVideoManager.onPause()
            banner.isAutoLoop(true)
            banner.start()
        }
    }

    override fun onResume() {
        super.onResume()
        taskHandler.post(task)
    }


    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        //移除数据绑定，否则第二次设置适配器出错
        banner.destroy()
    }

}
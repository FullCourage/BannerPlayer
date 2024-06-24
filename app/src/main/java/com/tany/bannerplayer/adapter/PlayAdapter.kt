package com.tany.bannerplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.tany.bannerplayer.R
import com.tany.bannerplayer.activity.MainActivity
import com.tany.bannerplayer.bean.PlayResourcesBean
import com.tany.bannerplayer.video.MyVideoPlayer
import com.youth.banner.adapter.BannerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class PlayAdapter(private val mContext: Context, private val dataList: ArrayList<PlayResourcesBean>,
                  private val mActivity: MainActivity
):
    BannerAdapter<PlayResourcesBean, RecyclerView.ViewHolder>(dataList) {

    private val mVHMap = HashMap<Int,RecyclerView.ViewHolder>()

    private val options by lazy {
        RequestOptions().priority(Priority.HIGH)
            .placeholder(R.mipmap.default_ic)
            .error(R.mipmap.default_ic)
    }

    companion object{
       //视频
       const val VIDEO = 1
        //图片
        const val IMAGE = 2
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)

        val realPosition = getRealPosition(position)
        if (holder is VideoHolder){
            setVideo( holder, getData(realPosition),position,realPosition,realCount)
            mVHMap[position] = holder
        }else if(holder is ImageHolder){
            setImage(holder,getData(realPosition),realPosition,realCount)
            mVHMap[position] = holder
        }
    }

    override fun getItemViewType(position: Int): Int {
        //这里的position不是真实的坐标，获取数据需要转换
        return if (getData(getRealPosition(position)).isImg){
            IMAGE
        }else {
            VIDEO
        }
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val holder:RecyclerView.ViewHolder
        val from = LayoutInflater.from(mContext)
        if (viewType==VIDEO){
            val view=from.inflate(R.layout.play_item_video,parent,false)
            holder=VideoHolder(view)
        }else {
            val view=from.inflate(R.layout.play_item_image,parent,false)
            holder=ImageHolder(view)
        }

        return holder
    }

    private fun setImage(
        holder: ImageHolder,
        data: PlayResourcesBean,
        position: Int,
        size: Int
    ) {
        Glide.with(mContext)
            .load(data.url)
            .apply(options)
            .into(holder.image)

        holder.title.text=data.title?:""
        holder.numIndicator.text="${position+1}/$size"
    }

    private fun setVideo(
        holder: VideoHolder,
        data: PlayResourcesBean,
        position: Int,
        realPosition: Int,
        size: Int
    ) {
        holder.video.apply {
            setUp(data.url,true,mContext.externalCacheDir,null,"")
            titleTextView.visibility = View.GONE
            backButton.visibility = View.GONE
            fullscreenButton.visibility = View.GONE
            startButton.visibility = View.GONE


            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = true
            //禁止全屏
            isAutoFullWithSize = false
            //isStartAfterPrepared=true
            dismissControlTime=0
            isClickable=false
            isEnabled=false
            isLongClickable=false
            playPosition=position


            //禁止滑动
            setIsTouchWiget(false)
            setVideoAllCallBack(object :GSYSampleCallBack(){
                override fun onComplete(url: String?, vararg objects: Any?) {
                    super.onComplete(url, *objects)
                    mActivity.banner.isAutoLoop(true)
                    mActivity.banner.start()
                }

                override fun onPlayError(url: String?, vararg objects: Any?) {
                    super.onPlayError(url, *objects)
                    mActivity.banner.isAutoLoop(true)
                    mActivity.banner.start()
                }

                override fun onClickStop(url: String?, vararg objects: Any?) {
                    super.onClickStop(url, *objects)
                    mActivity.banner.isAutoLoop(true)
                    mActivity.banner.start()
                }

                override fun onPrepared(url: String?, vararg objects: Any?) {
                    super.onPrepared(url, *objects)

                }

                override fun onStartPrepared(url: String?, vararg objects: Any?) {
                    super.onStartPrepared(url, *objects)

                }
                override fun onAutoComplete(url: String?, vararg objects: Any?) {
                    super.onAutoComplete(url, *objects)
                    mActivity.banner.isAutoLoop(true)

                    //快速轮播到下一页
                    mActivity.banner.setLoopTime(100)
                    mActivity.banner.start()
                    mActivity.banner.setLoopTime(mActivity.loopTime)
                }
            })
        }

        holder.title.text=data.title?:""
        holder.numIndicator.text="${realPosition+1}/$size"

    }

    internal class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val video: MyVideoPlayer
        val title: TextView
        val numIndicator:TextView
        init {
            video = itemView.findViewById(R.id.banner_vp)
            title = itemView.findViewById(R.id.video_title)
            numIndicator = itemView.findViewById(R.id.video_numIndicator)
        }
    }

    internal class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView
        val title: TextView
        val numIndicator:TextView
        init {
            image = itemView.findViewById(R.id.banner_image)
            title = itemView.findViewById(R.id.image_title)
            numIndicator = itemView.findViewById(R.id.image_numIndicator)
        }
    }

    fun getVHMap(): HashMap<Int,RecyclerView.ViewHolder> {
        return mVHMap
    }

    override fun onBindView(
        holder: RecyclerView.ViewHolder?,
        data: PlayResourcesBean?,
        position: Int,
        size: Int
    ) {
    }


}
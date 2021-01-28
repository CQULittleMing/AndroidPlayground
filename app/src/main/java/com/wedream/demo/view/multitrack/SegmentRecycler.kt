package com.wedream.demo.view.multitrack

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import com.wedream.demo.R
import com.wedream.demo.view.multitrack.base.AbsPlaneRecyclerAdapter
import com.wedream.demo.view.multitrack.base.ITrackContainer
import com.wedream.demo.view.multitrack.base.ElementData

class SegmentRecycler(context: Context, attrs: AttributeSet?, defStyle: Int) : ScrollView(context, attrs, defStyle), ITrackContainer<ElementData> {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private lateinit var trackContainerInner: FrameLayout
    private var elementHolders = mutableMapOf<Long, AbsPlaneRecyclerAdapter.ViewHolder>()
    private var handleHorizontalEvent = false
    private var visibleBoundLeft = 0
    private var visibleBoundRight = 0
    private var segmentAdapter: AbsPlaneRecyclerAdapter<AbsPlaneRecyclerAdapter.ViewHolder>? = null
    private var listener: EventListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        trackContainerInner = findViewById<FrameLayout>(R.id.track_container_inner)
    }

    fun setAdapter(adapter: AbsPlaneRecyclerAdapter<AbsPlaneRecyclerAdapter.ViewHolder>) {
        this.segmentAdapter = adapter
        this.segmentAdapter?.registerAdapterDataObserver(adapterDataObserver)
    }

    fun notifyHorizontalScroll(left: Int, right: Int) {
        visibleBoundLeft = left
        visibleBoundRight = right
        Log.e("xcm", "visibleBoundLeft = $visibleBoundLeft, visibleBoundRight = $visibleBoundRight")
        updateVisibleItem()
    }

    private fun updateVisibleItem() {
        for (holder in elementHolders.values) {
            val view = holder.itemView
            view.visibility = (if (showItemVisible(view)) VISIBLE else GONE)
        }
    }

    private fun showItemVisible(view: View): Boolean {
        return !(view.r() < visibleBoundLeft || view.l() > visibleBoundRight)
    }

    // TODO 合并更新
    private val adapterDataObserver = object : AbsPlaneRecyclerAdapter.AdapterDataObserver() {
        override fun onChanged() {
            updateViews()
        }

        override fun onItemInserted(ids: List<Long>) {
            insertElements(ids)
        }

        override fun onItemRemoved(ids: List<Long>) {
            handleItemRemoved(ids)
        }

        override fun handleHorizontalTouchEvent(handle: Boolean) {
            handleHorizontalEvent = handle
        }

        override fun onItemChanged(ids: List<Long>) {
            handleItemChanged(ids)
        }
    }

    private fun updateViews() {
        val adapter = segmentAdapter ?: return
        insertElements(adapter.getElementIds())
    }

    private fun insertElements(ids: List<Long>) {
        for (id in ids) {
            val adapter = segmentAdapter ?: return
            val holder = adapter.onCreateElementHolder(trackContainerInner, adapter.getElementType(id))
            updateHolder(adapter, holder, id)
            trackContainerInner.addView(holder.itemView)
            elementHolders[id] = holder
        }
    }

    private fun handleItemRemoved(ids: List<Long>) {
        for (id in ids) {
            val holder = elementHolders.remove(id)
            holder?.let {
                trackContainerInner.removeView(holder.itemView)
            }
        }
    }

    private fun handleItemChanged(ids: List<Long>) {
        for (id in ids) {
            val adapter = segmentAdapter ?: return
            val holder = elementHolders[id] ?: return
            updateHolder(adapter, holder, id)
        }
    }

    private fun updateHolder(adapter: AbsPlaneRecyclerAdapter<AbsPlaneRecyclerAdapter.ViewHolder>,
                             holder: AbsPlaneRecyclerAdapter.ViewHolder,
                             id: Long) {
        adapter.onBindElementHolder(holder, id)
        val params = holder.itemView.layoutParams as MarginLayoutParams?
            ?: FrameLayout.LayoutParams(holder.width, holder.height)
        params.setMargins(holder.x, holder.y, 0, 0)
        params.width = holder.width
        params.height = holder.height
        holder.itemView.layoutParams = params
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(handleHorizontalEvent)
        return super.onInterceptTouchEvent(ev)
    }

    // 如果该方法被回调，说明子view没有处理事件
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        listener?.onEmptyClick()
        return super.onTouchEvent(ev)
    }

    fun handleHorizontalTouchEvent(value: Boolean) {
        handleHorizontalEvent = value
    }

    fun setEventListener(listener: EventListener){
        this.listener = listener
    }

    interface EventListener {
        fun onEmptyClick()
    }
}

fun View.r() : Float{
    return right + translationX
}

fun View.l() : Float{
    return left + translationX
}
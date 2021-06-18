package com.wedream.demo.videoeditor.timeline.data

import androidx.lifecycle.ViewModel
import com.wedream.demo.videoeditor.editor.EditorData
import com.wedream.demo.videoeditor.editor.VideoEditor
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.project.ActionType
import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.project.asset.PlacedAsset
import com.wedream.demo.videoeditor.timeline.utils.TimelineUtils

class TimelineViewModel(private val videoEditor: VideoEditor) : ViewModel() {

    private var segmentMap = hashMapOf<Long, Segment>()
    private var timelineRealWidth = 0
    private var scale = 1.0
    private var timelineScrollX = 0

    init {
        videoEditor.onProjectChange {
            updateTimeline(it)
        }
    }

    private fun updateTimeline(editorData: EditorData) {
        timelineRealWidth = TimelineUtils.time2Width(videoEditor.getProjectDuration(), scale)
        for (event in editorData.events) {
            if (event.actionType == ActionType.Add) {
                val asset = videoEditor.getAsset(event.id) ?: continue
                if (asset is PlacedAsset) {
                    val start = TimelineUtils.time2Width(asset.getStart(), scale)
                    val end = TimelineUtils.time2Width(asset.getEnd(), scale)
                    segmentMap[asset.id] = TextSegment(asset.id, start, end, asset.id.toString())
                } else {
                    segmentMap[asset.id] = TextSegment(asset.id, 0, 0, asset.id.toString())
                }
            } else if (event.actionType == ActionType.Delete) {
                segmentMap.remove(event.id)
            } else if (event.actionType == ActionType.Modify) {
                val asset = videoEditor.getAsset(event.id) ?: continue
                if (asset is PlacedAsset) {
                    val start = TimelineUtils.time2Width(asset.getStart(), scale)
                    val end = TimelineUtils.time2Width(asset.getEnd(), scale)
                    segmentMap[asset.id]?.let {
                        it.left = start
                        it.right = end
                    }
                }
            }
        }
        if (editorData.mainTrackModified) {
            // 主轨被修改了，修改全部重新计算
            val assets = videoEditor.getAssets()
            var assetStart = 0.0
            var assetEnd = 0.0
            for (asset in assets) {
                assetEnd += asset.duration
                val start = TimelineUtils.time2Width(assetStart, scale)
                val end = TimelineUtils.time2Width(assetEnd, scale)
                segmentMap[asset.id]?.let {
                    it.left = start
                    it.right = end
                }
                assetStart += asset.duration
            }
        }
        MessageChannel.sendMessage(TimeLineMessageHelper.packTimelineChangedMessage(editorData))
    }

    fun getSegments(): List<Segment> {
        return segmentMap.values.toList()
    }

    fun getScale(): Double {
        return scale
    }

    fun setScale(scale: Double) {
        this.scale = scale
//        updateTimeline()
    }

    fun setScrollX(scrollX: Int) {
        this.timelineScrollX = scrollX
        MessageChannel.sendMessage(TimeLineMessageHelper.MSG_TIMELINE_SCROLL_CHANGED)
    }

    fun getScrollX(): Int {
        return timelineScrollX
    }

    fun getCurrentTime(): Double {
        return TimelineUtils.width2time(timelineScrollX, scale)
    }

    fun getRealTimeWidth(): Int {
        return timelineRealWidth
    }

    fun getSegmentByPos(pos: Int): Segment? {
        for (s in segmentMap.values) {
            if (s.contains(pos)) {
                return s
            }
        }
        return null
    }

    fun getSegment(id: Long): Segment? {
        return segmentMap[id]
    }

    fun getCurrentSegment(): Segment? {
        return getSegmentByPos(timelineScrollX)
    }
}
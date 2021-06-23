package com.wedream.demo.videoeditor.project.asset

import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.project.ProjectModifyListener

open class Asset(
    val id: Long,
    val type: AssetType,
    val fixDuration: Double,
    private var clipStart: Double = 0.0,
    private var clipEnd: Double = fixDuration
) {
    open val duration
        get() = clipEnd - clipStart

    private var modifyListener: ProjectModifyListener? = null

    fun getClipStart(): Double {
        return clipStart
    }

    fun getClipEnd(): Double {
        return clipEnd
    }

    fun setClipStart(start: Double) {
        this.clipStart = start
        modifyListener?.notifyItemModified(this)
    }

    fun setClipEnd(end: Double) {
        this.clipEnd = end
        modifyListener?.notifyItemModified(this)
    }

    fun setModifyListener(listener: ProjectModifyListener) {
        this.modifyListener = listener
    }

    fun removeModifyListener() {
        this.modifyListener = null
    }

    fun getModifyListener(): ProjectModifyListener? {
        return modifyListener
    }
}
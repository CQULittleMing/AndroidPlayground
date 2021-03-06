package com.wedream.demo.videoeditor.project.asset

import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.project.asset.operation.ISpeed
import com.wedream.demo.videoeditor.project.asset.operation.SpeedImpl

class MainTrackAsset(
    id: Long,
    private var path: String,
    assetType: AssetType,
    fixDuration: Double,
) : Asset(id, assetType, fixDuration), ISpeed {

    private val speedImpl = SpeedImpl(this)

    override val duration: Double
        get() = super.duration / getSpeed()

    override fun setSpeed(speed: Double) {
        speedImpl.setSpeed(speed)
    }

    override fun getSpeed(): Double {
        return speedImpl.getSpeed()
    }
}
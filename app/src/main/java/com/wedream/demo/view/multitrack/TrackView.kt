package com.wedream.demo.view.multitrack

import android.content.Context
import android.util.AttributeSet
import com.wedream.demo.view.multitrack.base.ElementView

class TrackView(context: Context, attrs: AttributeSet?, defStyle: Int) : ElementView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    init {
        isClickable = false
    }
}
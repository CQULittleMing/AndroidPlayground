package com.wedream.demo.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log

class InterceptEventActivity : AppCompatActivity() {

    var intercept = false
    var animate = true
    var translateXRunnable: TranslateXRunnable? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inercept_event)
        findViewById<Button>(R.id.bt1).setOnTouchListener { v, event ->
            log { "x = ${event.x}, y = ${event.y}" }
            log { "rawX = ${event.rawX}, rawY = ${event.rawY}" }
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    intercept = true
                    translateXRunnable = TranslateXRunnable(v)
                    v.post(translateXRunnable)
                }
                MotionEvent.ACTION_UP -> {
                    intercept = false
                    v.removeCallbacks(translateXRunnable)
                }
            }
            true
        }
        findViewById<Button>(R.id.bt2).setOnClickListener {
            log { "onclick $it" }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        log { "ev = ${ev.action}" }
        return if (ev.actionMasked == MotionEvent.ACTION_POINTER_DOWN && intercept) {
            true
        } else {
            super.dispatchTouchEvent(ev)
        }
    }

     class TranslateXRunnable(val view: View) : Runnable {
         override fun run() {
             view.translationX += 10
             view.postDelayed(this, 200)
         }
     }
}
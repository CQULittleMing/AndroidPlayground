package com.wedream.demo.inject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.project.VideoProject

class AnnotationTestActivity : AppCompatActivity() {

    @Inject(AccessId.VIDEO_PROJECT)
    lateinit var videoProject: VideoProject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(VideoProject())
    }

    private fun inject(vararg objects: Any) {
        val clazz = this.javaClass
        val fields = clazz.declaredFields
        for (field in fields) {
//            if (field.isAnnotationPresent(Inject::class.java)) {
//                val inject = field.getAnnotation(Inject::class.java)
//                log { "fieldType:${field.type}" }
//                log { "inject:$inject" }
//            }
            for (obj in objects) {
                if (obj.javaClass == field.type) {
                    field.set(this, obj)
                }
            }
        }
    }
}
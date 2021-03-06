package com.liabit.test14

import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.liabit.screencapture.ScreenCapture
import com.liabit.screencapture.ScreenCaptureUtil
import com.liabit.screenrecord.ScreenRecordUtil
import com.liabit.test14.databinding.ActivityMainBinding
import com.liabit.viewbinding.inflate

class MainActivity : AppCompatActivity() {

    companion object {
        const val ALIAS = "com.liabit.test14.FakeMainActivity"
    }

    private val binding by inflate<ActivityMainBinding>()

    private var floatButtons: LinearLayout? = null

    private val callback = Handler.Callback {
        handler?.sendEmptyMessageDelayed(1, 1000)
        return@Callback true
    }
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.hide.setOnClickListener {
            val componentName = ComponentName(this, ALIAS)
            packageManager.setComponentEnabledSetting(componentName,
                    //PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP)
        }

        binding.show.setOnClickListener {
            val componentName = ComponentName(this, ALIAS)
            packageManager.setComponentEnabledSetting(componentName,
                    //PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //addFloat()
        }

        binding.testScreenRecorder.setOnClickListener {
            if (floatButtons == null) {
                addFloat()
            } else {
                removeFloat()
            }
        }

        handler = Handler(callback)
        handler?.sendEmptyMessageDelayed(1, 1000)

        binding.toTestRecord.setOnClickListener {
            startActivity(Intent(this, TestMediaRecorder::class.java))
        }
        binding.toPicker.setOnClickListener {
            startActivity(Intent(this, TestMapLocationPicker::class.java))
        }
    }

    private fun removeFloat() {
        floatButtons?.let {
            val windowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.removeViewImmediate(it)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addFloat() {
        try {
            val windowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            floatButtons = LinearLayout(applicationContext).apply {
                this.orientation = LinearLayout.VERTICAL

                val dp60 =
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                60f,
                                application.resources.displayMetrics
                        ).toInt()


                val capture = ImageButton(applicationContext)
                capture.setImageResource(R.mipmap.screen_capture)

                val callback = object : ScreenCapture.Callback {

                    override fun onStartCapture() {
                        Log.d("TTTT", "onStartCapture")
                        this@apply.visibility = View.INVISIBLE
                    }

                    override fun onEndCapture(path: String?) {
                        Log.d("TTTT", "onEndCapture path=$path")
                        this@apply.visibility = View.VISIBLE
                    }
                }

                capture.setOnClickListener {
                    Log.d("TTTT", "capture")
                    ScreenCaptureUtil.getInstance(this@MainActivity).startCapture(callback)
                }
                capture.layoutParams = LinearLayout.LayoutParams(dp60, dp60)
                this.addView(capture)

                val record = ImageButton(applicationContext)
                record.setImageResource(R.mipmap.screen_record)
                record.setOnClickListener {
                    Log.d("TTTT", "record")
                    if (ScreenRecordUtil.getInstance(this@MainActivity).isRecording()) {
                        ScreenRecordUtil.getInstance(this@MainActivity).stopRecord()
                    } else {
                        ScreenRecordUtil.getInstance(this@MainActivity).startRecord()
                    }
                }
                record.layoutParams = LinearLayout.LayoutParams(dp60, dp60)
                this.addView(record)

                val lp = WindowManager.LayoutParams()

                lp.type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                /*WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY*/
                    2038 - 1
                else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

                lp.width = dp60
                lp.height = dp60 * 2

                lp.gravity = Gravity.RIGHT or Gravity.TOP
                lp.x = 0
                lp.y = dp60 * 2

                lp.format = PixelFormat.RGBA_8888
                lp.flags =
                        (WindowManager.LayoutParams.FLAG_FULLSCREEN
                                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                                or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                                or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                )

                windowManager.addView(this, lp)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TTTT", "error:", e)
        }
    }


}

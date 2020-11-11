package com.liabit.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.liabit.test.decorationtest.TestRecyclerViewDecorationActivity
import com.liabit.test.filtertest.TestFilterActivity
import com.liabit.test.gesturetest.TestDragActivity
import com.liabit.test.gesturetest.TestSwipeActivity
import com.liabit.test.tablayouttest.TestTabLayoutActivity
import com.liabit.test.tagviewtest.TestTagViewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {

        when (view.id) {
            R.id.stateButtonTest -> {
                startActivity(Intent(this, TestStateButtonActivity::class.java))
            }

            R.id.shimmerTest -> {
                startActivity(Intent(this, TestShimmerActivity::class.java))
            }

            R.id.gestureDragTest -> {
                startActivity(Intent(this, TestDragActivity::class.java))
            }

            R.id.gestureSwipeTest -> {
                startActivity(Intent(this, TestSwipeActivity::class.java))
            }

            R.id.pickerTest -> {
                startActivity(Intent(this, TestPickerActivity::class.java))
            }

            R.id.filterTest -> {
                startActivity(Intent(this, TestFilterActivity::class.java))
            }

            R.id.tabLayoutTest -> {
                startActivity(Intent(this, TestTabLayoutActivity::class.java))
            }

            R.id.decorationTest -> {
                startActivity(Intent(this, TestRecyclerViewDecorationActivity::class.java))
            }

            R.id.addSubViewTest -> {
                startActivity(Intent(this, TestAddSubViewActivity::class.java))
            }

            R.id.tagViewTest -> {
                startActivity(Intent(this, TestTagViewActivity::class.java))
            }

            R.id.popupTest -> {
                startActivity(Intent(this, TestPopupActivity::class.java))
            }

            R.id.otherTest -> {
                startActivity(Intent(this, TestFragmentVisibleActivity::class.java))
            }
        }

    }
}
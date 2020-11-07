package com.liabit.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_recycler_view_decoration.*

class RecyclerViewDecorationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_decoration)

        horizontalLinear.setOnClickListener {
            val intent = Intent(this, RecyclerViewDecorationTestActivity::class.java)
            intent.putExtra("TYPE", "horizontalLinear")
            startActivity(intent)
        }
        verticalLinear.setOnClickListener {
            val intent = Intent(this, RecyclerViewDecorationTestActivity::class.java)
            intent.putExtra("TYPE", "verticalLinear")
            startActivity(intent)
        }
        horizontalGrid.setOnClickListener {
            val intent = Intent(this, RecyclerViewDecorationTestActivity::class.java)
            intent.putExtra("TYPE", "horizontalGrid")
            startActivity(intent)
        }
        verticalGrid.setOnClickListener {
            val intent = Intent(this, RecyclerViewDecorationTestActivity::class.java)
            intent.putExtra("TYPE", "verticalGrid")
            startActivity(intent)
        }
        horizontalStaggered.setOnClickListener {
            val intent = Intent(this, RecyclerViewDecorationTestActivity::class.java)
            intent.putExtra("TYPE", "horizontalStaggered")
            startActivity(intent)
        }
        verticalStaggered.setOnClickListener {
            val intent = Intent(this, RecyclerViewDecorationTestActivity::class.java)
            intent.putExtra("TYPE", "verticalStaggered")
            startActivity(intent)
        }
    }

}
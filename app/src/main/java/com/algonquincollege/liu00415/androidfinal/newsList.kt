package com.algonquincollege.liu00415.androidfinal

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class newsList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        Toast.makeText(this, "Welcome to News Portal", Toast.LENGTH_LONG).show()

    }
}

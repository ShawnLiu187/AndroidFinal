package com.algonquincollege.liu00415.androidfinal

import android.app.Activity
import android.os.Bundle

class FaveNewsDetails : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fave_news_details)

        var dataToPass = intent.extras

        var newFragment = NewsFragment()
        newFragment.arguments = dataToPass

        var transition = getFragmentManager().beginTransaction() //how to load fragment
        transition.replace(R.id.newsFragmentLocation, newFragment)

        transition.commit()
    }
}

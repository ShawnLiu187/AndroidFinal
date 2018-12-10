package com.algonquincollege.liu00415.androidfinal

import android.app.Activity
import android.os.Bundle

class FoodDetails : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        var dataToPass = intent.extras //get our bundle back

        var newFragment = FoodFragment()
        newFragment.arguments = dataToPass // bundle goes to fragment

        var transition = getFragmentManager().beginTransaction() //how to load fragment
        transition.replace(R.id.foodFragmentLocation, newFragment) //where to load, what to load

        transition.commit()
    }
}

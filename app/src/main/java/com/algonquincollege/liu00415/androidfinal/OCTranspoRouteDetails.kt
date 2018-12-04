package com.algonquincollege.liu00415.androidfinal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class OCTranspoRouteDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_octranspo_route_details)

        /**
         * Send route data to fragment
         * Load Fragment
         */

        var newRouteFragment = OCTranspoFragment()
        newRouteFragment.arguments = intent.extras
        var transition = getFragmentManager().beginTransaction()
        transition.replace(R.id.fragment_location, newRouteFragment)
        transition.commit()
    }
}

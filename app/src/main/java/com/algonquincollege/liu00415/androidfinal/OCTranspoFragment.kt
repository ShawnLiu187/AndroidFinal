package com.algonquincollege.liu00415.androidfinal

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class OCTranspoFragment : Fragment(){

    /**
     * Inflate fragment and set text views to route information
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        var routeData = arguments
        var screen = inflater.inflate(R.layout.oc_fragment, container, false)

        var fullname = "${routeData.getString("RouteNumber")} ${routeData.getString("Destination")}"

        var destination = screen.findViewById<TextView>(R.id.destination)
        destination.text = fullname
        var location = screen.findViewById<TextView>(R.id.location)
        location.text = routeData.getString("Location")
        var time = screen.findViewById<TextView>(R.id.time)
        time.text = routeData.getString("StartTime")
        var speed = screen.findViewById<TextView>(R.id.speed)
        speed.text = routeData.getString("Speed")
        var delay = screen.findViewById<TextView>(R.id.delay)
        delay.text = routeData.getString("Delay")

        return screen


    }
}
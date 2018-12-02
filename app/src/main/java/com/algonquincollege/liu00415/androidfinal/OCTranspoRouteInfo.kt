package com.algonquincollege.liu00415.androidfinal

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.HttpURLConnection
import java.net.URL

class OCTranspoRouteInfo : AppCompatActivity() {

    lateinit var routeDirection: String
    lateinit var routeName: String

    var destination: String = "Not Available"
    var latitude: String = "Not Available"
    var longitude: String = "Not Available"
    var speed: String = "Not Available"
    var startTime: String = "Not Available"
    var delay: String = "Not Available"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_octranspo_route_info)

        val routeNumber = intent.extras.get("routeNumber").toString()
        val stopNumber = intent.extras.get("stopNumber").toString()
        routeDirection = intent.extras.get("routeDirection").toString()
        routeName = intent.extras.get("routeName").toString()

        var routeQuery = GetRouteInfo()
        routeQuery.execute(routeNumber, stopNumber)
    }

    inner class GetRouteInfo : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg params: String?): String {

            val url = URL("https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=${params[1]}&routeNo=${params[0]}")
            val connection = url.openConnection() as HttpURLConnection
            val response = connection.inputStream

            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = false
            val xpp = factory.newPullParser()
            xpp.setInput(response, "UTF-8")

            var selectedRoute = false

            while (xpp.eventType != XmlPullParser.END_DOCUMENT){

                if (xpp.eventType == XmlPullParser.START_TAG){
                    if (xpp.name == "Direction"){
                        xpp.next()
                        if(xpp.text == routeDirection){
                            selectedRoute = true
                        }else{
                            selectedRoute = false
                        }
                    }
                }

                while (selectedRoute == true){
                    xpp.next()

                    if (xpp.eventType == XmlPullParser.START_TAG){

                        if (xpp.name == "TripDestination"){
                            xpp.next()
                            if (xpp.text != null){destination = xpp.text}
                        }
                        if (xpp.name == "TripStartTime"){
                            xpp.next()
                            if (xpp.text != null){startTime = xpp.text}
                        }
                        if (xpp.name == "AdjustedScheduleTime"){
                            xpp.next()
                            if (xpp.text != null){delay = xpp.text}
                        }
                        if (xpp.name == "GPSSPEED"){
                            xpp.next()
                            if (xpp.text != null){speed = xpp.text}
                        }
                        if (xpp.name == "SPEED"){
                            xpp.next()
                            if (xpp.text != null){speed = xpp.text}
                        }
                        if (xpp.name == "GPSSpeed"){
                            xpp.next()
                            if (xpp.text != null){speed = xpp.text}
                        }
                        if (xpp.name == "Speed"){
                            xpp.next()
                            if (xpp.text != null){speed = xpp.text}
                        }
                        if (xpp.name == "Latitude"){
                            xpp.next()
                            if (xpp.text != null){latitude = xpp.text}
                        }
                        if (xpp.name == "Longitude"){
                            xpp.next()
                            if (xpp.text != null){longitude = xpp.text}
                        }

                    }

                    if (xpp.eventType == XmlPullParser.END_TAG){
                        if (xpp.name == "Trip"){
                            selectedRoute = false
                        }
                    }
                }

                xpp.next()

            }

            return "Done"
        }

        override fun onPostExecute(result: String?) {

            var routeData = Bundle()
            routeData.putString("Destination", destination)
            routeData.putString("Location", "Lat: $latitude Long: $longitude")
            routeData.putString("Speed", speed)
            routeData.putString("StartTime", startTime)
            routeData.putString("Delay", delay)

            var newRouteFragment = OCTranspoFragment()
            newRouteFragment.arguments = routeData
        }
    }
}
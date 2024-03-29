package com.algonquincollege.liu00415.androidfinal

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.HttpURLConnection
import java.net.URL
import android.content.Intent

class OCTranspoStopInfo : AppCompatActivity() {

    /**
     *  Initialize variables
     */

    // INIT STUFF //

    var routeNumberList = ArrayList<String>()
    var routeNameList = ArrayList<String>()
    var routeFullName = ArrayList<String>()
    var routeDirectionList = ArrayList<String>()
    lateinit var stopAdapter: MyAdapter
    lateinit var oc_progressBar: ProgressBar
    lateinit var stopName: String
    lateinit var saveStop: Button
    lateinit var deleteStop: Button
    lateinit var stopNameTextView: TextView

    //  ROUTE INIT  //
    lateinit var routeDirection: String
    lateinit var routeName: String
    lateinit var routeNumber: String

    /**
     * Initialize route data with default values because octranspo sucks
     */

    var destination: String = "Not Available"
    var latitude: String = "Not Available"
    var longitude: String = "Not Available"
    var speed: String = "Not Available"
    var startTime: String = "Not Available"
    var delay: String = "Not Available"
    var average: Int = 0

    // ON CREATE //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_octranspo_stop_info)

        val stopNumber = intent.extras.get("stopNumber").toString()

        val routeListView = findViewById<ListView>(R.id.routeListView)
        stopAdapter = MyAdapter(this)
        routeListView.adapter = stopAdapter

        var stopNumberQuery = GetStopInfo()
        stopNumberQuery.execute(stopNumber)

        oc_progressBar = findViewById(R.id.oc_progressBar)
        oc_progressBar.visibility = View.VISIBLE

        stopName = "Unknown stop"
        stopNameTextView = findViewById(R.id.stopName)

        /**
         *  Delete button to delete stop being displayed
         *  Takes user back to first activity
         */

        deleteStop = findViewById(R.id.deleteStop)
        deleteStop.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra("fullname", stopName)
            resultIntent.putExtra("id", intent.extras.get("id").toString())

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        /**
         *  Add button to save stop being displayed
         *  Takes user back to first activity
         */

        saveStop = findViewById(R.id.saveStop)
        saveStop.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra("stopNumber", stopNumber)
            resultIntent.putExtra("stopName", stopName)

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        /**
         *  Item click listeners for user to select a bus route
         *  Runs GetRouteInfo then starts OCTranspoRouteDetails activity
         */

        routeListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, View, position, id ->

            routeNumber = routeNumberList.get(position)
            routeDirection = routeDirectionList.get(position)
            routeName = routeNameList.get(position)

            var routeQuery = GetRouteInfo()
            routeQuery.execute(routeNumber, stopNumber)

        }
    }


    // QUERY STUFF //

    /**
     *  API call that returns a list of bus routes for the chosen stop
     */

    inner class GetStopInfo : AsyncTask<String, Int, String>(){

        var progress = 0

        override fun doInBackground(vararg params: String?): String {

            val url = URL("https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=${params[0]}")
            val connection = url.openConnection() as HttpURLConnection
            val response = connection.inputStream

            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = false
            val xpp = factory.newPullParser()
            xpp.setInput( response  , "UTF-8")

            progress += 50
            publishProgress()

            /**
             * Parse through xml response for route number, heading, direction and description tags
             * Save the data in arrays
             */

            while (xpp.eventType != XmlPullParser.END_DOCUMENT){

                if (xpp.eventType == XmlPullParser.START_TAG){
                    if (xpp.name == "RouteNo"){
                        xpp.next()
                        routeNumberList.add(xpp.text)
                    }
                    if (xpp.name == "RouteHeading"){
                        xpp.next()
                        routeNameList.add(xpp.text)
                    }
                    if (xpp.name == "Direction"){
                        xpp.next()
                        routeDirectionList.add(xpp.text)
                    }
                    if (xpp.name == "StopDescription"){
                        xpp.next()
                        stopName = xpp.text
                    }
                }
                xpp.next()
            }

            progress += 25
            publishProgress()

            /**
             * Combine values from route number and route name arrays for full name to be displayed to user
             */

            var i = 0
            val arrayLength = routeNumberList.size
            while (i < arrayLength){
                routeFullName.add("${routeNumberList[i]} - ${routeNameList[i]}")
                i++
            }

            progress += 25
            publishProgress()

            return "Done"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            oc_progressBar.progress = progress
        }

        /**
         * Hides progress bar, shows save or delete button, reloads list view
         */

        override fun onPostExecute(result: String?) {
            stopAdapter.notifyDataSetChanged()
            oc_progressBar.visibility = View.INVISIBLE
            stopNameTextView.visibility = View.VISIBLE
            stopNameTextView.text = stopName

            if (intent.extras.get("stopSaved").toString() == "false" ){
                saveStop.visibility = View.VISIBLE
            }
            else{
                deleteStop.visibility = View.VISIBLE
            }


        }

    }

    //  ROUTE QUERY STUFF   //

    /**
     *  API call that gets information about selected route
     *  Sends data to OCTranspoRouteDetails activity
     */

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

            var lateStops = 0
            var totalLateness = 0

            while (xpp.eventType != XmlPullParser.END_DOCUMENT){
                if (xpp.eventType == XmlPullParser.START_TAG){
                    if (xpp.name == "AdjustedScheduleTime"){
                        xpp.next()
                        //if (xpp.name != null) {
                            lateStops += 1
                            totalLateness += xpp.text.toInt()
                        //}
                    }
                    if (xpp.name == "Direction"){
                        xpp.next()
                        if(xpp.text == routeDirection){
                            selectedRoute = true
                        }
//                        else{
//                            selectedRoute = false
//                        }
                    }
                }

                /**
                 * Parse through xml response and pull data from select tags
                 */

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
                            delay = xpp.text
                            lateStops += 1
                            totalLateness += xpp.text.toInt()

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

            /**
             * Calculate average schedule adjustment time
             */
            if (lateStops != 0){
                if (totalLateness != 0){
                    average = totalLateness / lateStops
                }
            }

            return "Done"
        }

        /**
         * Take route data and send to showRouteDetails function below
         */

        override fun onPostExecute(result: String?) {

            var routeData = Bundle()
            routeData.putString("RouteNumber", routeNumber)
            routeData.putString("RouteName", routeName)
            routeData.putString("Destination", destination)
            routeData.putString("Location", "Lat: $latitude Long: $longitude")
            routeData.putString("Speed", speed)
            routeData.putString("StartTime", startTime)
            routeData.putString("Delay", delay)
            routeData.putString("Average", average.toString())

            showRouteDetails(routeData)


        }
    }

    /**
     *  Function that launches OCTranspoRouteDetails activity
     */

    fun showRouteDetails(data: Bundle){
        var detailActivity = Intent(this, OCTranspoRouteDetails::class.java)
        detailActivity.putExtras(data)
        startActivityForResult(detailActivity, 35)
    }

    // LIST VIEW STUFF //

    /**
     *  List View adapter
     */

    inner class MyAdapter(ctx : Context) : ArrayAdapter<String>(ctx, 0 ) {

        override fun getCount(): Int {
            return routeFullName.size
        }

        override fun getItem(position: Int): String? {
            return routeFullName.get(position)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var inflater = LayoutInflater.from(parent.context)

            var result = inflater.inflate(R.layout.oc_busname, null)

            val setRouteNumber = result.findViewById(R.id.routeName) as TextView
            setRouteNumber.setText(getItem(position))

            return result
        }

        override fun getItemId(position: Int): Long {
            return 0
        }
    }
}

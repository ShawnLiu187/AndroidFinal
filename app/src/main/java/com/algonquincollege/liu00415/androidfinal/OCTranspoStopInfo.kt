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

    // INIT STUFF //

    var routeNumberList = ArrayList<String>()
    var routeNameList = ArrayList<String>()
    var routeFullName = ArrayList<String>()
    lateinit var stopAdapter: MyAdapter
    lateinit var oc_progressBar: ProgressBar
    lateinit var stopName: String
    lateinit var saveStop: Button
    lateinit var stopNameTextView: TextView


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

        saveStop = findViewById(R.id.saveStop)
        saveStop.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra("stopNumber", stopNumber)
            resultIntent.putExtra("stopName", stopName)

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    // QUERY STUFF //


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
                    if (xpp.name == "StopDescription"){
                        xpp.next()
                        stopName = xpp.text
                    }
                }
                xpp.next()
            }

            progress += 25
            publishProgress()

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

        override fun onPostExecute(result: String?) {
            stopAdapter.notifyDataSetChanged()
            oc_progressBar.visibility = View.INVISIBLE
            saveStop.visibility = View.VISIBLE
            stopNameTextView.visibility = View.VISIBLE
            stopNameTextView.text = stopName

        }

    }

    // LIST VIEW STUFF //

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

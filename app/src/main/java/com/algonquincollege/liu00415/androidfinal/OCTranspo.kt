package com.algonquincollege.liu00415.androidfinal

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.HttpURLConnection
import java.net.URL


class OCTranspo : AppCompatActivity() {


    var stopList = ArrayList<String>()
    var stopPosition = 0
    lateinit var stopAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_octranspo)

        // DISPLAY LIST OF STOPS FROM DATABASE

        val dbHelper = MyOpenHelper()
        val db = dbHelper.writableDatabase
        val results = db.query( TABLE_NAME, arrayOf("_id", "stop_number", "stop_name"), null, null, null, null, null, null)

        results.moveToFirst()
        val stopIdIndex = results.getColumnIndex("_id")
        val stopNumberIndex = results.getColumnIndex("stop_number")
        val stopNameIndex = results.getColumnIndex("stop_name")

        while (!results.isAfterLast())
        {
            var stopNumber = results.getString(stopNumberIndex)
            var stopName = results.getString(stopNameIndex)
            stopList.add(stopNumber + " - " + stopName)

            results.moveToNext() //go to next row in table
        }


        // ADD OR VIEW A STOP

        val getStopActivity = Intent(this, OCTranspoStopInfo::class.java)

        val stopListView = findViewById<ListView>(R.id.stopListView)
        stopAdapter = MyAdapter(this)

        stopListView?.setAdapter(stopAdapter)

        stopListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, View, position, id ->

            stopPosition = position

            val dbHelper = MyOpenHelper()
            val db = dbHelper.writableDatabase
            val results = db.query( TABLE_NAME, arrayOf("_id", "stop_number", "stop_name"), null,null,null,null,null,null)

            //val showContactActivity = Intent(this, OCTranspoStopInfo::class.java)

            results.moveToFirst()

            val pos = position + 1
            var id = 1

            while (!results.isAfterLast())
            {
                //val id = results.getInt(0)


                if (id === pos)
                {
                    getStopActivity.putExtra("stopNumber", results.getString(stopNumberIndex))
                    getStopActivity.putExtra("stopSaved", "true")
                    getStopActivity.putExtra("id", results.getString(stopIdIndex))
                    break
                }

                id = id + 1

                results.moveToNext()
            }

            startActivityForResult(getStopActivity,69)
        }



        var addStop = findViewById<Button>(R.id.addStop)
        addStop.setOnClickListener{
            val addButton = findViewById<EditText>(R.id.stopNumber)
            val stopNumber = addButton.text
            if (stopNumber.toString() != ""){
                getStopActivity.putExtra("stopNumber", stopNumber)
                getStopActivity.putExtra("stopSaved", "false")
                startActivityForResult(getStopActivity, 420)
            }else{
                Toast.makeText(this, "Please enter a stop number", Toast.LENGTH_SHORT).show()
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){

            if (requestCode == 420){

                var savedStopName = data?.getStringExtra("stopName")
                var savedStopNumber = data?.getStringExtra("stopNumber")

                val dbHelper = MyOpenHelper()
                val db = dbHelper.writableDatabase

                val newStop = ContentValues()
                newStop.put("stop_number", savedStopNumber)
                newStop.put("stop_name", savedStopName)
                db.insert(TABLE_NAME, "", newStop)

                stopList.add("$savedStopNumber - $savedStopName")
                stopAdapter.notifyDataSetChanged()

                Snackbar.make(findViewById(R.id.addStop),"Added Stop: $savedStopNumber $savedStopName",Snackbar.LENGTH_SHORT).show()
            }

            if (requestCode == 69){

                val dbHelper = MyOpenHelper()
                val db = dbHelper.writableDatabase
                db.delete(TABLE_NAME, "_id=${data?.getStringExtra("id")}.", null)
                stopList.removeAt(stopPosition)
                stopAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Removed ${data?.getStringExtra("fullname")}", Toast.LENGTH_LONG).show()
            }


        }

    }


    // DATABASE STUFF //


    val DATABASE_NAME = "StopNumbers.db"
    val VERSION_NUM = 4
    val TABLE_NAME = "StopNumbers"

    inner class MyOpenHelper: SQLiteOpenHelper(this@OCTranspo, DATABASE_NAME, null, VERSION_NUM){

        override fun onCreate(db: SQLiteDatabase){

            db.execSQL("CREATE TABLE $TABLE_NAME ( _id INTEGER PRIMARY KEY AUTOINCREMENT, stop_number Text, stop_name Text)")

        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")

            onCreate(db)

        }

    }


    // LIST VIEW STUFF

    inner class MyAdapter(ctx : Context) : ArrayAdapter<String>(ctx, 0 ) {

        override fun getCount(): Int {
            return stopList.size
        }

        override fun getItem(position: Int): String? {
            return stopList.get(position)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var inflater = LayoutInflater.from(parent.getContext())

            var result = inflater.inflate(R.layout.oc_stopname, null)

            val thisText = result.findViewById(R.id.stopName) as TextView
            thisText.setText(getItem(position))

            return result
        }

        override fun getItemId(position: Int): Long {
            return 0
        }
    }
}

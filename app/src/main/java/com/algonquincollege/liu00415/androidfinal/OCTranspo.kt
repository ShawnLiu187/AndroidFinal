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
    lateinit var stopAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_octranspo)

        val stopListView = findViewById<ListView>(R.id.stopListView)
        stopAdapter = MyAdapter(this)

        stopListView?.setAdapter(stopAdapter)

        val getStopActivity = Intent(this, OCTranspoStopInfo::class.java)

        var addStop = findViewById<Button>(R.id.addStop)
        addStop.setOnClickListener{
            val stopNumber = findViewById<EditText>(R.id.stopNumber)
            getStopActivity.putExtra("stopNumber", stopNumber.text)
            startActivityForResult(getStopActivity, 420)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){

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

        }

    }


    // DATABASE STUFF //


    val DATABASE_NAME = "StopNumbers.db"
    val VERSION_NUM = 1
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

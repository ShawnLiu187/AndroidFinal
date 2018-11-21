package com.algonquincollege.liu00415.androidfinal

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

//progress bar
//snackbar
//custom dialog notificaiton

class OCTranspo : Activity() {


    var stopList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_octranspo)

        stopList.add("Baseline") //3017
        stopList.add("Barrhaven Centre") //3045
        stopList.add("Rideau") //3009
        stopList.add("Bayshore") //3050

        val stopListView = findViewById<ListView>(R.id.stopListView)
        val stopAdapter = MyAdapter(this)

        stopListView?.setAdapter(stopAdapter)

        Toast.makeText(this, "OC Transpo App, David McCreath", Toast.LENGTH_LONG).show()

//        val lrtUpdate = findViewById<Button>(R.id.lrt_update)
//        lrtUpdate.setOnClickListener{
//            var lrtMessage = layoutInflater.inflate(R.layout.lrt_message, null)
//
//            var builder = AlertDialog.Builder(this)
//            //builder.setTitle("Question")
//            builder.setView(lrtMessage)
//            builder.setPositiveButton("okay", { dialog, id ->
//            })
//            builder.setNegativeButton("okay", { dialog, id ->
//            });
//            var dialog = builder.create()
//            dialog.show()
//        }

//        var addStop = findViewById<Button>(R.id.addStop)
//        addStop.setOnClickListener{
//            Snackbar.make(addStop, "Stop Added", Snackbar.LENGTH_SHORT).show()
//        }

    }


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

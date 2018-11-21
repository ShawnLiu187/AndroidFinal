package com.algonquincollege.liu00415.androidfinal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button


import com.example.tylercrozman.tylerfinalportion.MovieMain
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var busPortal = findViewById<Button>(R.id.busPortal)
        busPortal.setOnClickListener{
            var busActivity = Intent(this, OCTranspo::class.java)
            startActivity(busActivity)

        moviePortal.setOnClickListener {
            var destinationFilm = Intent(this, MovieMain::class.java)
            startActivity(destinationFilm)


        }
    }
}

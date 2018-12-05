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


        val foodButton = findViewById<Button>(R.id.foodPortal)
        foodButton.setOnClickListener {
            val foodList = Intent(this, foodList::class.java)
            startActivity(foodList)
        }

        val newsButton = findViewById<Button>(R.id.newsPortal)
        newsButton.setOnClickListener {
            val newsList = Intent(this, newsList::class.java)
            startActivity(newsList)
        }


        var busPortal = findViewById<Button>(R.id.busPortal)
        busPortal.setOnClickListener {
            var busActivity = Intent(this, OCTranspo::class.java)
            startActivity(busActivity)

            val moviePortal = findViewById<Button>(R.id.moviePortal)
            moviePortal.setOnClickListener {
                var destinationFilm = Intent(this, MovieMain::class.java)
                startActivity(destinationFilm)


            }

        }
    }
}
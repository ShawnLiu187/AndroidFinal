package com.algonquincollege.liu00415.androidfinal


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast


class foodList : AppCompatActivity() {

    lateinit var searchButton: Button

    var snackMessage = "Searching for something"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

    Toast.makeText(this, "Welcome to Food Portal", Toast.LENGTH_LONG).show()

        searchButton = findViewById(R.id.sendButton)
        searchButton.setOnClickListener{
            Snackbar.make(searchButton, snackMessage, Snackbar.LENGTH_LONG)
                    .setAction("Undo", {
                        e -> Toast.makeText(this@foodList, "DoSomething", Toast.LENGTH_LONG).show()
                    })
                    .show()
        }
    }
}

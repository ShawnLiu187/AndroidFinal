package com.example.tylercrozman.tylerfinalportion

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.algonquincollege.liu00415.androidfinal.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.movie_cell.*
import kotlinx.android.synthetic.main.movie_main.*

class MovieMain : AppCompatActivity() {

    //////////////////// Movie Information... I feel like a database will make this easier.
    var movieTitles = ArrayList<String>()
    var movieYears = ArrayList<String>()
    var movieRated = ArrayList<String>()
    var movieReleased = ArrayList<String>()
    var movieRuntime = ArrayList<String>()
    var movieGenre = ArrayList<String>()
    var movieDirector = ArrayList<String>()
    var movieWriter = ArrayList<String>()
    var movieActors = ArrayList<String>()
    var moviePlot = ArrayList<String>()
    var movieLanguage = ArrayList<String>()
    var movieCountry = ArrayList<String>()
    var movieAwards = ArrayList<String>()
    var movieMetaScore = ArrayList<String>()
    var movieimdbRating = ArrayList<String>()
    var imdbVotes = ArrayList<String>()
    var imdbID = ArrayList<String>()
    var movieType = ArrayList<String>()
    var moviePoster = ArrayList<Int>()


    ///////////////////// Location in the arrays of the selected item
    //////////// Using it on startup to tell array size.
    var MovieIndex = movieTitles.size

    /////////////// Array Adapter variable name
    lateinit var myAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_main)

var greetToastText = ""
        if (movieTitles.isNotEmpty()) {
            greetToastText = "You have $MovieIndex movies saved "
        } else
        {
            greetToastText = "You have no movies saved "}
            Toast.makeText(this, greetToastText, Toast.LENGTH_LONG).show()

/// Search bar button click listener. Reads the edit text field's input and starts the next activity with that
        Search.setOnClickListener{
            var userInput = MovieSearch.text.toString()
            var destination = Intent(this, MovieDetails::class.java)
            destination.putExtra("Query", userInput)
            startActivityForResult(destination, 404)
        }

        myAdapter = ListMaker(this)
        list.setAdapter(myAdapter)
    }






    /////////////////// List adapter as done in the chat window lab
    inner class ListMaker(ctxt: Context) : ArrayAdapter<String>(ctxt, 0) {
        override fun getCount(): Int{
            return movieTitles.size
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            //////////// makes each list item into a cell based off of the prototype xml, in this case movie_cell
            var inflater = LayoutInflater.from( parent.getContext() )
            var result = null as View?
                result = inflater.inflate(R.layout.movie_cell, null)
            Title.text = movieTitles.get(position)
            Poster.setImageResource(moviePoster.get(position))

            result.setOnClickListener {

                //                //// Contact information to pass
                var info = ArrayList<String>()

                info.add(movieTitles.get(position))
                info.add(movieYears.get(position))
                info.add(movieRated.get(position))
                info.add(movieReleased .get(position))
                info.add(movieRuntime.get(position))
                info.add(movieGenre.get(position))
                info.add(movieDirector .get(position))
                info.add(movieWriter.get(position))
                info.add(movieActors.get(position))
                info.add(moviePlot.get(position))
                info.add(movieLanguage .get(position))
                info.add(movieCountry.get(position))
                info.add(movieAwards.get(position))
                info.add(movieMetaScore.get(position))
                info.add(movieimdbRating.get(position))
                info.add(imdbVotes.get(position))
                info.add(imdbID.get(position))
                info.add(movieType.get(position))
                info.add(moviePoster.get(position).toString())
//              ///// Go to Details page with info
                goTo(info)
            }

            return result
        }
        override fun getItemId(position: Int): Long{
            MovieIndex = position
            return 1
        }
    }
 fun goTo(info: ArrayList<String>){

     var whereTo = Intent(this, MovieDetails::class.java)
     intent.putExtra("Information", info)
     startActivity(intent)
 }
    ////////////// Save Movie from Details

//    override fun onActivityResult(requestCode: Int, responseCode: Int, data: Intent) {
//        if (requestCode == 404 && responseCode == Activity.RESULT_OK) {
//        }
//    }
//
//        }
}

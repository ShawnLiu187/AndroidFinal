package com.example.tylercrozman.tylerfinalportion

import android.app.Activity
import android.os.Bundle
import com.algonquincollege.liu00415.androidfinal.R

class SavedMovieDetails : Activity() {
    var movieTitles = ""
    var movieYears = ""
    var movieRated = ""
    var movieReleased = ""
    var movieRuntime = ""
    var movieGenre = ""
    var movieDirector = ""
    var movieWriter = ""
    var movieActors = ""
    var moviePlot = ""
    var movieLanguage = ""
    var movieCountry = ""
    var movieAwards = ""
    var movieMetaScore = ""
    var movieimdbRating= ""
    var imdbVotes = ""
    var imdbID = ""
    var movieType = ""
    var moviePoster = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_movie_details)

        var messagePassed = intent.extras.get("Information") as ArrayList<String>
        movieTitles = messagePassed.get(0)
        movieYears = messagePassed.get(1)
        movieRated = messagePassed.get(2)
        movieReleased = messagePassed.get(3)
        movieRuntime = messagePassed.get(4)
        movieGenre = messagePassed.get(5)
        movieDirector = messagePassed.get(6)
        movieWriter = messagePassed.get(7)
        movieActors = messagePassed.get(8)
        moviePlot = messagePassed.get(9)
        movieLanguage = messagePassed.get(10)
        movieCountry = messagePassed.get(11)
        movieAwards = messagePassed.get(12)
        movieMetaScore = messagePassed.get(13)
        movieimdbRating= messagePassed.get(14)
        imdbVotes = messagePassed.get(15)
        imdbID = messagePassed.get(16)
        movieType = messagePassed.get(17)
        moviePoster = messagePassed.get(18)

    }
}

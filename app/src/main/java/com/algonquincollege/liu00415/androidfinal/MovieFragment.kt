package com.algonquincollege.liu00415.androidfinal


import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.tylercrozman.tylerfinalportion.MovieMain
//import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.movie_details.*
import kotlinx.android.synthetic.main.movie_main.*
import org.w3c.dom.Document
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MovieFragment : Fragment() {
    var isTablet = false

    lateinit var transferredData: ArrayList<String>
    lateinit var FetchOrSaved: String
    lateinit var UserInput: String
    var ID: Long = 0





    lateinit var parentDocument: MovieMain

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var screen = inflater.inflate(R.layout.movie_details, container, false)




        var Loading = screen.findViewById<ProgressBar>(R.id.Loading)

        var Title = screen.findViewById<TextView>(R.id.Title)
        var MovieType = screen.findViewById<TextView>(R.id.MovieType)
        var Rated = screen.findViewById<TextView>(R.id.Rated)
        var Released = screen.findViewById<TextView>(R.id.Released)
        var Runtime = screen.findViewById<TextView>(R.id.Runtime)
        var Genre = screen.findViewById<TextView>(R.id.Genre)
        var Director = screen.findViewById<TextView>(R.id.Director)
        var Writer = screen.findViewById<TextView>(R.id.Writer)
        var Actors = screen.findViewById<TextView>(R.id.Actors)
        var Plot = screen.findViewById<TextView>(R.id.Plot)
        var Language = screen.findViewById<TextView>(R.id.Language)
        var Country = screen.findViewById<TextView>(R.id.Country)
        var Awards = screen.findViewById<TextView>(R.id.Awards)
        var MetaScore = screen.findViewById<TextView>(R.id.MetaScore)
        var imdbRating = screen.findViewById<TextView>(R.id.imdbRating)
        var imdbVotes = screen.findViewById<TextView>(R.id.imdbVotes)
        var imdbID = screen.findViewById<TextView>(R.id.imdbID)
        var Poster = screen.findViewById<ImageView>(R.id.Poster)


        var DeleteMovieData = screen.findViewById<Button>(R.id.DeleteMovieData)
        var SaveMovieData = screen.findViewById<Button>(R.id.SaveMovieData)



        if(FetchOrSaved == "Saved"){

        Title.text =     getString(R.string.MovieTitle)     +transferredData[0]
        MovieType.text = getString(R.string.MovieType)      +transferredData[11]
        Rated.text =     getString(R.string.MovieRated)     +transferredData[8]
        Released.text =  getString(R.string.MovieReleased)  +transferredData[1]
        Runtime.text =   getString(R.string.MovieRuntime)   +transferredData[6]
        Genre.text =     getString(R.string.MovieGenre)     +transferredData[7]
        Director.text =  getString(R.string.Director)       +transferredData[13]
        Writer.text =    getString(R.string.MovieWriter)    +transferredData[12]
        Actors.text =    getString(R.string.MovieActors)    +transferredData[15]
        Plot.text =      getString(R.string.MoviePlot)      +transferredData[14]
        Language.text =  getString(R.string.MovieLanguage)  +transferredData[9]
        Country.text =   getString(R.string.MovieCountry)   +transferredData[10]
        Awards.text =    getString(R.string.MovieAwards)    +transferredData[16]
        MetaScore.text = getString(R.string.MovieMetascore) +transferredData[2]
        imdbRating.text =getString(R.string.imdbRating)     +transferredData[4]
        imdbVotes.text = getString(R.string.imdbVotes)      +transferredData[5]
        imdbID.text =    getString(R.string.imdbId)         +transferredData[3]


            var fis: FileInputStream? = null
            try {    fis = parentDocument.openFileInput(transferredData[0])   }
            catch (e: FileNotFoundException) {    e.printStackTrace()  }
            var ActivityMoviePoster = BitmapFactory.decodeStream(fis)

            Poster.setImageBitmap(ActivityMoviePoster)



        DeleteMovieData.setOnClickListener {
            parentDocument.deleteMessage(ID.toLong())
            parentDocument.fragmentManager.beginTransaction().remove(this).commit()
        }
        SaveMovieData.setOnClickListener {
            parentDocument.fragmentManager.beginTransaction().remove(this).commit()
        }
    }


    if(FetchOrSaved == "Fetch"){

        Loading.visibility = View.VISIBLE

        DeleteMovieData.text = "Back"
        SaveMovieData.text = "Save"

        UserInput = parentDocument.MovieSearch.getText().toString()

        var theQuery  = ForecastQuery()
        theQuery.execute()

        DeleteMovieData.setOnClickListener {
            parentDocument.fragmentManager.beginTransaction().remove(this).commit()

        }
        SaveMovieData.setOnClickListener {
            val exhaustiveMovieData = ArrayList<String>()
            exhaustiveMovieData.add(ActivityMovieTitle)
            exhaustiveMovieData.add(ActivityMovieReleased)
            exhaustiveMovieData.add(ActivityMovieMetascore)
            exhaustiveMovieData.add(ActivityimdbId)
            exhaustiveMovieData.add(ActivityimdbRating)
            exhaustiveMovieData.add(ActivityimdbVotes)
            exhaustiveMovieData.add(ActivityMovieRuntime)
            exhaustiveMovieData.add(ActivityMovieGenre)
            exhaustiveMovieData.add(ActivityMovieRated)
            exhaustiveMovieData.add(ActivityMovieLanguage)
            exhaustiveMovieData.add(ActivityMovieCountry)
            exhaustiveMovieData.add(ActivityMovieType)
            exhaustiveMovieData.add(ActivityMovieWriter)
            exhaustiveMovieData.add(ActivityDirector)
            exhaustiveMovieData.add(ActivityMoviePlot)
            exhaustiveMovieData.add(ActivityMovieActors)
            exhaustiveMovieData.add(ActivityMovieAwards)
            exhaustiveMovieData.add(Year)


            parentDocument.addMessage(exhaustiveMovieData)
            parentDocument.fragmentManager.beginTransaction().remove(this).commit()

        }

    }
        return screen
}


    override fun onAttach(context: Activity?) {
        super.onAttach(context)

            parentDocument = context as MovieMain // need parent for later removing fragment
        }

    lateinit var ActivityMovieTitle : String
    lateinit var ActivityMovieReleased : String
    lateinit var ActivityMovieMetascore: String
    lateinit var ActivityimdbId: String
    lateinit var ActivityimdbRating: String
    lateinit var ActivityimdbVotes: String
    lateinit var ActivityMovieRuntime: String
    lateinit var ActivityMovieGenre: String
    lateinit var ActivityMovieRated: String
    lateinit var ActivityMovieLanguage: String
    lateinit var ActivityMovieCountry: String
    lateinit var ActivityMovieType: String
    lateinit var ActivityMovieWriter: String
    lateinit var ActivityMoviePlot: String
    lateinit var ActivityMovieActors: String
    lateinit var ActivityMovieAwards: String
    lateinit var ActivityDirector: String
    lateinit var Year: String
    lateinit var ActivityMoviePoster : Bitmap



    //  ***************** Fetch Script Be past this point Read at your own RISK   ****************//
    inner class ForecastQuery : AsyncTask<String, Int, String>() {
        var movieTitles : String? = null
        var movieYears : String? = null
        var movieRated : String? = null
        var movieReleased : String? = null
        var movieRuntime : String? = null
        var movieGenre : String? = null
        var movieDirector : String? = null
        var movieWriter : String? = null
        var movieActors : String? = null
        var moviePlot : String? = null
        var movieLanguage : String? = null
        var movieCountry : String? = null
        var movieAwards : String? = null
        var movieMetaScore : String? = null
        var movieimdbRating: String? = null
        var asyncImdbVotes : String? = null
        var asyncImdbID : String? = null
        var movieType : String? = null
        var moviePoster: String? = null
        var progress = 0


        fun getImage(url: URL): Bitmap? {
            var connection: HttpURLConnection? = null
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val responseCode = connection.responseCode
                return if (responseCode == 200) {
                    BitmapFactory.decodeStream(connection.inputStream)
                } else
                    null
            } catch (e: Exception) {
                return null
            } finally {
                connection?.disconnect()
            }
        }

        fun getImage(urlString: String): Bitmap? {
            try {
                val url = URL(urlString)
                return getImage(url)
            } catch (e: MalformedURLException) {
                return null
            }

        }

        fun fileExistance(fname : String):Boolean{
            val file = getActivity().getFileStreamPath(fname)
            return file.exists()   }



        override fun doInBackground(vararg params: String?): String {
            val url = URL("http://www.omdbapi.com/?apikey=6c9862c2&r=xml&t=$UserInput")
            var connection = url.openConnection() as HttpURLConnection
            var response = connection.getInputStream()

            val factory = XmlPullParserFactory.newInstance()
            factory.setNamespaceAware(false)
            val xpp = factory.newPullParser()
            xpp.setInput(response, "UTF-8")

            while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
                when (xpp.eventType) {
                    XmlPullParser.START_TAG -> {
                        if (xpp.name == "movie") {
                            movieTitles = xpp.getAttributeValue(null, "title")
                            movieYears = xpp.getAttributeValue(null, "year")
                            movieRated = xpp.getAttributeValue(null, "rated")
                            movieReleased = xpp.getAttributeValue(null, "released")
                            movieRuntime = xpp.getAttributeValue(null, "runtime")
                            movieGenre = xpp.getAttributeValue(null, "genre")
                            movieDirector = xpp.getAttributeValue(null, "director")
                            movieWriter = xpp.getAttributeValue(null, "writer")
                            movieActors = xpp.getAttributeValue(null, "actors")
                            moviePlot = xpp.getAttributeValue(null, "plot")
                            movieLanguage = xpp.getAttributeValue(null, "language")
                            movieCountry = xpp.getAttributeValue(null, "country")
                            movieAwards = xpp.getAttributeValue(null, "awards")
                            movieMetaScore = xpp.getAttributeValue(null, "metascore")
                            movieimdbRating = xpp.getAttributeValue(null, "imdbRating")
                            asyncImdbVotes = xpp.getAttributeValue(null, "imdbVotes")
                            asyncImdbID = xpp.getAttributeValue(null, "imdbID")
                            movieType = xpp.getAttributeValue(null, "type")
                            moviePoster = xpp.getAttributeValue(null, "poster").toString()
                            progress += 100


                            if(fileExistance("$movieTitles")){

                                var fis: FileInputStream? = null
                                try {    fis = parentDocument.openFileInput("$movieTitles")   }
                                catch (e: FileNotFoundException) {    e.printStackTrace()  }
                                ActivityMoviePoster = BitmapFactory.decodeStream(fis)

                            }
                            else {
                                var MovieUrlly = moviePoster
                                ActivityMoviePoster = getImage(MovieUrlly!!)!!
                                val outputStream = parentDocument.openFileOutput("$movieTitles", Context.MODE_PRIVATE)
                                ActivityMoviePoster?.compress((Bitmap.CompressFormat.PNG), 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                            }


                        }
                        publishProgress()
                    }
                    XmlPullParser.TEXT -> {   }


                }
                xpp.next()  // go to next xml element


            }
            return "Meow"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            Loading.setProgress(progress)
            Title.text =        getString(R.string.MovieTitle)      + movieTitles
            Released.text =     getString(R.string.MovieReleased  ) + movieReleased
            MetaScore.text =    getString(R.string.MovieMetascore ) + movieMetaScore
            imdbID.text =       getString(R.string.imdbId         ) + asyncImdbID
            imdbRating.text =   getString(R.string.imdbRating     ) + movieimdbRating
            imdbVotes.text =    getString(R.string.imdbVotes      ) + asyncImdbVotes
            Runtime.text =      getString(R.string.MovieRuntime   ) + movieRuntime
            Genre.text =        getString(R.string.MovieGenre     ) + movieGenre
            Rated.text =        getString(R.string.MovieRated     ) + movieRated
            Language.text =     getString(R.string.MovieLanguage  ) + movieLanguage
            Country.text =      getString(R.string.MovieCountry   ) + movieCountry
            MovieType.text =    getString(R.string.MovieType      ) + movieType
            Writer.text =       getString(R.string.MovieWriter    ) + movieWriter
            Director.text =     getString(R.string.Director       ) + movieDirector
            Plot.text =         getString(R.string.MoviePlot      ) + moviePlot
            Actors.text =       getString(R.string.MovieActors    ) + movieActors
            Awards.text =       getString(R.string.MovieAwards    ) + movieAwards


            ActivityMovieTitle      = movieTitles.toString()
            ActivityMovieReleased   = movieReleased.toString()
            ActivityMovieMetascore  = movieMetaScore.toString()
            ActivityimdbId          = asyncImdbID.toString()
            ActivityimdbRating      = movieimdbRating.toString()
            ActivityimdbVotes       = asyncImdbVotes.toString()
            ActivityMovieRuntime    = movieRuntime.toString()
            ActivityMovieGenre      = movieGenre.toString()
            ActivityMovieRated      = movieRated.toString()
            ActivityMovieLanguage   = movieLanguage.toString()
            ActivityMovieCountry    = movieCountry.toString()
            ActivityMovieType       = movieType.toString()
            ActivityMovieWriter     = movieWriter.toString()
            ActivityDirector        = movieDirector.toString()
            ActivityMoviePlot       = moviePlot.toString()
            ActivityMovieActors     = movieActors.toString()
            ActivityMovieAwards     = movieAwards.toString()
            Year                    = movieYears.toString()


        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Loading.visibility = View.INVISIBLE
            Poster.setImageBitmap(ActivityMoviePoster)
        }

    }


    }

package com.example.tylercrozman.tylerfinalportion

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.algonquincollege.liu00415.androidfinal.MovieFragment
import com.algonquincollege.liu00415.androidfinal.R
import kotlinx.android.synthetic.main.movie_details.*
import kotlinx.android.synthetic.main.movie_main.*
import java.io.FileInputStream
import java.io.FileNotFoundException

class MovieMain : AppCompatActivity() {

    val DATABASE_NAME = "Movie.db"
    val VERSION_NUM = 6
    val TABLE_NAME = "SavedMovies"
    val KEY_TITLE = "Title"
    var movieList = ArrayList<String>()

    lateinit var  db: SQLiteDatabase
    lateinit var results: Cursor
    lateinit var dbHelper : ChatDatabaseHelper
    lateinit var myAdapter: ArrayAdapter<String>
    var  MoviePosition = 0

    // var myList = null as ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_main)
        ///////////// Database read link followed by get a writable database
        dbHelper = ChatDatabaseHelper(); //Inner Class
        db = dbHelper.writableDatabase


        /////////////// SQL Query
        results = db.query(TABLE_NAME, arrayOf("_id", KEY_TITLE), null, null, null, null, null, null)


        results.moveToFirst()  //moves pointer to first row of results


        ////////////////////// Array Looper
        val movieIndex = results.getColumnIndex(KEY_TITLE)

        while(!results.isAfterLast()){
            var thisMessage = results.getString(movieIndex)
            movieList.add(thisMessage)

            results.moveToNext()
        }


        Log.i("MovieWindow", "Cursor's column count=" + results.getColumnCount())
        for(i in 0..(results.getColumnCount()-1)) {
            var columnName = results.getColumnName(i)
            Log.i("MovieWindow", "Column $columnName ")

        }
        //////////////


        var isTablet = fragment_location != null ///// Important! Set this after the setContentView line!!!


        var myList = list
        var searchBarText = MovieSearch

        myAdapter = MyAdapter(this)

        //////////////////////////////////////////////////// New event listener for lab  7
        myList.setOnItemClickListener { parent, view, position, id ->
            var dataToPass = Bundle()
            dataToPass.putString("DataSource", "Saved")
            dataToPass.putLong("ID", id)



            MoviePosition = position


            var info = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE _id = $id", null)
            info.moveToFirst()

            var ExtraList = ArrayList<String>()
            for(i in 1..(info.getColumnCount()-1)) {
                ExtraList.add(info.getString(i))

            }

            if (isTablet)
            {
                var newFragment = MovieFragment()

                newFragment.arguments = dataToPass // send Bundle to fragment

                newFragment.isTablet = true
                newFragment.ID = id
                newFragment.transferredData = ExtraList
                newFragment.FetchOrSaved = "Saved"

                var transition = getFragmentManager().beginTransaction()  //how to load fragment
                transition.replace(R.id.fragment_location, newFragment) //Where to load fragment
                transition.commit()
            }
            else
            {



                var detailActivity = Intent(this, MovieDetails::class.java)

                detailActivity.putExtras(dataToPass)
                detailActivity.putExtra("Info", ExtraList)


                startActivityForResult(detailActivity, 35);


            }
        }

////////
        myList.setAdapter(myAdapter)
        var srch = findViewById<ImageButton>(R.id.Search)
        srch.setOnClickListener{

            var userTyped = searchBarText.getText().toString()

            if (isTablet)
            {
                var newFragment = MovieFragment()


                newFragment.UserInput = userTyped
                newFragment.FetchOrSaved = "Fetch"

                var transition = getFragmentManager().beginTransaction()  //how to load fragment
                transition.replace(R.id.fragment_location, newFragment) //Where to load fragment
                transition.commit()
            }
            else
            {


                var intent = Intent(this, MovieDetails::class.java)
                intent.putExtra("information", userTyped)
                intent.putExtra("DataSource", "Fetch")
                startActivityForResult(intent, 404)


            }
        }


        StatS.setOnClickListener {
            var MovieRuntimeTotal = 0
            var ShortestMovie = 5000
            var ShortestName = "300 times 10"
            var LongestMovie = 0
            var LongestName = "Now"

            var TotalAges = 0
            var Newest = 0
            var NewestName = "300 times 10"
            var Oldest = 3000
            var OldestName = "Now"


            var Statistic = db.rawQuery("SELECT $KEY_TITLE, MovieRuntime, Year FROM $TABLE_NAME", null)
            Statistic.moveToFirst()


//
            for(i in 1..(Statistic.count)) {
                var CurrentMovie = Statistic.getString(0)
                var CurrentRuntime = Statistic.getString(1).dropLast(4)
                var CurrentYear = Statistic.getString(2)

                if (CurrentYear.toInt() > Newest){
                    Newest = CurrentYear.toInt()
                    NewestName = CurrentMovie.toString()
                }
                if (CurrentYear.toInt() < Oldest){
                    Oldest = CurrentYear.toInt()
                    OldestName = CurrentMovie.toString()
                }
                if(CurrentRuntime.toInt() > LongestMovie){
                    LongestMovie = CurrentRuntime.toInt()
                    LongestName = CurrentMovie
                }
                if(CurrentRuntime.toInt() < ShortestMovie){
                    ShortestMovie = CurrentRuntime.toInt()
                    ShortestName = CurrentMovie
                }
                MovieRuntimeTotal += CurrentRuntime.toInt()
                TotalAges += CurrentYear.toInt()

                Statistic.moveToNext()
            }

            MovieRuntimeTotal = MovieRuntimeTotal/(Statistic.count)
            TotalAges = TotalAges/(Statistic.count)



            var dialogStuff = layoutInflater.inflate(R.layout.movie_stats, null)

            var builder = AlertDialog.Builder(this);
            builder.setTitle("Statistics")
            builder.setView(dialogStuff) // insert view into dialog

            var Longest = dialogStuff.findViewById<TextView>(R.id.MovieLong)
            Longest.text = LongestName
            var LongestTime = dialogStuff.findViewById<TextView>(R.id.MovieLongTime)
            LongestTime.text = LongestMovie.toString()

            var Shortest = dialogStuff.findViewById<TextView>(R.id.MovieShort)
            Shortest.text = ShortestName
            var ShortestTime = dialogStuff.findViewById<TextView>(R.id.MovieShortTime)
            ShortestTime.text = ShortestMovie.toString()

            var AvgTime =  dialogStuff.findViewById<TextView>(R.id.MAvg)
            AvgTime.text = MovieRuntimeTotal.toString()




            var StatsOldest = dialogStuff.findViewById<TextView>(R.id.MovieOldest)
            StatsOldest.text = OldestName
            var OldestDate = dialogStuff.findViewById<TextView>(R.id.MovieOldestTime)
            OldestDate.text = Oldest.toString()

            var StatsNewest = dialogStuff.findViewById<TextView>(R.id.MovieNew)
            StatsNewest.text = NewestName
            var NewestDate = dialogStuff.findViewById<TextView>(R.id.MovieNewTime)
            NewestDate.text = Newest.toString()

            var AvgRelease =  dialogStuff.findViewById<TextView>(R.id.MAvgR)
            AvgRelease.text = TotalAges.toString()




            builder.setNegativeButton( "Ok", {dialog, id->
            });
// Create the AlertDialog
            var dialog = builder.create();
            dialog.show()
//                Snackbar.make(snackBarButton, currentMessage, Snackbar.LENGTH_LONG).show()


        }
    }


    //////////////////////// Database Class
    inner class MyAdapter(ctx : Context) : ArrayAdapter<String>(ctx, 0 ) {

        override fun getCount(): Int{
            return movieList.size
        }
        override fun getItem(position: Int): String {
            return  movieList.get(position)
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var inflater = LayoutInflater.from( parent.getContext() )
            var result = null as View?

                result = inflater.inflate(R.layout.movie_cell, null)


            val thisText = result.findViewById(R.id.Title) as TextView
            thisText.setText(getItem(position))


            var fis: FileInputStream? = null
            try {    fis = openFileInput(getItem(position))   }
            catch (e: FileNotFoundException) {    e.printStackTrace()  }
            var ActivityMoviePoster = BitmapFactory.decodeStream(fis)

            val thisPoster = result.findViewById(R.id.Poster) as ImageView

            thisPoster.setImageBitmap(ActivityMoviePoster)

            return result
        }
        override fun getItemId(position: Int): Long{
            results.moveToPosition(position)
            var index = results.getColumnIndex("_id")
            return results.getInt(index).toLong()
        }
    }

    inner class ChatDatabaseHelper: SQLiteOpenHelper(this@MovieMain, DATABASE_NAME, null, VERSION_NUM){
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL( "CREATE TABLE $TABLE_NAME ( _id INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_TITLE TEXT, MovieReleased TEXT, MovieMetascore TEXT, imdbId TEXT, imdbRating TEXT, imdbVotes TEXT, MovieRuntime TEXT, MovieGenre TEXT, MovieRated TEXT, MovieLanguage TEXT, MovieCountry TEXT, MovieType TEXT, MovieWriter TEXT, Director TEXT, MoviePlot TEXT, MovieActors TEXT, MovieAwards TEXT, Year TEXT)")
            //creates the table

        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") //deletes your old data

            // create new table
            onCreate(db)
        }
    }

    fun deleteMessage(id:Long){
        db.delete(TABLE_NAME, "_id=$id", null)

        results = db.query(TABLE_NAME, arrayOf("_id", KEY_TITLE), null, null, null, null, null, null)
        movieList.removeAt(MoviePosition)
        myAdapter.notifyDataSetChanged()



    }
    fun addMessage(AdditionManifest:ArrayList<String>){

        movieList.add(AdditionManifest[0])
        ///////// Write to database
        val newRow = ContentValues()
        newRow.put(KEY_TITLE, AdditionManifest[0])
        newRow.put("MovieReleased",     AdditionManifest[1])
        newRow.put("MovieMetascore",    AdditionManifest[2])
        newRow.put("imdbId",            AdditionManifest[3])
        newRow.put("imdbRating",        AdditionManifest[4])
        newRow.put("imdbVotes",         AdditionManifest[5])
        newRow.put("MovieRuntime",      AdditionManifest[6])
        newRow.put("MovieGenre",        AdditionManifest[7])
        newRow.put("MovieRated",        AdditionManifest[8])
        newRow.put("MovieLanguage",     AdditionManifest[9])
        newRow.put("MovieCountry",      AdditionManifest[10])
        newRow.put("MovieType",         AdditionManifest[11])
        newRow.put("MovieWriter",       AdditionManifest[12])
        newRow.put("Director",          AdditionManifest[13])
        newRow.put("MoviePlot",         AdditionManifest[14])
        newRow.put("MovieActors",       AdditionManifest[15])
        newRow.put("MovieAwards",       AdditionManifest[16])
        newRow.put("Year", AdditionManifest[17])
        db.insert(TABLE_NAME, "", newRow)
        results = db.query(TABLE_NAME, arrayOf("_id", KEY_TITLE), null, null, null, null, null, null)
        myAdapter.notifyDataSetChanged()
        MovieSearch.hint = ""


        Snackbar.make(list, "test", Snackbar.LENGTH_LONG)
                .setAction("dismiss", {e-> Toast.makeText( this@MovieMain, "Cool", Toast.LENGTH_SHORT).show()}).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 35 && resultCode == RESULT_OK) {
            var slatedForRemoval = data!!.getLongExtra("ID", 0).toInt()
            db.delete(TABLE_NAME, "_id=$slatedForRemoval", null)

            results = db.query(TABLE_NAME, arrayOf("_id", KEY_TITLE), null, null, null, null, null, null)
            movieList.removeAt(MoviePosition)
            myAdapter.notifyDataSetChanged()

        }
        if (requestCode == 404 && resultCode == RESULT_OK) {
            var AdditionManifest = data!!.getIntegerArrayListExtra("Details") as ArrayList<String>

            movieList.add(AdditionManifest[0])
            ///////// Write to database
            val newRow = ContentValues()
            newRow.put(KEY_TITLE, AdditionManifest[0])
            newRow.put("MovieReleased",     AdditionManifest[1])
            newRow.put("MovieMetascore",    AdditionManifest[2])
            newRow.put("imdbId",            AdditionManifest[3])
            newRow.put("imdbRating",        AdditionManifest[4])
            newRow.put("imdbVotes",         AdditionManifest[5])
            newRow.put("MovieRuntime",      AdditionManifest[6])
            newRow.put("MovieGenre",        AdditionManifest[7])
            newRow.put("MovieRated",        AdditionManifest[8])
            newRow.put("MovieLanguage",     AdditionManifest[9])
            newRow.put("MovieCountry",      AdditionManifest[10])
            newRow.put("MovieType",         AdditionManifest[11])
            newRow.put("MovieWriter",       AdditionManifest[12])
            newRow.put("Director",          AdditionManifest[13])
            newRow.put("MoviePlot",         AdditionManifest[14])
            newRow.put("MovieActors",       AdditionManifest[15])
            newRow.put("MovieAwards",       AdditionManifest[16])
            newRow.put("Year",       AdditionManifest[17])
            db.insert(TABLE_NAME, "", newRow)
            results = db.query(TABLE_NAME, arrayOf("_id", KEY_TITLE), null, null, null, null, null, null)
            myAdapter.notifyDataSetChanged()
            MovieSearch.hint = ""
            Snackbar.make(list, "test", Snackbar.LENGTH_LONG)
                    .setAction("dismiss", {e-> Toast.makeText( this@MovieMain, "Cool", Toast.LENGTH_SHORT).show()}).show()


        }
    }




}

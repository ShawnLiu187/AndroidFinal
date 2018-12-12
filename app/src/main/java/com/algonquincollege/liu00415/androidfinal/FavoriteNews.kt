package com.algonquincollege.liu00415.androidfinal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.*
import com.example.tylercrozman.tylerfinalportion.MovieMain
import kotlin.math.roundToInt

/**
 * FavoriteNews
 *
 * @author  Jordan Morrison
 * @version 1.0
 * @since   2018-12-10
 */

class FavoriteNews : AppCompatActivity() {
    var faveNewsArray = ArrayList<FavouriteStory?>()
    lateinit var faveNewsAdapter: FaveNewsAdapter
    data class FavouriteStory(var title: String?, var author: String?, var date: String?, var link: String?, var imageLink: String?, var description: String?)

    lateinit var dbHelper: FaveNewsDatabaseHelper
    lateinit var db: SQLiteDatabase
    lateinit var results: Cursor

    lateinit var faveNewsListView: ListView

    var newsPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_news)

        faveNewsListView = findViewById<ListView>(R.id.FaveNewsListView)
//        faveProgress = findViewById<ProgressBar>(R.id.progressNewsFave)
//        snackBtn = findViewById<Button>(R.id.snackBarButton)

        var newsToolBar = findViewById<Toolbar>(R.id.FaveNewsList_toolbar)
        setSupportActionBar(newsToolBar)



        dbHelper = FaveNewsDatabaseHelper()  //get a helper object

        db = dbHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_TITLE,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_AUTHOR,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_DATE,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_LINK,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_DESCRIPTION,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_IMAGELINK)

        results = db.query(FavouriteArticleContract.FavArticle.TABLE_NAME, projection, null, null, null, null, null)

        var storyRow: FavouriteStory?
        with(results) {
            while (moveToNext()) {
                storyRow = FavouriteStory(null, null, null, null, null, null)
                storyRow?.title = getString(getColumnIndexOrThrow(FavouriteArticleContract.FavArticle.COLUMN_NAME_TITLE))
                storyRow?.author = getString(getColumnIndexOrThrow(FavouriteArticleContract.FavArticle.COLUMN_NAME_AUTHOR))
                storyRow?.date = getString(getColumnIndexOrThrow(FavouriteArticleContract.FavArticle.COLUMN_NAME_DATE))
                storyRow?.link = getString(getColumnIndexOrThrow(FavouriteArticleContract.FavArticle.COLUMN_NAME_LINK))
                storyRow?.description = getString(getColumnIndexOrThrow(FavouriteArticleContract.FavArticle.COLUMN_NAME_DESCRIPTION))
                storyRow?.imageLink = getString(getColumnIndexOrThrow(FavouriteArticleContract.FavArticle.COLUMN_NAME_IMAGELINK))
                faveNewsArray.add(storyRow!!)
            }
//            results.close()
        }

        faveNewsAdapter = FaveNewsAdapter(this)
        faveNewsListView?.setAdapter(faveNewsAdapter)

        Toast.makeText(this, "Welcome to your Favourite News", Toast.LENGTH_LONG).show()

        faveNewsListView.setOnItemClickListener { parent, view, position, id ->
            newsPosition = position
            var titleTP = faveNewsArray.get(position)?.title
            var authorTP = faveNewsArray.get(position)?.author
            var dateTP = faveNewsArray.get(position)?.date
            var linkTP = faveNewsArray.get(position)?.link
            var descTP = faveNewsArray.get(position)?.description
            var imageTP = faveNewsArray.get(position)?.imageLink

            var dataTP = Bundle()
            dataTP.putString("title", titleTP)
            dataTP.putString("author", authorTP)
            dataTP.putString("date", dateTP)
            dataTP.putString("link", linkTP)
            dataTP.putString("desc", descTP)
            dataTP.putString("image", imageTP)
            dataTP.putLong("id", id)

            var faveDetailActivity = Intent(this, FaveNewsDetails::class.java)
            faveDetailActivity.putExtras(dataTP)
            startActivityForResult(faveDetailActivity, 21)
        }
    }

    inner class FaveNewsAdapter(ctx: Context): ArrayAdapter<FavouriteStory>(ctx, 0) {

        override fun getCount(): Int {
            return faveNewsArray.size
        }

        override fun getItem(position: Int): FavouriteStory? {
            return faveNewsArray.get(position)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var inflater = LayoutInflater.from(parent.getContext())
            var result = null as View?

            result = inflater.inflate(R.layout.news_list_item, null)
            val storyTitle = result.findViewById(R.id.storyListItemTitle) as TextView
            val authorName = result.findViewById(R.id.storyListItemAuthor) as TextView
            val story = getItem(position)
            storyTitle.text = story?.title
            authorName.text = story?.author

            return result
        }

        override fun getItemId(position: Int): Long {
            results.moveToPosition(position)
            return results.getInt(  results.getColumnIndex("_id") ).toLong()
        }
    }

    val DATABASE_NAME = "FavouriteArticles"
    val VERSION_NUM = 6

    object FavouriteArticleContract {
        // Table contents are grouped together in an anonymous object.
        object FavArticle : BaseColumns {
            const val TABLE_NAME = "Articles"
            const val COLUMN_NAME_TITLE = "title"
            const val COLUMN_NAME_AUTHOR = "author"
            const val COLUMN_NAME_DATE = "date"
            const val COLUMN_NAME_LINK = "link"
            const val COLUMN_NAME_DESCRIPTION = "description"
            const val COLUMN_NAME_IMAGELINK = "imageLink"
        }
    }

    private val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${FavouriteArticleContract.FavArticle.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${FavouriteArticleContract.FavArticle.COLUMN_NAME_TITLE} TEXT," +
                    "${FavouriteArticleContract.FavArticle.COLUMN_NAME_AUTHOR} TEXT," +
                    "${FavouriteArticleContract.FavArticle.COLUMN_NAME_DATE} TEXT," +
                    "${FavouriteArticleContract.FavArticle.COLUMN_NAME_LINK} TEXT," +
                    "${FavouriteArticleContract.FavArticle.COLUMN_NAME_DESCRIPTION} TEXT," +
                    "${FavouriteArticleContract.FavArticle.COLUMN_NAME_IMAGELINK} TEXT)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FavouriteArticleContract.FavArticle.TABLE_NAME}"

    inner class FaveNewsDatabaseHelper : SQLiteOpenHelper(this@FavoriteNews, DATABASE_NAME, null, VERSION_NUM) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES) //create the table
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(SQL_DELETE_ENTRIES) //deletes your old data
            //create new table
            onCreate(db)
        }

    }

    fun countArticles()
    {

        var articleCount = faveNewsArray.size
        var articleMessage = "Number of articles: $articleCount"
        Snackbar.make(faveNewsListView, articleMessage, Snackbar.LENGTH_LONG)
                .setAction("Info", {
                    e -> Toast.makeText(this@FavoriteNews, "This is the number of articles saved", Toast.LENGTH_LONG).show()
                })
                .show()
    }
    fun maxWords()
    {
        var maxWords = 0
        for (i in 0..faveNewsArray.size-1)
        {
            var words = faveNewsArray[i]?.description?.split(' ')
            var wordCount = words!!.size
            if(maxWords<wordCount)
            {
                maxWords = wordCount
            }
        }
        var maxWordsMessage = "The max word count of all articles is $maxWords"
        Snackbar.make(faveNewsListView, maxWordsMessage, Snackbar.LENGTH_LONG)
                .setAction("Info", {
                    e -> Toast.makeText(this@FavoriteNews, "This is the max word count of all articles saved", Toast.LENGTH_LONG).show()
                })
                .show()
    }

    fun minWords()
    {
        var minWords = 0
        if (faveNewsArray.size > 0){
            minWords = faveNewsArray[0]?.description?.split(' ')!!.size
            for (i in 1..faveNewsArray.size-1)
            {
                var words = faveNewsArray[i]?.description?.split(' ')
                var wordCount = words!!.size
                if(minWords>wordCount)
                {
                    minWords = wordCount
                }
            }
        }

        var minWordsMessage = "The min word count of all articles is $minWords"
        Snackbar.make(faveNewsListView, minWordsMessage, Snackbar.LENGTH_LONG)
                .setAction("Info", {
                    e -> Toast.makeText(this@FavoriteNews, "This is the min word count of all articles saved", Toast.LENGTH_LONG).show()
                })
                .show()
    }

    fun averageWords()
    {
        var totalWords = 0
        for (i in 0..faveNewsArray.size-1)
        {
            var words = faveNewsArray[i]?.description?.split(' ')!!.size
            totalWords+=words
        }
        var averageWords = (totalWords/faveNewsArray.size)
        var averageMessage = "The average word count of all articles is $averageWords"
        Snackbar.make(faveNewsListView, averageMessage, Snackbar.LENGTH_LONG)
                .setAction("Info", {
                    e -> Toast.makeText(this@FavoriteNews, "This is the average word count of all articles saved", Toast.LENGTH_LONG).show()
                })
                .show()

    }

    fun deleteNews(id:Long)
    {
        val projection = arrayOf(BaseColumns._ID,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_TITLE,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_AUTHOR,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_DATE,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_LINK,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_DESCRIPTION,
                FavouriteArticleContract.FavArticle.COLUMN_NAME_IMAGELINK)

        db.delete(FavouriteArticleContract.FavArticle.TABLE_NAME, "_id=$id", null)

        results = db.query(FavouriteArticleContract.FavArticle.TABLE_NAME, projection, null, null, null, null, null)

        faveNewsArray.removeAt(newsPosition)

        faveNewsAdapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 21 && resultCode == Activity.RESULT_OK)
        {
            deleteNews(data!!.getLongExtra("id", 0))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.news_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.action21 -> {
                val foodList = Intent(this, foodList::class.java)
                startActivity(foodList)
            }
            R.id.action22 -> {
                var busActivity = Intent(this, OCTranspo::class.java)
                startActivity(busActivity)
            }
            R.id.action23 -> {
                var destinationFilm = Intent(this, MovieMain::class.java)
                startActivity(destinationFilm)
            }
            R.id.action24 -> {
                var dialogStuff = layoutInflater.inflate(R.layout.foodlist_dialog, null)
                var editText = dialogStuff.findViewById<EditText>(R.id.commenField)

                var builder = AlertDialog.Builder(this);
                builder.setTitle("How to navigate?")
// Add the buttons
                builder.setView(dialogStuff)

                builder.setPositiveButton("Ok", {dialog, id ->
                    //user clicked OK button
//                    currentMessage = editText.text.toString()
                })
                builder.setNegativeButton("Cancel", {dialog, id ->
                    //user cancelled diaalog
                })
// Create the AlertDialog
                var dialog = builder.create()
                dialog.show()
            }
            R.id.action25 -> {
                countArticles()
            }
            R.id.action26 -> {
                maxWords()
            }
            R.id.action27 -> {
                averageWords()
            }
            R.id.action28 -> {
                minWords()
            }
            R.id.action29 -> {
                val newsList = Intent(this, newsList::class.java)
                startActivity(newsList)
            }

        }
        return true
    }
}

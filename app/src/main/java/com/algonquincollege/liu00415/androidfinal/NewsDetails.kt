package com.algonquincollege.liu00415.androidfinal

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class NewsDetails : AppCompatActivity() {

    var receivedStory = newsList.Story(null, null, null, null, null, null)

    lateinit var db: SQLiteDatabase
    lateinit var dbHelper: ArticleDatabaseHelper
    lateinit var results: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        dbHelper = ArticleDatabaseHelper()
        db = dbHelper.writableDatabase

        //Extracting Detail
        receivedStory.title = intent.getStringExtra("title")
        receivedStory.author = intent.getStringExtra("author")
        receivedStory.date = intent.getStringExtra("date")
        receivedStory.link = intent.getStringExtra("link")
        receivedStory.description = intent.getStringExtra("description")
        receivedStory.imageLink = intent.getStringExtra("imageLink")

        val articleTitleView = findViewById<TextView>(R.id.articleTitle)
        val articleAuthorView = findViewById<TextView>(R.id.authorName)
        val articleDateView = findViewById<TextView>(R.id.articleDate)
        val articleLinkView = findViewById<TextView>(R.id.articleLink)
        val articleDescView = findViewById<TextView>(R.id.articleContent)
        val articleImageView = findViewById<WebView>(R.id.articleImage)

        articleTitleView.text = receivedStory.title
        articleAuthorView.text = receivedStory.author
        articleDateView.text = receivedStory.date
        articleLinkView.text = receivedStory.link
        articleDescView.text = receivedStory.description

        val htmlString = "<img src='${receivedStory.imageLink}'/>"

        articleImageView.loadDataWithBaseURL(null, "<style>img{display: inline;max-height: 100%;max-width: 100%;}</style>" + htmlString, "text/html", "UTF-8", null)

        //Buttons

        val cancelNewsBtn = findViewById<Button>(R.id.cancelNewsBtn)
        cancelNewsBtn.setOnClickListener {
            finish()
        }

        val saveNewsBtn = findViewById<Button>(R.id.saveNewsBtn)
        saveNewsBtn.setOnClickListener {

            var newsFaveIntent = Intent(this, FavoriteNews::class.java)
            startActivity(newsFaveIntent)
            val values = ContentValues().apply {
                put(FavouriteArticleContract.FavArticle.COLUMN_NAME_TITLE, receivedStory.title)
                put(FavouriteArticleContract.FavArticle.COLUMN_NAME_AUTHOR, receivedStory.author)
                put(FavouriteArticleContract.FavArticle.COLUMN_NAME_DATE, receivedStory.date)
                put(FavouriteArticleContract.FavArticle.COLUMN_NAME_LINK, receivedStory.link)
                put(FavouriteArticleContract.FavArticle.COLUMN_NAME_DESCRIPTION, receivedStory.description)
                put(FavouriteArticleContract.FavArticle.COLUMN_NAME_IMAGELINK, receivedStory.imageLink)
            }
            db.insert(FavouriteArticleContract.FavArticle.TABLE_NAME, null, values)
            Toast.makeText(this@NewsDetails, "Added Article to Favourites", Toast.LENGTH_SHORT)
                    .show()
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

    inner class ArticleDatabaseHelper : SQLiteOpenHelper(this@NewsDetails, DATABASE_NAME, null, VERSION_NUM) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES) //create the table
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(SQL_DELETE_ENTRIES) //deletes your old data
            //create new table
            onCreate(db)
        }

    }
}

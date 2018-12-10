package com.algonquincollege.liu00415.androidfinal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.*
import com.example.tylercrozman.tylerfinalportion.MovieMain
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class newsList : AppCompatActivity() {

    lateinit var newsProgressBar: ProgressBar

    var newsArray = ArrayList<Story?>()
    lateinit var newsAdapter: NewsAdapter
    data class Story(var title: String?, var author: String?, var date: String?, var link: String?, var imageLink: String?, var description: String?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        var newsToolBar = findViewById<Toolbar>(R.id.newsList_toolbar)
        setSupportActionBar(newsToolBar)

        Toast.makeText(this, "Welcome to News Portal", Toast.LENGTH_LONG).show()

        var newsListView = findViewById<ListView>(R.id.newsListView)

        newsAdapter = NewsAdapter(this)
        newsListView?.setAdapter(newsAdapter)

        newsListView.setOnItemClickListener { parent, view, position, id ->
            var newsDetailIntent = Intent(this, NewsDetails::class.java)

            newsDetailIntent.putExtra("title",newsArray[position]?.title)
            newsDetailIntent.putExtra("author",newsArray[position]?.author)
            newsDetailIntent.putExtra("date",newsArray[position]?.date)
            newsDetailIntent.putExtra("link",newsArray[position]?.link)
            newsDetailIntent.putExtra("description",newsArray[position]?.description)
            newsDetailIntent.putExtra("imageLink",newsArray[position]?.imageLink)

            startActivity(newsDetailIntent)
        }
        newsProgressBar = findViewById<ProgressBar>(R.id.progressNews)
        newsProgressBar.visibility = View.VISIBLE

        val myQuery = StoryQuery()
        myQuery.execute()
    }

    inner class NewsAdapter(ctx: Context): ArrayAdapter<Story>(ctx, 0) {

        override fun getCount(): Int {
            return newsArray.size
        }

        override fun getItem(position: Int): Story? {
            return newsArray.get(position)
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
            return 0
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class StoryQuery : AsyncTask<String, Int, String>() {
        var story: Story? = null
        var progress = 0
        lateinit var bitmap: Bitmap


        override fun doInBackground(vararg params: String?): String {
            val url = URL("https://www.cbc.ca/cmlink/rss-world")

            val connection = url.openConnection() as HttpURLConnection
            val response = connection.inputStream

            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = false
            val xpp = factory.newPullParser()
            xpp.setInput(response, "UTF-8")
            var eventType = xpp.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                Log.d("PARSING: ", "Event Type: ${xpp.eventType}, Event Name: ${xpp.name}")
                when(eventType) {
                    XmlPullParser.START_TAG -> {
                        if (xpp.name == "item") {
                            Log.d("FOUND ITEM TAG", "Creating Story Object")
                            this.story = Story(null, null,null,null, null, null )
                            progress += 20
                        } else if (this.story != null) {
                            when {
                                xpp.name == "title" -> this.story?.title = xpp.nextText()
                                xpp.name == "link" -> this.story?.link = xpp.nextText()
                                xpp.name == "pubDate" -> this.story?.date = xpp.nextText()
                                xpp.name == "author" -> this.story?.author == xpp.nextText()
                                xpp.name == "description" -> {
                                    this.story?.description = xpp.nextText()
                                    this.story?.imageLink = PatternMatcherGroupHtml.main(story!!.description!!)
                                    this.story?.description = PatternMatcherGroupHtmlText.main(story!!.description!!)
                                }
                            }
                            Log.d("Added Fields: ", "${this.story?.title}")
                            progress += 60
                        }
                        publishProgress()
                    }
                    XmlPullParser.END_TAG -> {
                        if (xpp.name == "item") {
                            newsArray.add(this.story)
                            this.story = null
                        }
                        progress += 20
                    }
                }
                eventType = xpp.next()
            }
            return "Done"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            newsProgressBar.setProgress(progress)
        }

        override fun onPostExecute(result: String?) {
            newsAdapter.notifyDataSetChanged()
            newsProgressBar.visibility = View.INVISIBLE
        }
    }

    object PatternMatcherGroupHtml {

        @JvmStatic
        fun main(arg: String): String {
            val p = Pattern.compile("src='(\\S+)'")
            val m = p.matcher(arg)
            // if we find a match, get the group
            if (m.find()) {
                // get the matching group
                val codeGroup = m.group(1)
                // print the group
                System.out.format("'%s'\n", codeGroup)
                return codeGroup
            }
            return arg
        }
    }
    object PatternMatcherGroupHtmlText {

        @JvmStatic
        fun main(arg: String): String {
            val p = Pattern.compile("<p>(.*?)</p>")
            val m = p.matcher(arg)
            // if we find a match, get the group
            if (m.find()) {
                // get the matching group
                val codeGroup = m.group(1)
                // print the group
                System.out.format("'%s'\n", codeGroup)
                return codeGroup
            }
            return arg
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
        }
        return true
    }
}

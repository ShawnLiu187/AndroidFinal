package com.algonquincollege.liu00415.androidfinal


import android.app.Activity
import android.os.Bundle
//import android.support.v4.app.Fragment
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
/**
 * NewsFragment
 *
 * @author  Jordan Morrison
 * @version 1.1
 * @since   2018-12-10
 */
class NewsFragment : Fragment() {

    lateinit var parentDocument: FavoriteNews

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var dataPassed = arguments

        var title = dataPassed.getString("title")
        var author = dataPassed.getString("author")
        var date = dataPassed.getString("date")
        var link = dataPassed.getString("link")
        var desc = dataPassed.getString("desc")
        var image = dataPassed.getString("image")
        var id = dataPassed.getLong("id")

        var screen = inflater.inflate(R.layout.fragment_news, container, false)

        val faveNewsTitle = screen.findViewById<TextView>(R.id.faveNewsTitle)
        faveNewsTitle.text = title

        val faveNewsAuthor = screen.findViewById<TextView>(R.id.faveNewsAuthor)
        faveNewsAuthor.text = author

        val faveNewsDate = screen.findViewById<TextView>(R.id.faveNewsDate)
        faveNewsDate.text = date

        val faveNewsLink = screen.findViewById<TextView>(R.id.faveNewsLink)
        faveNewsLink.text = link

        val faveNewsContent = screen.findViewById<TextView>(R.id.faveNewsContent)
        faveNewsContent.text = desc

        val faveNewsImage = screen.findViewById<WebView>(R.id.faveNewsImage)
        val htmlString = "<img src='$image'/>"
        faveNewsImage.loadDataWithBaseURL(null, "<style>img{display: inline;max-height: 100%;max-width: 100%;}</style>" + htmlString, "text/html", "UTF-8", null)

        val faveNewsId = screen.findViewById<TextView>(R.id.faveNewsId)
        faveNewsId.text = "Article ID: $id"

        var newsDelBtn = screen.findViewById<Button>(R.id.delFaveNewsBtn)
        newsDelBtn.setOnClickListener {
            var dataBack = Intent()
            dataBack.putExtra("id", id)
            activity?.setResult(Activity.RESULT_OK, dataBack)
            activity?.finish()
        }

        val faveNewsCancelBtn = screen.findViewById<Button>(R.id.cancelFaveNewsBtn)
        faveNewsCancelBtn.setOnClickListener {
            var dataBack = Intent()
            activity?.setResult(Activity.RESULT_CANCELED, dataBack)
            activity?.finish()
        }
        // Inflate the layout for this fragment
        return screen
    }


}

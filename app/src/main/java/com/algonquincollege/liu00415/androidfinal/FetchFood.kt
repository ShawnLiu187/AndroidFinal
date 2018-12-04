package com.algonquincollege.liu00415.androidfinal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class FetchFood : AppCompatActivity() {

    lateinit var cancelFood: Button
    lateinit var saveFood: Button

    lateinit var foodName: TextView
    lateinit var foodCalorie: TextView
    lateinit var foodProtein: TextView
    lateinit var foodFat: TextView
    lateinit var foodProgress: ProgressBar

    lateinit var foodKeyword: String

    var responseCalorie : Double? = null
    var responseFat : Double? = null
    var responseProtein : Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_food)

        foodName = findViewById<TextView>(R.id.foodName)
        foodCalorie = findViewById(R.id.calorie)
        foodProtein = findViewById(R.id.protein)
        foodFat = findViewById(R.id.fat)
        foodProgress = findViewById(R.id.foodProgress)

        foodProgress.visibility = View.VISIBLE

        cancelFood = findViewById<Button>(R.id.cancelFood)
        saveFood = findViewById<Button>(R.id.saveFood)

        var dataPassed = intent.extras
        foodKeyword = dataPassed.getString("Message")

        cancelFood.setOnClickListener {
            var cancelIntent = Intent()
            cancelIntent.putExtra("CancelMessage", "Cancelled")
            setResult(Activity.RESULT_CANCELED, cancelIntent)
            //setResult(Activity.RESULT_OK, cancelIntent)
            finish()
        }

        saveFood.setOnClickListener{
            var saveIntent = Intent()

//            var dataToPassBack = Bundle()
//            dataToPassBack.putString("foodName", foodKeyword)
//            dataToPassBack.putDouble("calorie", responseCalorie!!)
//            dataToPassBack.putDouble("protein", responseProtein!!)
//            dataToPassBack.putDouble("fat", responseFat!!)

//            saveIntent.putExtras(dataToPassBack)
            saveIntent.putExtra("foodName", foodKeyword)
            saveIntent.putExtra("calorie", responseCalorie)
            saveIntent.putExtra("protein", responseProtein)
            saveIntent.putExtra("fat", responseFat)

            setResult(Activity.RESULT_OK, saveIntent)
            finish()
        }

        var foodQuery = FoodQuery()
        foodQuery.execute()
    }

    inner class FoodQuery: AsyncTask<String, Integer, String>(){
        var searchProgress=0

        override fun doInBackground(vararg params: String?): String {
            try{
                var foodSearch = Uri.encode(foodKeyword)
                var url = URL("https://api.edamam.com/api/food-database/parser?app_id=f3f9275a&app_key=b08724c806dac2361c16175458f65bc8&ingr=$foodSearch")
                var urlConnection = url.openConnection() as HttpURLConnection
                var response = urlConnection.inputStream

                var reader = BufferedReader(InputStreamReader(response, "UTF-8"), 8)
                var stringBuilder = StringBuilder()

                var line: String? = reader.readLine()

                while(line != null){
                    stringBuilder.append(line + "\n")
                    line = reader.readLine()
                }

                var result = stringBuilder.toString()
                var root = JSONObject(result)

                var foodArray = root.getJSONArray("parsed")
                searchProgress = 25

                var foodObject = foodArray.getJSONObject(0)
                foodObject = foodObject.getJSONObject("food").getJSONObject("nutrients")
                searchProgress = 50

                responseCalorie = foodObject.getDouble("ENERC_KCAL")

                if(foodObject.has("FAT") ){

                    responseFat = foodObject.getDouble("FAT")
                    searchProgress = 75
                } else{
                    searchProgress = 75
                    responseFat = 0.00
                }

                if(foodObject.has("PROCNT") ){

                    responseProtein = foodObject.getDouble("PROCNT")
                    searchProgress = 90
                } else{
                    searchProgress = 90
                    responseProtein = 0.00
                }

                searchProgress = 100
                foodProgress.setProgress(searchProgress)

            }catch(e: Exception){
                Log.i("Exception", e.message)
//
//                foodName.setText("Food not found")
//                foodCalorie.text = "Calories:  N/A"
//                foodFat.text = "Fat: N/A"
//                foodProtein.text = "Protein: N/A"
            }
            return "Response"
        }

        override fun onProgressUpdate(vararg values: Integer?) {
//            super.onProgressUpdate(*values)

            foodProgress.setProgress(searchProgress)
        }

        override fun onPostExecute(result: String?) {
            foodName.setText(foodKeyword)
            foodCalorie.text = "Calories:  " + responseCalorie.toString()
            foodFat.text = "Fat:  " + responseFat.toString()
            foodProtein.text = "Protein: " + responseProtein.toString()
            foodProgress.visibility = View.INVISIBLE
        }
    }
}

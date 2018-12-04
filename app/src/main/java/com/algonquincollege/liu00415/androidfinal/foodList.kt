package com.algonquincollege.liu00415.androidfinal


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.*
import com.example.tylercrozman.tylerfinalportion.MovieMain


class foodList : AppCompatActivity() {

    lateinit var searchButton: Button
    lateinit var foodEdit: EditText

    var snackMessage = "Searching for something"

    var foodNames = ArrayList<String>()
    var foodCalories = ArrayList<Double>()
    var foodFats = ArrayList<Double>()
    var foodProteins = ArrayList<Double>()
    var foodAdapter : FoodAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        var toolbar = findViewById<Toolbar>(R.id.foodList_toolbar)
        setSupportActionBar(toolbar)

        foodEdit = findViewById(R.id.foodEdit)



        Toast.makeText(this, "Welcome to Food Portal", Toast.LENGTH_LONG).show()

        searchButton = findViewById(R.id.sendButton)
        searchButton.setOnClickListener{

            var dataToPass = Bundle()
            var typedFood = foodEdit.getText().toString()
            dataToPass.putString("Message", typedFood)

            var foodActivity = Intent(this, FetchFood::class.java)
            foodActivity.putExtras(dataToPass) // send data to next page
            startActivityForResult(foodActivity, 23)


            Snackbar.make(searchButton, snackMessage, Snackbar.LENGTH_LONG)
                    .setAction("Undo", {
                        e -> Toast.makeText(this@foodList, "DoSomething", Toast.LENGTH_LONG).show()
                    })
                    .show()
        }

        var foodListView = findViewById<ListView>(R.id.foodListView)
        foodAdapter = FoodAdapter()
        foodListView?.setAdapter(foodAdapter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 50){
            Log.i("Food List", "Returned to FoodList.onActivityResult");
        }
        if(resultCode == Activity.RESULT_OK){

            var foodName = data?.getStringExtra("foodName")
            foodNames.add(foodName!!)

            var calorie = data?.getDoubleExtra("calorie", 0.00)
            foodCalories.add(calorie!!)

            var protein = data?.getDoubleExtra("protein", 0.00)
            foodProteins.add(protein!!)

            var fat = data?.getDoubleExtra("fat", 0.00)
            foodFats.add(fat!!)

            foodAdapter?.notifyDataSetChanged()

            Toast.makeText(this, "Food Added", Toast.LENGTH_LONG).show()
        }
        if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this, "Please check your spelling and try again", Toast.LENGTH_LONG).show()
        }

    }

    inner class FoodAdapter : ArrayAdapter<String>(this@foodList, 0 ) {
        override fun getCount(): Int {
            return foodNames.size
        }

        override fun getItem(position: Int): String? {
            return foodNames.get(position)
        }

        override fun getView(position : Int, convertView: View?, parent : ViewGroup): View {
            var inflater = LayoutInflater.from(parent.getContext())
            var result = null as View?


            result = inflater.inflate(R.layout.food_item, null)
            val thisText = result.findViewById(R.id.foodContent) as TextView
            thisText.setText("Food Name: " + getItem(position) + "  \nCalories: " + foodCalories[position] + " Protein: " + foodProteins[position] + "  Fat: " + foodFats[position])
            return result
        }

        override fun getItemId(position: Int): Long{
            return 0
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.action1 -> {
                val newsList = Intent(this, newsList::class.java)
                startActivity(newsList)
            }
            R.id.action2 -> {
                var busActivity = Intent(this, OCTranspo::class.java)
                startActivity(busActivity)
            }
            R.id.action3 -> {
                var destinationFilm = Intent(this, MovieMain::class.java)
                startActivity(destinationFilm)
            }
            R.id.action4 -> {
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

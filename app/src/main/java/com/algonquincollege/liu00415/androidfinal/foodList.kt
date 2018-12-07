package com.algonquincollege.liu00415.androidfinal


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.*
import com.example.tylercrozman.tylerfinalportion.MovieMain
import android.content.Context
import kotlin.math.roundToInt


class foodList : AppCompatActivity() {

    lateinit var searchButton: Button
    lateinit var foodEdit: EditText

    var snackMessage = "Searching for something"

    var foodNames = ArrayList<String>()
    var foodCalories = ArrayList<Double>()
    var foodFats = ArrayList<Double>()
    var foodProteins = ArrayList<Double>()
    var foodAdapter : FoodAdapter? = null

    //database stuff
    lateinit var dbHelper: FoodDatabaseHelper
    lateinit var db: SQLiteDatabase
    lateinit var results: Cursor

    //fragment stuff
    var foodPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        var toolbar = findViewById<Toolbar>(R.id.foodList_toolbar)
        setSupportActionBar(toolbar)

        //database write to array
        dbHelper = FoodDatabaseHelper()  //get a helper object
        db = dbHelper.writableDatabase

        results = db.query(TABLE_NAME, arrayOf("_id", FOODNAMES, FOODCALORIESS, FOODPROTEINS, FOODFATS), null, null, null, null, null, null )

        results.moveToFirst() // point to first row of results
        val idIndex = results.getColumnIndex("_id") //find the index of _id column
        val foodNamesIndex = results.getColumnIndex(FOODNAMES)
        val calorieIndex = results.getColumnIndex(FOODCALORIESS)
        val proteinIndex = results.getColumnIndex(FOODPROTEINS)
        val fatIndex = results.getColumnIndex(FOODFATS)

        while(!results.isAfterLast()) // while you are not done with reading data
        {
            var thisID = results.getInt(idIndex)
            var thisFoodName = results.getString(foodNamesIndex)
            var thisCalorie = results.getDouble(calorieIndex)
            var thisProtein = results.getDouble(proteinIndex)
            var thisFat = results.getDouble(fatIndex)

            foodNames.add(thisFoodName)
            foodCalories.add(thisCalorie)
            foodProteins.add(thisProtein)
            foodFats.add(thisFat)
            results.moveToNext()
        }

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


        var fragmentLocation = findViewById<FrameLayout>(R.id.foodFragmentLocation)

        foodListView.setOnItemClickListener { parent, view, position, id ->
            foodPosition = position

            var foodNameTP = foodNames.get(position)
            var foodCalorieTP = foodCalories.get(position)
            var foodProteinTP = foodProteins.get(position)
            var foodFatTP = foodFats.get(position)

            var dataTP = Bundle()
            dataTP.putString("foodName", foodNameTP)
            dataTP.putDouble("calorie", foodCalorieTP)
            dataTP.putDouble("protein", foodProteinTP)
            dataTP.putDouble("fat", foodFatTP)
            dataTP.putLong("id", id)
//            var newId = position+1
//            dataTP.putInt("id", newId)

            var foodDetailActivity = Intent(this, FoodDetails::class.java)
            foodDetailActivity.putExtras(dataTP)
            startActivityForResult(foodDetailActivity, 35)
        }
        foodAdapter = FoodAdapter(this)
        foodListView?.setAdapter(foodAdapter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 23) {
            Log.i("Food List", "Returned to FoodList.onActivityResult");

            if (resultCode == Activity.RESULT_OK) {

                var foodName = data?.getStringExtra("foodName")
                foodNames.add(foodName!!)

                var calorie = data?.getDoubleExtra("calorie", 0.00)
                foodCalories.add(calorie!!)

                var protein = data?.getDoubleExtra("protein", 0.00)
                foodProteins.add(protein!!)

                var fat = data?.getDoubleExtra("fat", 0.00)
                foodFats.add(fat!!)

                foodAdapter?.notifyDataSetChanged()

                //write to database
                val newRow = ContentValues()
                newRow.put(FOODNAMES, foodName)
                newRow.put(FOODCALORIESS, calorie)
                newRow.put(FOODPROTEINS, protein)
                newRow.put(FOODFATS, fat)

              val idreturned = db.insert(TABLE_NAME, "", newRow)

                Log.i("ID=", ""+idreturned)
                results = db.query(TABLE_NAME, arrayOf("_id", FOODNAMES, FOODCALORIESS, FOODPROTEINS, FOODFATS), null, null, null, null, null, null )

                Toast.makeText(this, "Food Added", Toast.LENGTH_LONG).show()
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Please check your spelling and try again", Toast.LENGTH_LONG).show()
            }
        }
        if(requestCode == 35){
            Toast.makeText(this, "Back to FoodList", Toast.LENGTH_LONG).show()
            if(resultCode == Activity.RESULT_OK){
                deleteMessage(data!!.getLongExtra("id", 0))
            }
        }


    }

    val DATABASE_NAME = "FoodDatabaseFile"
    val VERSION_NUM = 1
    val TABLE_NAME = "Foods"
    val FOODNAMES = "foodNames"
    val FOODCALORIESS = "calories"
    val FOODPROTEINS = "proteins"
    val FOODFATS = "fats"

    inner class FoodDatabaseHelper : SQLiteOpenHelper(this@foodList, DATABASE_NAME, null, VERSION_NUM){
        override fun onCreate(db: SQLiteDatabase) {
            Log.i("FoodDatabaseHelper", "Calling onCreate");
            db.execSQL("CREATE TABLE $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, $FOODNAMES TEXT, $FOODCALORIESS TEXT, $FOODPROTEINS TEXT, $FOODFATS TEXT)")
        }



        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)

            Log.i("FoodDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);

            //create new table
            onCreate(db)
        }
    }

    inner class FoodAdapter(ctx: Context) : ArrayAdapter<String>(ctx, 0 ) {
        override fun getCount(): Int {
            return foodNames.size
        }

        override fun getItem(position: Int): String? {
            return foodNames.get(position)
        }

        override fun getView(position : Int, convertView: View?, parent : ViewGroup): View {
            var inflater = LayoutInflater.from(parent.getContext())
            var result = null as View?

            val foodNameStr = getString(R.string.foodName)
            val calorieStr = getString(R.string.calorie)
            val proteinStr = getString(R.string.protein)
            val fatStr = getString(R.string.fat)

            result = inflater.inflate(R.layout.food_item, null)
            val thisText = result.findViewById(R.id.foodContent) as TextView
            //thisText.setText("Food Name: " + getItem(position) + "  \nCalories: " + foodCalories[position] + " Protein: " + foodProteins[position] + "  Fat: " + foodFats[position])
            thisText.text = "$foodNameStr: " + getItem(position) + " \n$calorieStr: " + foodCalories[position] + " $proteinStr: " + foodProteins[position] + " $fatStr: " + foodFats[position]
                    return result
        }

        override fun getItemId(position: Int): Long{
            results.moveToPosition(position)
            return results.getInt(  results.getColumnIndex("_id") ).toLong()
        }
    }

    fun deleteMessage(id:Long)
    {
        db.delete(TABLE_NAME, "_id=$id", null)
        results = db.query(TABLE_NAME, arrayOf("_id", FOODNAMES, FOODCALORIESS, FOODPROTEINS, FOODFATS), null, null, null, null, null, null )
        foodNames.removeAt(foodPosition)
        foodCalories.removeAt(foodPosition)
        foodProteins.removeAt(foodPosition)
        foodFats.removeAt(foodPosition)
        foodAdapter?.notifyDataSetChanged()//reload
    }

    fun averageCalorie()
    {
        var totalCalorie = 0.00
        for ( i in 0..foodCalories.size-1)
        {
            totalCalorie = totalCalorie.plus(foodCalories[i])
        }

        var averageCalorie = (totalCalorie/foodCalories.size).roundToInt()
        //Toast.makeText(this, "Average Calories: $averageCalorie", Toast.LENGTH_LONG).show()

        var averageCalorieStr = getString(R.string.averageCalorie)
        var maxMessage = "$averageCalorieStr: $averageCalorie"
        Snackbar.make(searchButton, maxMessage, Snackbar.LENGTH_LONG)
                .setAction("Info", {
                    e -> Toast.makeText(this@foodList, "This is the average calories of your favourite food items", Toast.LENGTH_LONG).show()
                })
                .show()
    }

    fun maxCalorie()
    {
        var maxCalorie = 0.00
        var maxIndex = 0
        for ( i in 0..foodCalories.size-1)
        {
            if(foodCalories[i]>maxCalorie){
                maxCalorie = foodCalories[i]
                maxIndex = i
            }
        }
        var maxName = foodNames[maxIndex]
//        Toast.makeText(this, "Max Calories: $maxName with $maxCalorie", Toast.LENGTH_LONG).show()
        var maxCalorieStr = getString(R.string.maxCalorie)
        var maxMessage = "$maxCalorieStr: $maxName with $maxCalorie"
        Snackbar.make(searchButton, maxMessage, Snackbar.LENGTH_LONG)
                .setAction("Info", {
                    e -> Toast.makeText(this@foodList, "This is the food with the most calories among favourite food items", Toast.LENGTH_LONG).show()
                })
                .show()
    }

    fun minCalorie()
    {
        var minCalorie = 0.0
        var minIndex = 0
        if(foodCalories.size > 0) {
            minCalorie = foodCalories[0]
            for (i in 1..foodCalories.size - 1) {
                if (foodCalories[i] < minCalorie) {
                    minCalorie = foodCalories[i]
                    minIndex = i
                }
            }
        }
        var minName = foodNames[minIndex]
        //Toast.makeText(this, "Max Calories: $minName with $minCalorie", Toast.LENGTH_LONG).show()
        var minCalorieStr = getString(R.string.minCalorie)
        var maxMessage = "$minCalorieStr: $minName with $minCalorie"
        Snackbar.make(searchButton, maxMessage, Snackbar.LENGTH_LONG)
                .setAction("Info", {
                    e -> Toast.makeText(this@foodList, "This is the food with the least calories among favourite food items", Toast.LENGTH_LONG).show()
                })
                .show()
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

                val howToStr = getString(R.string.howToNavigate)
                val okStr = getString(R.string.ok)
                val cancelStr = getString(R.string.cancel_faveFood)

                var builder = AlertDialog.Builder(this);
                builder.setTitle("$howToStr?")
// Add the buttons
                builder.setView(dialogStuff)

                builder.setPositiveButton("$okStr", {dialog, id ->
                    //user clicked OK button
//                    currentMessage = editText.text.toString()
                })
                builder.setNegativeButton("$cancelStr", {dialog, id ->
                    //user cancelled diaalog
                })
// Create the AlertDialog
                var dialog = builder.create()
                dialog.show()
            }
            R.id.action5 -> {
                maxCalorie()
            }
            R.id.action6 -> {
                averageCalorie()
            }
            R.id.action7 -> {
                minCalorie()
            }
        }
        return true
    }
}

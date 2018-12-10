package com.algonquincollege.liu00415.androidfinal


import android.app.Activity
import android.content.Intent
import android.os.Bundle
//import android.support.v4.app.Fragment
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FoodFragment : Fragment() {

    lateinit var parentDocument: foodList

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var dataPassed = arguments

        var foodName = dataPassed?.getString("foodName")
        var calorie = dataPassed?.getDouble("calorie")
        var protein = dataPassed?.getDouble("protein")
        var fat = dataPassed?.getDouble("fat")
        var id = dataPassed?.getLong("id")

        val foodNameStr = getString(R.string.foodName)
        val calorieStr = getString(R.string.calorie)
        val proteinStr = getString(R.string.protein)
        val fatStr = getString(R.string.fat)

        var screen = inflater.inflate(R.layout.fragment_food, container, false)
        var foodNameView = screen.findViewById<TextView>(R.id.foodNameDetail)
//        foodNameView.setText(foodName)
        foodNameView.text = "$foodNameStr: $foodName"
        var calorieView = screen.findViewById<TextView>(R.id.foodCalorieDetail)
//        calorieView.setText(calorie.toString())
        calorieView.text = "$calorieStr: $calorie"
        var proteinView = screen.findViewById<TextView>(R.id.foodProteinDetail)
//        proteinView.setText(protein.toString())
        proteinView.text = "$proteinStr: $protein"
        var fatView = screen.findViewById<TextView>(R.id.foodFatDetail)
//        fatView.setText(fat.toString())
        fatView.text = "$fatStr: $fat"
        var idView = screen.findViewById<TextView>(R.id.foodIdDetail)
//        idView.setText(id.toString())
        idView.text = "ID: $id"

        var foodDelBtn = screen.findViewById<Button>(R.id.deleteFoodBtn)
        foodDelBtn.setOnClickListener {
                var dataBack = Intent()
                dataBack.putExtra("id", id)
                activity?.setResult(Activity.RESULT_OK, dataBack)
                activity?.finish()
        }

        var cancelDelBtn = screen.findViewById<Button>(R.id.cancelDelBtn)
        cancelDelBtn.setOnClickListener{
            var dataBack = Intent()
//            dataBack.putExtra("id", id)
            activity?.setResult(Activity.RESULT_CANCELED, dataBack)
            activity?.finish()
        }

        return screen
    }


}

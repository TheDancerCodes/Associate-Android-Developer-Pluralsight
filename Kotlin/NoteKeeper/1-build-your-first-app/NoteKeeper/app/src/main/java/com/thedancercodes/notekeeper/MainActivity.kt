package com.thedancercodes.notekeeper

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Code that handles a user tap on the FAB: Double the value contained in the Activity's TextView
        fab.setOnClickListener { view ->

            // Declare a variable to hold the current value of teh TextView
            val originalValue = textDisplayedValue.text.toString().toInt()

            // Assign this variable the result of doubling the originalValue
            val newValue = originalValue * 2

            // Display the newValue
            textDisplayedValue.text = newValue.toString()

            // Leverage String Template to include value of the originalValue & newValue in the string.
            Snackbar.make(view, "Value $originalValue changed to $newValue", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

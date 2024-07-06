package com.example.cosmosinhand.ui

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cosmosinhand.R
import com.example.cosmosinhand.database.DatabaseMain
import com.example.cosmosinhand.repository.CosmosRepository
import kotlinx.android.synthetic.main.activity_cosmos.*
import java.text.SimpleDateFormat
import java.util.*

class CosmosActivity : AppCompatActivity() {


    lateinit var toggle: ActionBarDrawerToggle
    lateinit var viewmodel: CosmosViewModel

    // simple date format
    var simpleDateFormate = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // instance of repository
        val repository = CosmosRepository(DatabaseMain(this))
        // instance of viewModel
        val viewModelProviderFactory = CosmosViewModelProviderFactory(application, repository)
        viewmodel =
            ViewModelProvider(this, viewModelProviderFactory).get(CosmosViewModel::class.java)
        // setting up ContentView
        setContentView(R.layout.activity_cosmos)

        // setting up toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)

        // bottom navigation view for navigation control
        bottomnavigationview.setupWithNavController(fv.findNavController())

        //initialising drawer layout
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        // setting up toggle button
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // btn to select date of a random date
//        btn_toolbar.setOnClickListener {
//            datepick()
//        }


    }

    //on item selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item))
            true
        else
            super.onOptionsItemSelected(item)

    }

    // Function to Call getApodList function of viewModel
    private fun callApod(sd: Date, ed: Date) {
        viewmodel.getApodList(simpleDateFormate.format(ed), simpleDateFormate.format(sd))
    }

    // function to pick up date
    private fun datepick() {
        val myCal = Calendar.getInstance()
        val year = myCal.get(Calendar.YEAR)
        val month = myCal.get(Calendar.MONTH)
        val day = myCal.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, selectedyear, seletedmonth, selecteddayofmonth ->
                val selecteddate = "$selectedyear-$seletedmonth-$selecteddayofmonth"
                // sd-> starting date
                var sd = simpleDateFormate.parse(selecteddate)
                // ed->date just before 5 days of starting date
                var ed: Date = Date(sd.time - 432000000L)
                callApod(sd, ed)
            }, year, month, day
        )
        dpd.show()
    }


}
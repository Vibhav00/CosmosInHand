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
    var simpleDateFormate = SimpleDateFormat("yyyy-MM-dd")
    //var toolbar:Button?=null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = CosmosRepository(DatabaseMain(this))
        val viewModelProviderFactory = CosmosViewModelProviderFactory(application,repository)
        viewmodel =
            ViewModelProvider(this, viewModelProviderFactory).get(CosmosViewModel::class.java)
        setContentView(R.layout.activity_cosmos)
         // toolbar=findViewById(R.id.btn_toolbar)


        val toolbar=findViewById<View>(R.id.toolbar)  as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
//        if(supportActionBar!=null)
//        {
//            supportActionBar?.
//
//
//        }

        //bottom navigation view
        bottomnavigationview.setupWithNavController(fv.findNavController())
        //drawer layout
       // Log.e("vibhav", "yaha tk main")
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_toolbar.setOnClickListener{
            datepick()
        }


    }

    //drawer layout
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item))
            true
        else
            super.onOptionsItemSelected(item)

    }

    private fun callApod(sd: Date, ed: Date) {


      viewmodel.getApodList(simpleDateFormate.format(ed),simpleDateFormate.format(sd))
    }

    private fun datepick() {
        val myCal = Calendar.getInstance()
        val year = myCal.get(Calendar.YEAR)
        val month = myCal.get(Calendar.MONTH)
        val day = myCal.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, selectedyear, seletedmonth, selecteddayofmonth ->
                val selecteddate = "$selectedyear-$seletedmonth-$selecteddayofmonth"
                var sd = simpleDateFormate.parse(selecteddate)
                var ed:Date = Date(sd.time-432000000L)
                Log.e("check1",simpleDateFormate.format(sd))
                Log.e("check2",simpleDateFormate.format(ed))


                callApod(sd,ed)
            }, year, month, day
        )
        dpd.show()
    }


}
package com.example.cosmosinhand.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cosmosinhand.R
import com.example.cosmosinhand.database.DatabaseMain
import com.example.cosmosinhand.repository.CosmosRepository
import kotlinx.android.synthetic.main.activity_cosmos.*

class CosmosActivity : AppCompatActivity() {


    lateinit var toggle: ActionBarDrawerToggle
    lateinit var viewmodel: CosmosViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = CosmosRepository(DatabaseMain(this))
        val viewModelProviderFactory = CosmosViewModelProviderFactory(application,repository)
        viewmodel =
            ViewModelProvider(this, viewModelProviderFactory).get(CosmosViewModel::class.java)
        setContentView(R.layout.activity_cosmos)

        //bottom navigation view
        bottomnavigationview.setupWithNavController(fv.findNavController())
        //drawer layout
       // Log.e("vibhav", "yaha tk main")
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    //drawer layout
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item))
            true
        else
            super.onOptionsItemSelected(item)

    }

}
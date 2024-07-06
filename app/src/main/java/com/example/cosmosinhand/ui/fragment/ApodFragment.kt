package com.example.cosmosinhand.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cosmosinhand.R
import com.example.cosmosinhand.adapters.ApodAdapter
import com.example.cosmosinhand.repository.CosmosRepository
import com.example.cosmosinhand.ui.CosmosActivity
import com.example.cosmosinhand.ui.CosmosViewModel
import com.example.cosmosinhand.ui.CosmosViewModelProviderFactory
import com.example.cosmosinhand.util.Resource
import kotlinx.android.synthetic.main.activity_cosmos.*
import kotlinx.android.synthetic.main.fragment_apod.*
import java.text.SimpleDateFormat
import java.util.*

class ApodFragment : Fragment(R.layout.fragment_apod) {

    lateinit var viewModel: CosmosViewModel
    lateinit var apodAdapter: ApodAdapter
    var simpleDateFormate = SimpleDateFormat("yyyy-MM-dd")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //   initialising viewModel
        viewModel = (activity as CosmosActivity).viewmodel

        //   checking if there is internet connection or not
        //   if not goto  saved fragment which do not require internet connection
        if (!viewModel.hasInternateConnection()) {
            Toast.makeText(
                (activity as CosmosActivity).applicationContext,
                "NO ITERNET CONNECTION ",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_apod_fragment_to_savedFragment)
        }

        //settion up recyclere view
        setUpRecyclerView()

        // navigating to the description fragment after clicking on the items of  the recycler view
        apodAdapter.setOnItemClickListner {

            val bundle = Bundle().apply {
                putSerializable("url", it.hdurl)
                putSerializable("description", it.explanation)
                putSerializable("title",it.title)
                putSerializable("copy",it.copyright)
            }
            findNavController().navigate(R.id.action_apod_fragment_to_descFragment, bundle)
        }

        // observing json response(APOD) and updating differ (DIFFER)
        viewModel.apodList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { res ->
                        apodAdapter.differ.submitList(res)
                        rvapod.smoothScrollToPosition(5)
                    }

                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e("djkfj", "An error occured ")

                    }

                }
                is Resource.Loding -> {
                    showProgressBar()

                }
            }


        })


    }

    //function to hind progress bar
    private fun hideProgressBar() {
        pbar_apod.visibility = View.INVISIBLE
    }

    // function to show progress bar
    private fun showProgressBar() {
        pbar_apod.visibility = View.VISIBLE
    }

    // function to set recycler view
    private fun setUpRecyclerView() {
        apodAdapter = ApodAdapter()
        rvapod.apply {
            adapter = apodAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)

        }
    }


}
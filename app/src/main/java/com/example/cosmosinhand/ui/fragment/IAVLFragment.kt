package com.example.cosmosinhand.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cosmosinhand.R
import com.example.cosmosinhand.adapters.IavlAdapter
import com.example.cosmosinhand.ui.CosmosActivity
import com.example.cosmosinhand.ui.CosmosViewModel
import com.example.cosmosinhand.util.Resource
import kotlinx.android.synthetic.main.fragment_i_a_v_l.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class IAVLFragment : Fragment(R.layout.fragment_i_a_v_l) {

    lateinit var viewModel: CosmosViewModel
    lateinit var iavlAdapter: IavlAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // initialising view model
        viewModel = (activity as CosmosActivity).viewmodel
        var name: String = "sun"

        // setting up recycler view
        setUpRecyclerView()


        // setting a functionality to search after each 1/2 second when the text in the
        // Edit text is changed
        var job: Job? = null
        et_iavl.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                editable?.let {
                    if (editable.isNotEmpty()) {
                        viewModel.getIavlList(editable.toString())
                    }
                }
            }
        }


        // navigating to the database fragment if there is no internet connection
        if (!viewModel.hasInternateConnection()) {
            Toast.makeText(
                (activity as CosmosActivity).applicationContext,
                "NO ITERNET CONNECTION ",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_iavl_fragmet_to_savedFragment)
        }


        // setting item click listener to the items in the iavl adapter
        iavlAdapter.setOnItemClickListner {

            val bundle = Bundle().apply {
                putSerializable("urlpre", it.href)
                putSerializable("description", it.data[0].description_508)
            }
            findNavController().navigate(R.id.action_iavl_fragmet_to_descFragment, bundle)
        }

        // observing the response IAVL and posting it to differiavl
        viewModel.iavlList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { res ->
                        iavlAdapter.diffiiavl.submitList(res.collection.items)

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e("kjkj", "Erroor occcured in iavl")
                    }
                }
                is Resource.Loding -> {
                    showProgressBar()

                }
            }


        })


    }

    // function to hide progress bar
    private fun hideProgressBar() {
        pb_iavl.visibility = View.INVISIBLE

    }

    // function to show progress bar
    private fun showProgressBar() {
        pb_iavl.visibility = View.VISIBLE
    }

    // funtion to setuprecylerview
    private fun setUpRecyclerView() {
        iavlAdapter = IavlAdapter()
        rv_iavl.apply {
            adapter = iavlAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
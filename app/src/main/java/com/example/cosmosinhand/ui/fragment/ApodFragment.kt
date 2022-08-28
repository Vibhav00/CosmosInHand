package com.example.cosmosinhand.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cosmosinhand.R
import com.example.cosmosinhand.adapters.ApodAdapter
import com.example.cosmosinhand.repository.CosmosRepository
import com.example.cosmosinhand.ui.CosmosActivity
import com.example.cosmosinhand.ui.CosmosViewModel
import com.example.cosmosinhand.ui.CosmosViewModelProviderFactory
import com.example.cosmosinhand.util.Resource
import kotlinx.android.synthetic.main.fragment_apod.*

class ApodFragment : Fragment(R.layout.fragment_apod) {

    lateinit var viewModel: CosmosViewModel
    lateinit var apodAdapter: ApodAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("vibhav", "yaha tk")
        viewModel = (activity as CosmosActivity).viewmodel


        if(!viewModel.hasInternateConnection())
        {
            Toast.makeText((activity as CosmosActivity).applicationContext,"NO ITERNET CONNECTION ", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_apod_fragment_to_savedFragment)
        }

        setUpRecyclerView()
        apodAdapter.setOnItemClickListner {

            val bundle = Bundle().apply {
                putSerializable("url", it.hdurl)
                putSerializable("description", it.explanation)
            }
            findNavController().navigate(R.id.action_apod_fragment_to_descFragment, bundle)
        }
        viewModel.getApodList("2022-01-01", "2022-01-05")
        viewModel.apodList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { res ->
                        apodAdapter.differ.submitList(res)
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

    private fun hideProgressBar() {
        pbar_apod.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        pbar_apod.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        apodAdapter = ApodAdapter()
        rvapod.apply {
            adapter = apodAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}
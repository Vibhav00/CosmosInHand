package com.example.cosmosinhand.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cosmosinhand.R
import com.example.cosmosinhand.adapters.DatabaseAdapter
import com.example.cosmosinhand.adapters.IavlAdapter
import com.example.cosmosinhand.ui.CosmosActivity
import com.example.cosmosinhand.ui.CosmosViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_i_a_v_l.*
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class SavedFragment : Fragment(R.layout.fragment_saved) {
    lateinit var viewModel: CosmosViewModel
    lateinit var databaseAdapter: DatabaseAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as CosmosActivity).viewmodel

        setUpRecyclerView()

        Log.e("aman1", "kjkjk")
        GlobalScope.launch {
            Log.e("aman", viewModel.getAllSavedItems().toString())

        }
        Log.e("aman2", "kdjf")
        viewModel.getAllSavedItems().observe(viewLifecycleOwner, Observer {
            databaseAdapter.differDateBaseItem.submitList(it)
        })

        databaseAdapter.setonItemClickListner {
            var bundle = Bundle().apply {
                putSerializable("url", it.ure)
                putSerializable("description", it.des)
            }
            findNavController().navigate(R.id.action_savedFragment_to_descFragment, bundle)
        }


        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val databaseItem = databaseAdapter.differDateBaseItem.currentList[position]
                viewModel.deleteSavedItem(databaseItem)

                Snackbar.make(view, "deleted ", Snackbar.LENGTH_LONG).apply {
                    setAction("undo")
                    {
                        viewModel.saveIntoDatabase(databaseItem)
                    }
                        .show()
                }

            }

        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(rv_saved_fra)
        }

    }

    private fun setUpRecyclerView() {
        databaseAdapter = DatabaseAdapter()
        rv_saved_fra.apply {
            adapter = databaseAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
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

        //initialising view model
        viewModel = (activity as CosmosActivity).viewmodel

        // setting up recycler veiw
        setUpRecyclerView()

        // observing the dataitems from the database and updating it to the differDataBaseItem
        viewModel.getAllSavedItems().observe(viewLifecycleOwner, Observer {
            databaseAdapter.differDateBaseItem.submitList(it)
        })

        // onclick listner to the database items and navigating to the description fragment
        databaseAdapter.setonItemClickListner {
            var bundle = Bundle().apply {
                putSerializable("url", it.ure)
                putSerializable("description", it.des)
            }
            findNavController().navigate(R.id.action_savedFragment_to_descFragment, bundle)
        }


        // implementing the functionality to swip items of the database to delete
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

                // making snackbar to undo changes
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

    // function to set up recyclerview
    private fun setUpRecyclerView() {
        databaseAdapter = DatabaseAdapter()
        rv_saved_fra.apply {
            adapter = databaseAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
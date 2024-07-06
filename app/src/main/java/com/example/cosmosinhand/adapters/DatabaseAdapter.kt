package com.example.cosmosinhand.adapters

import android.print.PrintDocumentAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cosmosinhand.R
import com.example.cosmosinhand.models.database.DatabaseItem
import kotlinx.android.synthetic.main.item_apod.view.*
import kotlinx.android.synthetic.main.item_i_a_v_l.view.*


/**
 * it is here to handle Database items
 */
class DatabaseAdapter : RecyclerView.Adapter<DatabaseAdapter.DataItemViewHolder>() {
    class DataItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    private var differCallBackSaved = object : DiffUtil.ItemCallback<DatabaseItem>() {
        override fun areItemsTheSame(oldItem: DatabaseItem, newItem: DatabaseItem): Boolean {
            return oldItem.ure == newItem.ure
        }

        override fun areContentsTheSame(oldItem: DatabaseItem, newItem: DatabaseItem): Boolean {
            return oldItem == newItem
        }

    }

    val differDateBaseItem = AsyncListDiffer(this, differCallBackSaved)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataItemViewHolder {
        return DataItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_i_a_v_l, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataItemViewHolder, position: Int) {
        val dataItem = differDateBaseItem.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(dataItem.ure).into(image_view_iavl)
            desc_iavl.text = dataItem.des
            title_iavl.text="This is new Item "
//            title_iavl.visibility=View.INVISIBLE
            setOnClickListener {
                onItemDataClickListner?.let {
                    it(dataItem)

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differDateBaseItem.currentList.size
    }

    private var onItemDataClickListner: ((DatabaseItem) -> Unit)? = null
    fun setonItemClickListner(listner: (DatabaseItem) -> Unit) {
        onItemDataClickListner = listner
    }


}
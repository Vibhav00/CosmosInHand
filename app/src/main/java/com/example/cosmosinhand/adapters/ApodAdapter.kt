package com.example.cosmosinhand.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cosmosinhand.R
import com.example.cosmosinhand.models.apod.Apod
import com.example.cosmosinhand.models.apod.ApodItem
import kotlinx.android.synthetic.main.item_apod.view.*


/**
 * this is here to handle the recyclerview of Apod items
 * using AsyncListDiffer for this
 */
class ApodAdapter : RecyclerView.Adapter<ApodAdapter.ApodViewHolder>() {

    class ApodViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)

    private val differCallBack = object : DiffUtil.ItemCallback<ApodItem>() {
        override fun areItemsTheSame(oldItem: ApodItem, newItem: ApodItem): Boolean {
            return oldItem.hdurl == newItem.hdurl
        }

        override fun areContentsTheSame(oldItem: ApodItem, newItem: ApodItem): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApodViewHolder {
        return ApodViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_apod, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ApodViewHolder, position: Int) {
        val apod = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(apod.url).into(iv_apod)
            date_apod.text = apod.date
            desc_apod.text = apod.explanation
            setOnClickListener {
                onItemClickListener?.let {
                    it(apod)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((ApodItem) -> Unit)? = null
    fun setOnItemClickListner(listner: (ApodItem) -> Unit) {
        onItemClickListener = listner
    }
}
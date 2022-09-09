package com.example.cosmosinhand.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cosmosinhand.R
import com.example.cosmosinhand.models.apod.ApodItem
import com.example.cosmosinhand.models.iavl.Item
import kotlinx.android.synthetic.main.item_i_a_v_l.view.*

/**
 * it is to handle IAVL(image and video library ) items.
 *
 */
class IavlAdapter : RecyclerView.Adapter<IavlAdapter.IavlViewHolder>() {
    class IavlViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Item>() {


        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.href == newItem.href
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }
    val diffiiavl = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IavlViewHolder {
        return IavlViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_i_a_v_l, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IavlViewHolder, position: Int) {
        val iavl = diffiiavl.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(iavl.links[0].href).into(image_view_iavl)
            title_iavl.text = iavl.data[0].title
            desc_iavl.text = iavl.data[0].description_508

            setOnClickListener {
                onItemClickListnerIavl?.let {
                    it(iavl)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return diffiiavl.currentList.size
    }


    private var onItemClickListnerIavl: ((Item) -> Unit)? = null
    fun setOnItemClickListner(listner: (Item) -> Unit) {
        onItemClickListnerIavl = listner
    }
}
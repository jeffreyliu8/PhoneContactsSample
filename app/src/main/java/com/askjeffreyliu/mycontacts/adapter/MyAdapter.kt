package com.askjeffreyliu.mycontacts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.askjeffreyliu.mycontacts.R
import com.askjeffreyliu.mycontacts.model.MyContact
import kotlinx.android.synthetic.main.my_list_layout.view.*

class MyAdapter(private val listener: (MyContact) -> Unit) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var mList: List<MyContact>? = null

    fun updateList(list: List<MyContact>?) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.my_list_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =
        holder.bind(mList!![position], listener)

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: MyContact, listener: (MyContact) -> Unit) = with(itemView) {
            nameTextView.text = item.id
            descriptionTextView.text = item.name

            setOnClickListener {
                listener(item)
            }
        }
    }
}
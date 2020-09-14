package com.askjeffreyliu.mycontacts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.askjeffreyliu.mycontacts.R
import com.askjeffreyliu.mycontacts.model.MyContact
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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
            nameTextView.text = item.name
            phoneTextView.text = item.phone
//            emailTextView.text = item.email

            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)

            Glide.with(context)
                .load(item.photo)
                .apply(options)
                .into(avatarImageView)

            setOnClickListener {
                listener(item)
            }
        }
    }
}
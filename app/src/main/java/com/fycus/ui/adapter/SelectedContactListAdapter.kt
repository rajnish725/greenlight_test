package com.fycus.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fycus.R
import com.fycus.databinding.MyContactListBinding
import com.fycus.databinding.SelectContactListBinding
import com.fycus.models.response.ContactModel

class SelectedContactListAdapter(
    val context: Context,
    private val contactList: ArrayList<ContactModel>
) : RecyclerView.Adapter<SelectedContactListAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding: SelectContactListBinding = DataBindingUtil.bind(view)!!
        /*override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClick(adapterPosition, v)
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.select_contact_list, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.ivCross.setOnClickListener {
            if(listener != null){
                listener!!.onItemClick(position,it)
            }
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }
}
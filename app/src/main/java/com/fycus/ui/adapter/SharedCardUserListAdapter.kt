package com.fycus.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fycus.R
import com.fycus.databinding.MyContactListBinding
import com.fycus.databinding.SharedCardUserListBinding

class SharedCardUserListAdapter(
    val context: Context
) : RecyclerView.Adapter<SharedCardUserListAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding: SharedCardUserListBinding = DataBindingUtil.bind(view)!!
        /*override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClick(adapterPosition, v)
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.shared_card_user_list, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.llRemove.setOnClickListener{
            if(listener != null){
                listener!!.onItemClick(position,it)
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }
}
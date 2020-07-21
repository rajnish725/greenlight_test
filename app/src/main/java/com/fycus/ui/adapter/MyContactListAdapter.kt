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
import com.fycus.models.response.ContactModel

class MyContactListAdapter(
    val context: Context,
    private val contactList: ArrayList<ContactModel>
) : RecyclerView.Adapter<MyContactListAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding: MyContactListBinding = DataBindingUtil.bind(view)!!
        /*override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClick(adapterPosition, v)
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_contact_list, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = contactList[position]
        /*holder.binding.tvName.text = model.name
        holder.binding.tvMobile.text = model.mobileno

        if(model.profilepic != ""){
            Glide.with(context!!).load(Uri.parse(model.profilepic))
                 .error(R.drawable.user) //error
                .into(holder.binding.ivUser);
        }

         */

        if(model.isCheck){
            holder.binding.ivCheck.visibility = View.VISIBLE
        }else{
            holder.binding.ivCheck.visibility = View.GONE
        }
        holder.binding.cardView.setOnClickListener {
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
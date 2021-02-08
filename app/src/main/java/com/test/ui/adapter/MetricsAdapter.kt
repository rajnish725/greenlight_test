package com.test.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.R
import com.test.databinding.MetricsAdapterBinding
import com.test.listner.OnItemListener
import com.test.models.MetricsDataModel

/**
 * Created by Rajnish Yadav on 06 Feb 2021 at 18:10.
 */
class MetricsAdapter(
    val mContext: Context,
    var list: List<MetricsDataModel>,
    val from: Int,
    val listener: OnItemListener
) : RecyclerView.Adapter<MetricsAdapter.MetricsHolder>() {

    class MetricsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: MetricsAdapterBinding = DataBindingUtil.bind(itemView)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetricsHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.metrics_adapter, parent, false)
        return MetricsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: MetricsHolder, position: Int) {
        val model = list[position]
        var name: String = ""

        if (from == 1) {
            name = model.country!!
            holder.binding.txtName.text = name

        } else if (from == 2) {
            name = model.zone!!
            holder.binding.txtName.text = name

        } else if (from == 3) {
            name = model.region!!
            holder.binding.txtName.text = name

        } else if (from == 4) {
            name = model.area!!
            holder.binding.txtName.text = name
        } else if (from == 5) {
            name = model.name!!
            holder.binding.txtName.text = name
        }
        holder.itemView.setOnClickListener {
            if (from == 5)
                listener.onCellClickListener("", holder.itemView, position, name, from, true)
            else
                listener.onCellClickListener("", holder.itemView, position, name, from, false)
        }
    }

    fun filterList(filterdNames: List<MetricsDataModel>) {
        list = filterdNames
        notifyDataSetChanged()
    }


}
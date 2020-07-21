package com.fycus.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.fycus.R
import com.fycus.models.FaqModel
import kotlinx.android.synthetic.main.faq_child.view.*
import kotlinx.android.synthetic.main.faq_header.view.*

class FaqExpandableAdapter(
    val context: Context?, private val childList: ArrayList<FaqModel>,
    private val headerList: ArrayList<String>
) :
    BaseExpandableListAdapter() {


    override fun getGroup(p0: Int): Any {
        return headerList.size
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        b: Boolean,
        convertView: View?,
        viewGroup: ViewGroup?
    ): View {
        val inflater =
            this.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var convertView = convertView
        if (convertView == null) {
            val infalInflater = this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.faq_header, null)
        }
        if(groupPosition == 0){
          convertView!!.view.visibility=View.GONE
        }else{
            convertView!!.view.visibility=View.VISIBLE
        }

        convertView.tvTitle.text = headerList[groupPosition]
        if (b)
            convertView.ivIcon!!.animate().rotation(90f).setDuration(200).start()
        else
            convertView.ivIcon!!.animate().rotation(0f).setDuration(200).start()
        //convertView.ivIcon.setImageResource(R.drawable.ic_navigate_next_black_24dp)
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return this.childList[groupPosition].message
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        b: Boolean,
        convertView1: View?,
        viewGroup: ViewGroup?
    ): View {

        var convertView = convertView1

        //val childText = getChild(groupPosition, childPosition) as String

        if (convertView == null) {
            val infalInflater =
                this.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.faq_child, null)
        }

        val model: FaqModel = childList[groupPosition]
        convertView!!.tvMessage.text = model.message
        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return headerList.size
    }
}
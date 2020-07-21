package com.fycus.ui.activity

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fycus.BaseActivity
import com.fycus.R
import com.fycus.databinding.ActivityAddContactsFamilyBusinessAndFriendBinding
import com.fycus.models.response.ContactModel
import com.fycus.ui.adapter.MyContactListAdapter
import com.fycus.ui.adapter.SelectedContactListAdapter

class AddContactsFamilyBusinessAndFriendActivity : BaseActivity() {
    private lateinit var binding : ActivityAddContactsFamilyBusinessAndFriendBinding
    private var adapter : MyContactListAdapter?= null
    private var adapterSelected : SelectedContactListAdapter?= null
    private var contactList : ArrayList<ContactModel>? = null
    private var contactSelectedList : ArrayList<ContactModel>? = null
    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_add_contacts_family_business_and_friend)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_contacts_family_business_and_friend)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.navigationIcon = this.getDrawable(R.drawable.back)
        binding.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        type = this.intent.getStringExtra("type")!!
        when (type) {
            "1" -> {
                supportActionBar?.title = "Family"
            }
            "2" -> {
                supportActionBar?.title = "Business"
            }
            "3" -> {
                supportActionBar?.title = "Family"
            }
            else -> {
                supportActionBar?.title = ""
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = getColor(R.color.colorPrimaryDark)
            } else {
                window.statusBarColor = Color.BLUE
            }
        }
        contactList = ArrayList()
        contactSelectedList = ArrayList()
        setData()
    }

    private fun setData() {
        setSelectedContact()
        contactList!!.clear()
        //contactList!!.addAll(arrayList!!)
        contactList!!.add(ContactModel(false))
        contactList!!.add(ContactModel(false))
        contactList!!.add(ContactModel(false))
        contactList!!.add(ContactModel(false))
        contactList!!.add(ContactModel(false))
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerviewContact.layoutManager = layoutManager
        adapter = MyContactListAdapter(this,contactList!!)
        binding.recyclerviewContact.adapter = adapter

        adapter!!.setOnItemClickListener(object : MyContactListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, view: View) {
                contactList!![position].isCheck = !contactList!![position].isCheck
                adapter!!.notifyDataSetChanged()
                contactSelectedList!!.clear()
                for(i in 0 until contactList!!.size){
                    if(contactList!![position].isCheck){
                        contactSelectedList!!.add(contactList!![position])
                    }
                }
                adapterSelected!!.notifyDataSetChanged()
            }
        })
    }

    private fun setSelectedContact() {
        binding.recyclerviewSelected.layoutManager = GridLayoutManager(
            this, 1,
            GridLayoutManager.HORIZONTAL, false
        )
        contactSelectedList!!.clear()
        adapterSelected = SelectedContactListAdapter(this,contactSelectedList!!)
        binding.recyclerviewSelected.adapter = adapterSelected
        adapterSelected!!.setOnItemClickListener(object : SelectedContactListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, view: View) {
                /*for(i in 0 until contactList!!.size){
                    if(contactList!![i].userid == contactSelectedList!![position].userid){
                        contactList!![i].isCheck = false
                    }
                }*/
                contactSelectedList!!.removeAt(position)
                adapterSelected!!.notifyDataSetChanged()
                adapter!!.notifyDataSetChanged()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
package com.fycus.ui.activity

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fycus.BaseActivity
import com.fycus.R
import com.fycus.databinding.ActivityCardShareBinding
import com.fycus.models.response.ContactModel
import com.fycus.ui.adapter.MyContactListAdapter
import com.fycus.ui.adapter.SelectedContactListAdapter
import com.fycus.utils.ToastObj

class CardShareActivity : BaseActivity() {
    private lateinit var binding : ActivityCardShareBinding
    private var adapter : MyContactListAdapter?= null
    private var adapterSelected : SelectedContactListAdapter?= null
    private var contactList : ArrayList<ContactModel>? = null
    private var contactSelectedList : ArrayList<ContactModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_card_share)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Contacts To Send"
        binding.toolbar.navigationIcon = this.getDrawable(R.drawable.back)
        binding.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView
        searchView!!.setBackgroundColor(Color.WHITE)
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //adapter.getFilter().filter(newText)
                ToastObj.message(this@CardShareActivity,newText)
                return false
            }
        })
        // searchView!!.background = this.getDrawable(R.drawable.edt_round_corner)
       // searchView!!.background = this.getDrawable(R.drawable.edt_round_corner)
        return super.onCreateOptionsMenu(menu)
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
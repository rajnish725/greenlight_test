package com.fycus.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fycus.BaseFragment
import com.fycus.R
import com.fycus.databinding.FragmentMyContactsBinding
import com.fycus.models.response.ContactModel
import com.fycus.ui.activity.HomeActivity
import com.fycus.ui.adapter.MyContactListAdapter

class MyContactsFragment : BaseFragment() {
    private lateinit var binding : FragmentMyContactsBinding
    private var adapter : MyContactListAdapter? = null
    private var contactList : ArrayList<ContactModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_contacts, container, false)
        (requireActivity() as HomeActivity).changeIcon(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactList = ArrayList()
        contactList!!.add(ContactModel(false))
        contactList!!.add(ContactModel(false))
        contactList!!.add(ContactModel(false))
        contactList!!.add(ContactModel(false))
        contactList!!.add(ContactModel(false))
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerviewContacts.layoutManager = layoutManager
        adapter = MyContactListAdapter(requireActivity(), contactList!!)
        binding.recyclerviewContacts.adapter = adapter
    }
}
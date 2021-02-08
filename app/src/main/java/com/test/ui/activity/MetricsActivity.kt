package com.test.ui.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.BaseActivity
import com.test.R
import com.test.databinding.AppMetricsScreenBinding
import com.test.listner.OnItemListener
import com.test.models.MetricsDataModel
import com.test.models.MetricsModel
import com.test.roomDB.MetricDatabase
import com.test.ui.adapter.MetricsAdapter
import com.test.utils.ToastObj
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException


/**
 * Created by Rajnish Yadav on 06 Feb 2021 at 17:14.
 */
class MetricsActivity : BaseActivity(), View.OnClickListener, OnItemListener {
    private val TAG = "MetricsActivity"
    private lateinit var binding: AppMetricsScreenBinding
    var mContext: Context? = null
    private var adapter: MetricsAdapter? = null
    var metricDatabase: MetricDatabase? = null
    var metricsModel: MetricsModel? = null
    var screenNo: Int = 0
    var backClick: Int = 0
    var listener: OnItemListener? = null
    var list: List<MetricsDataModel>? = null
    var isAsc: Boolean = false;
    var pTitle = "Country Performance"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.app_metrics_screen)
        mContext = this
        listener = this
        setLister()
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString())
            }
        })
    }

    fun setLister() {
        binding.imgBack.setOnClickListener(this)
        binding.txtPerformanceZone.setOnClickListener(this)
        binding.txtListHeader.setOnClickListener(this)

    }


    private fun getMetricsData() {
        apiService.getMetricsData(

        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { showProgressDialog() }
            .doOnCompleted { hideProgressDialog() }
            .subscribe(
                { commonResponse ->
                    // utils.simpleAlert(this, "", it.msg.toString())
                    if (commonResponse.ResponseStatus == 200) {
                        setData(commonResponse.mData);
                    } else {
                        Toast.makeText(this, commonResponse.message, Toast.LENGTH_SHORT).show()
                    }
                }, {
                    hideProgressDialog()
                    if (it is ConnectException) {
                        utils.simpleAlert(
                            this,
                            "",
                            getString(R.string.check_network_connection)
                        )
                    } else {
                        utils.simpleAlert(this, "", it.message.toString())
                    }
                }
            )
    }

    private fun setData(mData: MetricsModel?) {
        if (mData != null) {
            metricsModel = mData
            binding.linPerformance.visibility = View.VISIBLE
            binding.txtPerformanceZone.visibility = View.GONE
            binding.recyclerPerformance.layoutManager = LinearLayoutManager(mContext)
            metricDatabase = MetricDatabase.getDataseClient(mContext!!)
            //var check: Int
            CoroutineScope(Dispatchers.IO).launch {
                //val loginDetails = LoginTableModel(username, password)
                metricDatabase!!.loginDao().InsertData(mData)
                //  loginDatabase!!.loginDao().InsertDataM(mData.areaList!!.get(1))
                Log.d(TAG, "setData: inserted ")
            }
            binding.txtTitle.text = pTitle
            binding.txtListHeader.text = "country"
            screenNo = 1
            backClick = 1
            list = metricsModel!!.countryList!!
            adapter =
                MetricsAdapter(mContext!!, list!!, screenNo, listener!!)
            binding.recyclerPerformance.adapter = adapter
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.img_back -> {
                onBackPressed()
            }
            R.id.txt_performance_zone -> {
                getMetricsData()
            }
            R.id.txt_list_header -> {

                if (isAsc) {
                    ascAndDecOrder(isAsc)
                    isAsc = false

                } else {
                    ascAndDecOrder(isAsc)
                    isAsc = true
                }
            }
        }
    }

    fun ascAndDecOrder(isAsc: Boolean) {
        var shortedModel = list;
        if (isAsc) {
            if (binding.txtListHeader.text.toString().equals("Country")) {
                list = list!!.sortedBy { model -> model.country }
                screenNo = 1
            } else if (binding.txtListHeader.text.toString().equals("Zone")) {
                list = list!!.sortedBy { model -> model.zone }
                screenNo = 2
            } else if (binding.txtListHeader.text.toString().equals("Region")) {
                screenNo = 3
                list = list!!.sortedBy { model -> model.region }
            } else if (binding.txtListHeader.text.toString().equals("Area")) {
                screenNo = 4
                list = list!!.sortedBy { model -> model.area }
            } else if (binding.txtListHeader.text.toString().equals("Employee")) {
                list = list!!.sortedBy { model -> model.name }
                adapter = MetricsAdapter(mContext!!, list!!, 5, listener!!)
                binding.recyclerPerformance.adapter = adapter
                return
            }


        } else {
            if (binding.txtListHeader.text.toString().equals("Country")) {
                list = shortedModel!!.sortedByDescending { model -> model.country }
            } else if (binding.txtListHeader.text.toString().equals("Zone")) {
                list = shortedModel!!.sortedByDescending { model -> model.zone }
            } else if (binding.txtListHeader.text.toString().equals("Region")) {
                list = shortedModel!!.sortedByDescending { model -> model.region }
            } else if (binding.txtListHeader.text.toString().equals("Area")) {
                list = shortedModel!!.sortedByDescending { model -> model.area }
            } else if (binding.txtListHeader.text.toString().equals("Employee")) {
                list = shortedModel!!.sortedByDescending { model -> model.name }
            }


        }
        adapter = MetricsAdapter(mContext!!, list!!, screenNo, listener!!)
        binding.recyclerPerformance.adapter = adapter
    }

    override fun onCellClickListener(
        from: String,
        view: View,
        position: Int,
        obj1: Any,
        obj2: Any,
        Ischeck: Boolean
    ) {
        if (!Ischeck) {
            pTitle = binding.txtTitle.text.toString()
            binding.txtTitle.text = obj1.toString() + " Performance"
            manageClick(obj2.toString().toInt(), position)
            //  manageClick(obj2.toString().toInt(), position)
        }
    }

    private fun manageClick(from: Int, position: Int) {
        binding.editTextSearch.visibility = View.GONE
        backClick = from
        Log.d(TAG, "" + screenNo + "manageClick: " + from)
        if (from == 1) {
            //binding.txtTitle.text = ""
            screenNo = 2
            binding.txtListHeader.text = "Zone"
            list = metricsModel!!.zoneList!!
            adapter = MetricsAdapter(mContext!!, list!!, screenNo, listener!!)
        } else if (from == 2) {
            screenNo = 3
            list = metricsModel!!.regionList!!
            binding.txtListHeader.text = "Region"
            adapter = MetricsAdapter(mContext!!, list!!, screenNo, listener!!)

        } else if (from == 3) {
            screenNo = 4
            list = metricsModel!!.areaList!!
            binding.txtListHeader.text = "Area"
            adapter = MetricsAdapter(mContext!!, list!!, screenNo, listener!!)
        } else if (from == 4) {
            //screenNo = 5
            binding.editTextSearch.visibility = View.VISIBLE
            list = metricsModel!!.employeeList!!
            binding.txtListHeader.text = "Employee"
            adapter =
                MetricsAdapter(mContext!!, list!!, 5, listener!!)
        }
        binding.recyclerPerformance.adapter = adapter
    }

    fun backButtonClick() {
        ToastObj.message(mContext!!, "test" + screenNo)
        if (backClick != 0) {
            binding.txtTitle.text = pTitle
            manageClick(backClick - 1, 0)
        } else {
            onBackPressed()
        }
    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        val filterdList: ArrayList<MetricsDataModel> = ArrayList()

        //looping through existing elements
        for (s in list!!) {
            //if the existing elements contains the search input
            if (s.name!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdList.add(s)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter!!.filterList(filterdList)
    }

    override fun onBackPressed() {
        if (backClick == 1 && screenNo != 1) {
            setData(metricsModel)
        } else if (backClick > 1) {
            backButtonClick()
        } else {
            if (backClick == 1) {
                backClick = 0
                binding.linPerformance.visibility = View.GONE
                binding.txtPerformanceZone.visibility = View.VISIBLE
            } else
                finish()
        }
    }
}
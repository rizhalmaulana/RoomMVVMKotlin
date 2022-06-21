package com.rizal.roommvvmkotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizal.roommvvmkotlin.adapter.RecycleAdapter
import com.rizal.roommvvmkotlin.databinding.ActivityMainBinding
import com.rizal.roommvvmkotlin.db.Subscriber
import com.rizal.roommvvmkotlin.db.SubscriberDatabase
import com.rizal.roommvvmkotlin.db.SubscriberRepository
import com.rizal.roommvvmkotlin.viewmodel.SubscriberViewModel
import com.rizal.roommvvmkotlin.viewmodel.SubscriberViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: RecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)

        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding.viewModel = subscriberViewModel
        binding.lifecycleOwner = this

        initRecycleView()

        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecycleView() {
        binding.rvSubscriber.layoutManager = LinearLayoutManager(this)
        adapter = RecycleAdapter({ selectedItem: Subscriber -> clickItem(selectedItem) })
        binding.rvSubscriber.adapter = adapter

        displaySubscriberList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displaySubscriberList() {
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("TAG", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun clickItem(subscriber: Subscriber) {
//        Toast.makeText(applicationContext, "Nama yang dipiih ${subscriber.name}", Toast.LENGTH_SHORT).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}
package com.rizal.roommvvmkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rizal.roommvvmkotlin.R
import com.rizal.roommvvmkotlin.databinding.ListItemBinding
import com.rizal.roommvvmkotlin.db.Subscriber

class RecycleAdapter(private val clickItemListener: (Subscriber) -> Unit): RecyclerView.Adapter<MyViewHodler>() {

    private val subscriberList = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHodler {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)

        return MyViewHodler(binding)
    }

    override fun onBindViewHolder(holder: MyViewHodler, position: Int) {
        holder.bind(subscriberList[position], clickItemListener)
    }

    override fun getItemCount(): Int {
        return subscriberList.size
    }

    fun setList(subscribers: List<Subscriber>) {
        subscriberList.clear()
        subscriberList.addAll(subscribers)
    }

}

class MyViewHodler(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(subscriber: Subscriber, clickItemListener: (Subscriber) -> Unit) {
        binding.itemNama.text = subscriber.name
        binding.itemEmail.text = subscriber.email
        binding.itemCardList.setOnClickListener {
            clickItemListener(subscriber)
        }
    }
}
package com.example.shopyproject.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopyproject.Constants
import com.example.shopyproject.R
import com.example.shopyproject.databinding.ActivityOrderBinding
import com.example.shopyproject.entities.Order
import com.google.firebase.firestore.FirebaseFirestore

class OrderActivity : AppCompatActivity() ,OnOrderListener ,OrderAux{

    private lateinit var binding : ActivityOrderBinding
    private lateinit var adapter: OrderAdapter
    private lateinit var orderSelected : Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setUpRecyclerView() {
        adapter = OrderAdapter(mutableListOf(), this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrderActivity)
            adapter = this@OrderActivity.adapter
        }
    }

    private fun setUpFirestore(){
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.COLL_REQUEST)
            .get()
            .addOnSuccessListener {
                for (document in it){
                    val order = document.toObject(Order::class.java)
                    order.id = document.id
                    adapter.add(order)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al consultar datos", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onStartChat(order: Order) {

    }

    override fun onStatusChanged(order: Order) {

    }

    override fun getOrderSelecter(): Order = orderSelected
}
package com.example.shopyproject.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopyproject.Constants
import com.example.shopyproject.R
import com.example.shopyproject.chat.ChatFragment
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
        setUpRecyclerView()
        setUpFirestore()
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
        orderSelected = order

        val fragment = ChatFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerMain, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onStatusChanged(order: Order) {
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.COLL_REQUEST)
            .document(order.id)
            .update(Constants.PROP_STATUS, order.status)
            .addOnSuccessListener {
                Toast.makeText(this, "Orden Actualizada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getOrderSelecter(): Order = orderSelected
}
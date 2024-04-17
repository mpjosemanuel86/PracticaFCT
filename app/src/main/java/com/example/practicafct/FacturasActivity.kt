package com.example.practicafct
import FacturasAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicafct.databinding.ActivityFacturasBinding
import com.example.practicafct.Facturas


class FacturasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacturasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacturasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val manager = LinearLayoutManager(this)

        binding.recyclerFacturas.layoutManager = manager
        binding.recyclerFacturas.adapter =
            FacturasAdapter(FacturasProvider.facturas) { facturas ->
                onItemSelected(
                   facturas
                )
            }

    }

    private fun onItemSelected(facturas: Facturas) {

    }


}
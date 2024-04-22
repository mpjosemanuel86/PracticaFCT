package com.example.practicafct.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicafct.data.retrofit.InvoiceService
import com.example.practicafct.databinding.ActivityFacturasBinding
import com.example.practicafct.ui.model.adapter.FacturasAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FacturasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacturasBinding
    private lateinit var adapter: FacturasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacturasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        fetchDataFromAPI()
    }

    private fun initRecyclerView() {
        val manager = LinearLayoutManager(this)
        binding.recyclerFacturas.layoutManager = manager
        adapter = FacturasAdapter(emptyList()) { facturas ->
            // Acción al hacer clic en una factura
        }
        binding.recyclerFacturas.adapter = adapter
    }

    private fun fetchDataFromAPI() {
        val invoiceService = InvoiceService()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val facturas = invoiceService.getDataFromAPI()
                facturas?.let {
                    adapter.updateData(it)
                }
            } catch (e: Exception) {
                // Manejar la excepción
                Log.e("FacturasActivity", "Error fetching data from API", e)
                // Puedes mostrar un mensaje de error al usuario si lo deseas
                Toast.makeText(
                    this@FacturasActivity,
                    "Error fetching data from API",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
package com.example.practicafct.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicafct.Facturas
import com.example.practicafct.data.retrofit.InvoiceService
import com.example.practicafct.databinding.ActivityFacturasBinding
import com.example.practicafct.ui.model.adapter.FacturasAdapter
import com.example.practicafct.ui.model.adapter.FacturasViewHolder
import com.example.practicafct.ui.viewmodel.InvoiceActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FacturasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacturasBinding
    private lateinit var adapter: FacturasAdapter
    private val viewModel: InvoiceActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacturasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.filteredInvoices.observe(this) { invoices ->
            initRecyclerView(invoices)
        }

        //fetchDataFromAPI()
    }

    private fun initRecyclerView(invoiceList: List<Facturas>) {
        val manager = LinearLayoutManager(this)
        binding.recyclerFacturas.layoutManager = manager
        adapter = FacturasAdapter(invoiceList) {invoices ->
            //Al tocar debe mostrar un mensaje de función  no implementada.
        }
        binding.recyclerFacturas.adapter = adapter
    }
}
package com.example.practicafct.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practicafct.MyApplication
import com.example.practicafct.data.room.FacturaModelRoom
import com.example.practicafct.databinding.ActivityFacturasBinding
import com.example.practicafct.ui.model.adapter.FacturasAdapter
import com.example.practicafct.ui.viewmodel.FacturaActivityViewModel

class FacturasActivity : AppCompatActivity() {

    private lateinit var facturasAdapter: FacturasAdapter
    private lateinit var rvFacturas: RecyclerView
    private lateinit var binding: ActivityFacturasBinding

    private val viewModel: FacturaActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacturasBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        MyApplication()


        viewModel.filteredFacturasLiveData.observe(this) { facturas->
            initRecyclerView(facturas)
        }

        //fetchDataFromAPI()
    }

    private fun initRecyclerView(facturas: List<FacturaModelRoom>) {
        binding.recyclerFacturas.layoutManager = LinearLayoutManager(this)
        facturasAdapter = FacturasAdapter(facturas)
        binding.recyclerFacturas.adapter = facturasAdapter
    }
}
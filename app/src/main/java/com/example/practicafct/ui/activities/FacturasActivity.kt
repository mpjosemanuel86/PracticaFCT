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

    // Adaptador para la lista de facturas
    private lateinit var facturasAdapter: FacturasAdapter
    // RecyclerView para mostrar la lista de facturas
    private lateinit var rvFacturas: RecyclerView
    // Binding para la actividad
    private lateinit var binding: ActivityFacturasBinding
    // ViewModel asociado a la actividad
    private val viewModel: FacturaActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla el layout de la actividad usando ViewBinding
        binding = ActivityFacturasBinding.inflate(layoutInflater)
        // Habilita la funcionalidad de edge-to-edge
        enableEdgeToEdge()
        // Establece el layout de la actividad
        setContentView(binding.root)
        // Inicializa la aplicación (¿Es necesario?)
        MyApplication()

        // Observa los cambios en las facturas filtradas y actualiza la interfaz de usuario
        viewModel.filteredFacturasLiveData.observe(this) { facturas->
            initRecyclerView(facturas)
        }

        // Llama a la función para obtener y mostrar los datos de la API
        //fetchDataFromAPI()
        binding.switchRetromock.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDataSource(isChecked)
        }
    }


    // Inicializa el RecyclerView con la lista de facturas
    private fun initRecyclerView(facturas: List<FacturaModelRoom>) {
        // Establece un LayoutManager para el RecyclerView
        binding.recyclerFacturas.layoutManager = LinearLayoutManager(this)
        // Crea un adaptador para las facturas y lo establece en el RecyclerView
        facturasAdapter = FacturasAdapter(facturas)
        binding.recyclerFacturas.adapter = facturasAdapter
    }
}


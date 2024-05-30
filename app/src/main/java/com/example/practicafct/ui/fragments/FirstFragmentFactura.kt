package com.example.practicafct.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.practicafct.MyApplication
import com.example.practicafct.R
import com.example.practicafct.data.room.FacturaModelRoom
import com.example.practicafct.databinding.FragmentFirstFacturaBinding
import com.example.practicafct.ui.model.adapter.FacturasAdapter
import com.example.practicafct.ui.viewmodel.FacturaActivityViewModel
import com.example.practicafct.ui.viewmodel.SharedViewModel

class FirstFragmentFactura : Fragment() {

    private lateinit var facturasAdapter: FacturasAdapter
    private lateinit var rvFacturas: RecyclerView
    private lateinit var binding: FragmentFirstFacturaBinding
    private val viewmodel: FacturaActivityViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var originalList: List<FacturaModelRoom> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstFacturaBinding.inflate(inflater, container, false)
        return binding.root
    } // ¿Es necesario inicializar la aplicación aquí?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbarImageButton: ImageButton = view.findViewById(R.id.ibFiltro)
        toolbarImageButton.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragmentFactura_to_secondFragmentFactura)
        }

        // Configurar RecyclerView
        rvFacturas = binding.recyclerFacturas
        rvFacturas.layoutManager = LinearLayoutManager(requireContext())

        // Inicializar el adaptador con una lista vacía para evitar errores de inicialización
        facturasAdapter = FacturasAdapter(emptyList())
        rvFacturas.adapter = facturasAdapter

        // Observa los datos filtrados
        viewmodel.filteredInvoicesLiveData.observe(viewLifecycleOwner) { invoices ->
            originalList = invoices
            Log.d("ListadoFacturas", "Original List Size: ${invoices.size}")
            facturasAdapter.updateFacturas(invoices)
            // Aquí puedes verificar los filtros si es necesario

        }

        val switchRetromock = view.findViewById<SwitchCompat>(R.id.switchRetromock)
        switchRetromock.setOnCheckedChangeListener { _, isChecked ->
            viewmodel.useRetrofitService = isChecked
            viewmodel.searchInvoices()
        }
        viewmodel.filterLiveData.observe(viewLifecycleOwner) { filter ->
            if (filter != null) {
                viewmodel.verificarFiltros()
            }
        }


        // Observa los filtros y aplica el filtrado
        sharedViewModel.filters.observe(viewLifecycleOwner) { filters ->
            if (filters != null) {
                Log.d("Filtros", "Recibidos: $filters")
                viewmodel.applyFilters(filters.maxDate, filters.minDate, filters.maxValueSlider, filters.status)

            }
        }
    }
}

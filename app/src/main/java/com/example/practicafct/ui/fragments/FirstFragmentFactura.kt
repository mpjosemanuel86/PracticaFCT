package com.example.practicafct.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbarImageButton: ImageButton = view.findViewById(R.id.ibFiltro)
        toolbarImageButton.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragmentFactura_to_secondFragmentFactura)
        }

        // Configurar el ImageButton para volver a MainActivity
        val backButton: ImageButton = view.findViewById(R.id.ib_consumo_volver)
        backButton.setOnClickListener {
            requireActivity().finish() // Cierra la actividad actual (el fragmento está en una actividad)
        }

        // Configurar RecyclerView
        rvFacturas = binding.recyclerFacturas
        rvFacturas.layoutManager = LinearLayoutManager(requireContext())

        // Inicializar el adaptador con una lista vacía para evitar errores de inicialización
        facturasAdapter = FacturasAdapter(emptyList())
        rvFacturas.adapter = facturasAdapter

        // Observa los datos filtrados
        viewmodel.filteredInvoicesLiveData.observe(viewLifecycleOwner) { invoices ->
            if (invoices != originalList) {
                originalList = invoices
                facturasAdapter.updateFacturas(invoices)
            }
        }
        val btnLoadFromKtor: Button = view.findViewById(R.id.btKtor)
        btnLoadFromKtor.setOnClickListener {
            // Llama a la función correspondiente en el ViewModel para cargar las facturas desde Ktor
            viewmodel.searchInvoicesWithKtor()
        }

        val switchRetromock = view.findViewById<SwitchCompat>(R.id.switchRetromock)
        switchRetromock.setOnCheckedChangeListener { _, isChecked ->
            viewmodel.useRetrofitService = isChecked
            viewmodel.searchInvoices()
        }

        viewmodel.filterLiveData.observe(viewLifecycleOwner) { filter ->
            filter?.let {
                viewmodel.verificarFiltros()
            }
        }

        // Observa los filtros y aplica el filtrado
        sharedViewModel.filters.observe(viewLifecycleOwner) { filters ->
            filters?.let {
                viewmodel.applyFilters(
                    filters.maxDate,
                    filters.minDate,
                    filters.maxValueSlider,
                    filters.status
                )
            }
        }
    }
}

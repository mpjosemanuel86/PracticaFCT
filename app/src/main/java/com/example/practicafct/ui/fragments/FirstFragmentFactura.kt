package com.example.practicafct.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatImageButton
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

class FirstFragmentFactura : Fragment() {

    private lateinit var facturasAdapter: FacturasAdapter
    private lateinit var rvFacturas: RecyclerView
    private lateinit var binding: FragmentFirstFacturaBinding
    private val viewModel: FacturaActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstFacturaBinding.inflate(inflater, container, false)
        val root = binding.root
        MyApplication() // ¿Es necesario inicializar la aplicación aquí?

        viewModel.filteredFacturasLiveData.observe(viewLifecycleOwner) { facturas ->
            initRecyclerView(facturas)
        }

        binding.switchRetromock.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDataSource(isChecked)
        }

        val btnNavigateFiltros = root.findViewById<AppCompatImageButton>(R.id.ibFiltro)
        btnNavigateFiltros.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragmentFactura_to_secondFragmentFactura)
        }

        return root
    }

    private fun initRecyclerView(facturas: List<FacturaModelRoom>) {
        binding.recyclerFacturas.layoutManager = LinearLayoutManager(requireContext())
        facturasAdapter = FacturasAdapter(facturas)
        binding.recyclerFacturas.adapter = facturasAdapter
    }
}

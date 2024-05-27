package com.example.practicafct.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.practicafct.R
import com.example.practicafct.databinding.FragmentPantallaDetallesBinding
import com.example.practicafct.ui.viewmodel.DatosSmartSolarViewModel


class PantallaDetallesFragment : Fragment() {

    private lateinit var binding: FragmentPantallaDetallesBinding
    private val viewModel: DatosSmartSolarViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPantallaDetallesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.energyDataLiveData.observe(viewLifecycleOwner) { energyData ->
            binding.etCAU.setText(energyData.cau)
            binding.etEstado.setText(energyData.estadoSolicitud)
            binding.etTipo.setText(energyData.tipoAutoconsumo)
            binding.etCompensacion.setText(energyData.compensacionExcedentes)
            binding.etPotencia.setText(energyData.potenciaInstalacion)
        }
    }
}
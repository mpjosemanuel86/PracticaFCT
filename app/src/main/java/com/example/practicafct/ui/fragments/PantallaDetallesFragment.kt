package com.example.practicafct.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.practicafct.R
import com.example.practicafct.databinding.FragmentPantallaDetallesBinding
import com.example.practicafct.ui.fragments.MyDialogFragment
import com.example.practicafct.ui.viewmodel.DatosSmartSolarViewModel

class PantallaDetallesFragment : Fragment() {

    private lateinit var binding: FragmentPantallaDetallesBinding
    private val viewModel: DatosSmartSolarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPantallaDetallesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.energyDataLiveData.observe(viewLifecycleOwner) { energyData ->
            binding.etCAU.setText(energyData.cau)
            binding.etEstado.setText(energyData.estadoSolicitud)
            binding.etTipo.setText(energyData.tipoAutoconsumo)
            binding.etCompensacion.setText(energyData.compensacionExcedentes)
            binding.etPotencia.setText(energyData.potenciaInstalacion)
        }

        // Obtén una referencia al ImageButton que desencadenará la apertura del diálogo
        val btnInfo = binding.ibInfo

        // Configura el clic del ImageButton para abrir el diálogo
        btnInfo.setOnClickListener {
            // Crea una instancia del fragmento del diálogo
            val dialogFragment = MyDialogFragment()
            // Muestra el diálogo
            dialogFragment.show(parentFragmentManager, "MyDialogFragment")
        }
    }
}

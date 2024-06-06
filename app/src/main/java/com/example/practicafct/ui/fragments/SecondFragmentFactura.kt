package com.example.practicafct.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView


import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.example.practicafct.R
import com.example.practicafct.constants.Constants
import com.example.practicafct.core.network.toDateString
import com.example.practicafct.databinding.FragmentSecondFacturaBinding
import com.example.practicafct.ui.model.adapter.Filtros
import com.example.practicafct.ui.viewmodel.FacturaActivityViewModel
import com.example.practicafct.ui.viewmodel.SharedViewModel
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class SecondFragmentFactura : Fragment() {

    private lateinit var btnDesde: Button
    private lateinit var btnHasta: Button
    private lateinit var binding: FragmentSecondFacturaBinding
    private lateinit var textViewSlider: TextView
    private lateinit var tvSliderLeft: TextView
    private lateinit var tvSliderRight: TextView
    private lateinit var slider: Slider
    private lateinit var paid: CheckBox
    private lateinit var canceled: CheckBox
    private lateinit var fixedPayment: CheckBox
    private lateinit var pendingPayment: CheckBox
    private lateinit var paymentPlan: CheckBox
    private val viewModel: FacturaActivityViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolBar = view.findViewById<Toolbar>(R.id.toolbar)
        val cancelButton = view.findViewById<ImageButton>(R.id.btnClose)

        btnDesde = binding.btnDesde
        btnHasta = binding.btnHasta
        btnDesde.setOnClickListener { showDatePickerDesde(it) }
        btnHasta.setOnClickListener { showDatePickerHasta(it) }
        cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragmentFactura_to_firstFragmentFactura)
        }
        initComponents()
        loadFilters()
    }


    private fun initComponents() {
        initSeekBar()
        initCheckBoxes()
        initApplyFiltersButton()
        initResetFilterButton()
    }

    private fun initCheckBoxes() {
        paid = binding.cbPagadas
        canceled = binding.cbAnuladas
        fixedPayment = binding.cbCuotaFija
        pendingPayment = binding.cbPendientePago
        paymentPlan = binding.cbPlanPago
    }

    private fun initResetFilterButton() {
        binding.btnBorrarFiltros.setOnClickListener {
            resetFilters()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondFacturaBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loadFilters() {
        sharedViewModel.minDate.value?.let {
            binding.btnDesde.text = it
        }
        sharedViewModel.maxDate.value?.let {
            binding.btnHasta.text = it
        }
        sharedViewModel.maxValueSlider.value?.let {
            binding.slider.value = it.toFloat()
            textViewSlider.text = it.toInt().toString()
        } ?: run {
            // Valor predeterminado si no hay filtros guardados
            binding.slider.value = 300f
            textViewSlider.text = "300"
        }
        sharedViewModel.status.value?.let {
            binding.cbPagadas.isChecked = it[Constants.PAID_STRING] ?: false
            binding.cbAnuladas.isChecked = it[Constants.CANCELED_STRING] ?: false
            binding.cbCuotaFija.isChecked = it[Constants.FIXED_PAYMENT_STRING] ?: false
            binding.cbPendientePago.isChecked = it[Constants.PENDING_PAYMENT_STRING] ?: false
            binding.cbPlanPago.isChecked = it[Constants.PAYMENT_PLAN_STRING] ?: false
        }
    }

    private fun showDatePickerDesde(view: View) {
        val cal = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                btnDesde.text = selectedDate
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun showDatePickerHasta(view: View) {
        val cal = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                btnHasta.text = selectedDate
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun initSeekBar() {
        textViewSlider = binding.tvSeekBarValue
        tvSliderLeft = binding.tvMinSeekBar
        tvSliderRight = binding.tvMaxSeekBar
        slider = binding.slider

        tvSliderRight.text = "300€"

        slider.valueFrom = 0f
        slider.valueTo = 300f
        slider.value = 300f

        slider.addOnChangeListener { slider, _, _ ->
            val value = slider.value.toInt()
            textViewSlider.text = "$value"
        }
        tvSliderLeft.text = "0"
    }

    private fun initApplyFiltersButton() {
        binding.btnAplicarFiltros.setOnClickListener {
            val maxValueSlider = if (binding.tvSeekBarValue.text.toString().toDouble() == 0.0) 300.0 else binding.tvSeekBarValue.text.toString().toDouble()
            val status = hashMapOf(
                Constants.PAID_STRING to paid.isChecked,
                Constants.CANCELED_STRING to canceled.isChecked,
                Constants.FIXED_PAYMENT_STRING to fixedPayment.isChecked,
                Constants.PENDING_PAYMENT_STRING to pendingPayment.isChecked,
                Constants.PAYMENT_PLAN_STRING to paymentPlan.isChecked
            )

            val minDate: String = if (binding.btnDesde.text == getString(R.string.diaMesAnno))
                LocalDate.ofEpochDay(0).toDateString("dd/MM/yyyy")
            else binding.btnDesde.text.toString()
            val maxDate: String =
                if (binding.btnHasta.text == getString(R.string.diaMesAnno)) LocalDate.now().toDateString("dd/MM/yyyy") else binding.btnHasta.text.toString()

            Log.d("Filtros", "minDate: $minDate, maxDate: $maxDate, maxValueSlider: $maxValueSlider")
            Log.d("Filtros", "status: $status")

            val filters = Filtros(minDate, maxDate, maxValueSlider, status)
            sharedViewModel.setFilters(filters)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun resetFilters() {
        binding.btnDesde.text = getString(R.string.diaMesAnno)
        binding.btnHasta.text = getString(R.string.diaMesAnno)
        binding.slider.value = 300f
        textViewSlider.text = "300"
        binding.cbPagadas.isChecked = false
        binding.cbAnuladas.isChecked = false
        binding.cbCuotaFija.isChecked = false
        binding.cbPendientePago.isChecked = false
        binding.cbPlanPago.isChecked = false
    }
}
package com.example.practicafct.ui.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient


import com.example.practicafct.MyApplication
import com.example.practicafct.constants.Constants
import com.example.practicafct.data.FacturasRepository
import com.example.practicafct.data.room.FacturaModelRoom
import com.example.practicafct.ui.model.adapter.Filtros
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FacturaActivityViewModel : ViewModel() {

    private lateinit var facturaRepository: FacturasRepository
    private var _filteredFacturasLiveData = MutableLiveData<List<FacturaModelRoom>>()
    var useRetrofitService = false
    private var invoices: List<FacturaModelRoom> = emptyList()
    val filteredInvoicesLiveData: LiveData<List<FacturaModelRoom>>
        get() = _filteredFacturasLiveData

    private var _maxAmount: Float = 0.0f
    var maxAmount = 0.0f
        get() = _maxAmount

    private var _filterLiveData = MutableLiveData<Filtros>()
    val filterLiveData: LiveData<Filtros>
        get() = _filterLiveData

    init {
        initRepository()
        searchInvoices()
    }

    fun initRepository() {
        facturaRepository = FacturasRepository()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }

    fun applyFilters(
        maxDate: String,
        minDate: String,
        maxValueSlider: Double,
        status: HashMap<String, Boolean>
    ) {
        val filtro = Filtros(minDate, maxDate, maxValueSlider, status)
        Log.d("InvoiceViewmodel", "Aplicando filtros: $filtro")
        _filterLiveData.postValue(filtro)
        verificarFiltros()
    }

    fun verificarFiltros() {
        Log.d("InvoiceViewmodel", "Verificando filtros")
        val currentFilters = _filterLiveData.value
        if (currentFilters != null) {
            var filteredList = invoices.toList()
            filteredList = verificarDatosFiltro(filteredList, currentFilters)
            filteredList = verificarCheckBox(filteredList, currentFilters)
            filteredList = verificarBalanceBar(filteredList, currentFilters)
            Log.d("InvoiceViewmodel", "Lista filtrada: ${filteredList.size}")
            _filteredFacturasLiveData.postValue(filteredList)
        } else {
            Log.d("InvoiceViewmodel", "No se encontraron filtros para aplicar")
        }
    }

    private fun verificarDatosFiltro(
        filteredList: List<FacturaModelRoom>,
        filters: Filtros
    ): List<FacturaModelRoom> {
        val maxDate = filters.maxDate
        val minDate = filters.minDate
        val filteredListResult = ArrayList<FacturaModelRoom>()

        // Si los filtros de fecha están vacíos o nulos, devuelve la lista original
        if (maxDate.isNullOrEmpty() && minDate.isNullOrEmpty()) {
            return filteredList
        }

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var minDateLocal: Date? = null
        var maxDateLocal: Date? = null

        try {
            if (!minDate.isNullOrEmpty()) {
                minDateLocal = simpleDateFormat.parse(minDate)
            }
            if (!maxDate.isNullOrEmpty()) {
                maxDateLocal = simpleDateFormat.parse(maxDate)
            }
        } catch (e: ParseException) {
            Log.d("Error", "Error al analizar las fechas: ${e.message}")
        }

        Log.d(
            "VerificarDatosFiltro",
            "minDateLocal: $minDateLocal, maxDateLocal: $maxDateLocal"
        )

        for (factura in filteredList) {
            var invoiceDate: Date? = null
            try {
                invoiceDate = simpleDateFormat.parse(factura.fecha)
            } catch (e: ParseException) {
                Log.d("Error", "Error al analizar la fecha de la factura: ${e.message}")
            }

            if (invoiceDate != null) {
                Log.d("VerificarDatosFiltro", "invoiceDate: $invoiceDate")

                val afterMinDate = minDateLocal?.let { invoiceDate.after(it) } ?: true
                val beforeMaxDate = maxDateLocal?.let { invoiceDate.before(it) } ?: true

                if (afterMinDate && beforeMaxDate) {
                    filteredListResult.add(factura)
                }
            }
        }

        return filteredListResult
    }



    private fun verificarCheckBox(
        filteredInvoices: List<FacturaModelRoom>,
        filters: Filtros
    ): List<FacturaModelRoom> {
        val filteredInvoicesCheckBox = ArrayList<FacturaModelRoom>()
        val status = filters.status

        val checkBoxPaid = status[Constants.PAID_STRING] ?: false
        val checkBoxCanceled = status[Constants.CANCELED_STRING] ?: false
        val checkBoxFixedPayment = status[Constants.FIXED_PAYMENT_STRING] ?: false
        val checkBoxPendingPayment = status[Constants.PENDING_PAYMENT_STRING] ?: false
        val checkBoxPaymentPlan = status[Constants.PAYMENT_PLAN_STRING] ?: false

        Log.d(
            "VerificarCheckBox",
            "checkBoxPaid=$checkBoxPaid, checkBoxCanceled=$checkBoxCanceled, checkBoxFixedPayment=$checkBoxFixedPayment, checkBoxPendingPayment=$checkBoxPendingPayment, checkBoxPaymentPlan=$checkBoxPaymentPlan"
        )

        if (checkBoxPaid || checkBoxCanceled || checkBoxFixedPayment || checkBoxPendingPayment || checkBoxPaymentPlan) {
            for (invoice in filteredInvoices) {
                val invoiceState = invoice.descEstado
                val isPaid = invoiceState == "Pagada"
                val isCanceled = invoiceState == "Anuladas"
                val isFixedPayment = invoiceState == "Cuota fija"
                val isPendingPayment = invoiceState == "Pendiente de pago"
                val isPaymentPlan = invoiceState == "planPago"

                Log.d(
                    "VerificarCheckBox",
                    "invoiceState=$invoiceState, isPaid=$isPaid, isCanceled=$isCanceled, isFixedPayment=$isFixedPayment, isPendingPayment=$isPendingPayment, isPaymentPlan=$isPaymentPlan"
                )

                if ((isPaid && checkBoxPaid) || (isCanceled && checkBoxCanceled) || (isFixedPayment && checkBoxFixedPayment) || (isPendingPayment && checkBoxPendingPayment) || (isPaymentPlan && checkBoxPaymentPlan)) {
                    filteredInvoicesCheckBox.add(invoice)
                }
            }
            return filteredInvoicesCheckBox
        } else {
            return filteredInvoices
        }
    }

    private fun verificarBalanceBar(
        filteredList: List<FacturaModelRoom>,
        filters: Filtros
    ): List<FacturaModelRoom> {
        val filteredInvoicesBalanceBar = ArrayList<FacturaModelRoom>()
        val maxValueSlider = filters.maxValueSlider

        Log.d("VerificarBalanceBar", "maxValueSlider=$maxValueSlider")

        if (maxValueSlider > 0) {
            for (factura in filteredList) {
                Log.d(
                    "VerificarBalanceBar",
                    "factura.importeOrdenacion=${factura.importeOrdenacion}"
                )
                if (factura.importeOrdenacion < maxValueSlider) {
                    filteredInvoicesBalanceBar.add(factura)
                }
            }
            return filteredInvoicesBalanceBar
        }
        return filteredList
    }

    fun searchInvoices() {
        viewModelScope.launch {
            invoices = facturaRepository.getAllFacturasFromRoom()
            _filteredFacturasLiveData.postValue(invoices) // Actualizar lista original

            try {
                if (isInternetAvailable()) {
                    if (useRetrofitService) {
                        // Si hay conexión a Internet, usar Retrofit
                        facturaRepository.fetchAndInsertFacturasFromMock()
                        Log.d("Retromock", "Usando Retromock")
                    } else {
                        facturaRepository.fetchAndInsertFacturasFromAPI()
                        Log.d("Retrofit", "Usando Retrofit")
                    }
                } else {
                    // Si no hay conexión a Internet, usar Retromock
                    facturaRepository.fetchAndInsertFacturasFromMock()
                    Log.d("Retromock", "Usando Retromock")
                }
                invoices = facturaRepository.getAllFacturasFromRoom()
                _filteredFacturasLiveData.postValue(invoices) // Actualizar lista original después de obtener nuevas facturas
                verificarFiltros() // Aplicar filtros después de actualizar las facturas
            } catch (e: Exception) {
                Log.d("Error", e.printStackTrace().toString())
            }
        }
    }
    fun searchInvoicesWithKtor() {
        viewModelScope.launch {
            try {
                val newInvoices = withContext(Dispatchers.IO) {
                    val client = HttpClient()
                    val response: String = client.get("https://viewnextandroid2.wiremockapi.cloud/")
                    Json.decodeFromString<List<FacturaModelRoom>>(response)
                }
                invoices = newInvoices
                _filteredFacturasLiveData.postValue(invoices)
                verificarFiltros()
            } catch (e: Exception) {
                Log.e("FacturaActivityViewModel", "Error al obtener las facturas: ${e.message}")
            }
        }
    }
}

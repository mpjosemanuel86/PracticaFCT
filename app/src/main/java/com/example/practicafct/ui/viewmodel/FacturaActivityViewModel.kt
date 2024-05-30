package com.example.practicafct.ui.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.practicafct.MyApplication
import com.example.practicafct.constants.Constants
import com.example.practicafct.data.FacturasRepository


import com.example.practicafct.data.room.FacturaModelRoom
import com.example.practicafct.ui.model.adapter.Filtros
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// ViewModel para la actividad de facturas
class FacturaActivityViewModel() : ViewModel() {

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



    private var useAPI = false
    init {
        initRepository()
        fetchInvoices()
    }

    fun initRepository() {
        facturaRepository = FacturasRepository()
    }

    fun fetchInvoices() {
        viewModelScope.launch {
            // Obtiene las facturas almacenadas localmente
            _filteredFacturasLiveData.postValue(facturaRepository.getAllFacturasFromRoom())
            try {
                // Verifica si hay conexión a Internet
                if (isInternetAvailable()) {
                    // Si hay conexión a Internet, obtiene las facturas de la API o de la base de datos local
                    when (useAPI) {
                        true -> facturaRepository.fetchAndInsertFacturasFromAPI()
                        false -> facturaRepository.fetchAndInsertFacturasFromMock() // Hasta que se implemente retromock
                    }
                    // Actualiza la lista de facturas

                    _filteredFacturasLiveData.postValue(facturaRepository.getAllFacturasFromRoom())
                }
            } catch (e: Exception) {
                Log.d("Error", e.printStackTrace().toString())
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        // Verifica si hay una conexión disponible y si es Wi-Fi, celular o Ethernet
        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }

    fun searchInvoices() {
        viewModelScope.launch {
            invoices = facturaRepository.getAllFacturasFromRoom()
            _filteredFacturasLiveData.postValue(invoices)
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
                _filteredFacturasLiveData.postValue(invoices)
            } catch (e: Exception) {
                Log.d("Error", e.printStackTrace().toString())
            }
        }
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

    private fun verificarDatosFiltro(filteredList: List<FacturaModelRoom>, filters: Filtros): List<FacturaModelRoom> {
        val maxDate = filters.maxDate
        val minDate = filters.minDate
        val filteredListResult = ArrayList<FacturaModelRoom>()

        if (!maxDate.isNullOrEmpty() && !minDate.isNullOrEmpty()) {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            var minDateLocal: Date? = null
            var maxDateLocal: Date? = null

            try {
                minDateLocal = simpleDateFormat.parse(minDate)
                maxDateLocal = simpleDateFormat.parse(maxDate)
            } catch (e: ParseException) {
                Log.d("Error", "Error al analizar las fechas: ${e.message}")
            }

            Log.d("VerificarDatosFiltro", "minDateLocal: $minDateLocal, maxDateLocal: $maxDateLocal")

            for (factura in filteredList) {
                var invoiceDate = Date()
                try {
                    invoiceDate = simpleDateFormat.parse(factura.fecha)!!
                } catch (e: ParseException) {
                    Log.d("Error", "Error al analizar la fecha de la factura: ${e.message}")
                }

                Log.d("VerificarDatosFiltro", "invoiceDate: $invoiceDate")

                if (invoiceDate.after(minDateLocal) && invoiceDate.before(maxDateLocal)) {
                    filteredListResult.add(factura)
                }
            }
        }

        return filteredListResult
    }

    private fun verificarCheckBox(filteredInvoices: List<FacturaModelRoom>, filters: Filtros): List<FacturaModelRoom> {
        val filteredInvoicesCheckBox = ArrayList<FacturaModelRoom>()
        val status = filters.status
        //Se obtienen los estados de las CheckBoxes.
        val checkBoxPaid = status[Constants.PAID_STRING] ?: false
        val checkBoxCanceled = status[Constants.CANCELED_STRING] ?: false
        val checkBoxFixedPayment = status[Constants.FIXED_PAYMENT_STRING] ?: false
        val checkBoxPendingPayment = status[Constants.PENDING_PAYMENT_STRING] ?: false
        val checkBoxPaymentPlan = status[Constants.PAYMENT_PLAN_STRING] ?: false

        Log.d("VerificarCheckBox", "checkBoxPaid=$checkBoxPaid, checkBoxCanceled=$checkBoxCanceled, checkBoxFixedPayment=$checkBoxFixedPayment, checkBoxPendingPayment=$checkBoxPendingPayment, checkBoxPaymentPlan=$checkBoxPaymentPlan")

        if (checkBoxPaid || checkBoxCanceled || checkBoxFixedPayment || checkBoxPendingPayment || checkBoxPaymentPlan) {
            for (invoice in filteredInvoices) {
                val invoiceState = invoice.descEstado
                val isPaid = invoiceState == "Pagada"
                val isCanceled = invoiceState == "Anuladas"
                val isFixedPayment = invoiceState == "Cuota fija"
                val isPendingPayment = invoiceState == "Pendiente de pago"
                val isPaymentPlan = invoiceState == "planPago"

                Log.d("VerificarCheckBox", "invoiceState=$invoiceState, isPaid=$isPaid, isCanceled=$isCanceled, isFixedPayment=$isFixedPayment, isPendingPayment=$isPendingPayment, isPaymentPlan=$isPaymentPlan")

                if ((isPaid && checkBoxPaid) || (isCanceled && checkBoxCanceled) || (isFixedPayment && checkBoxFixedPayment) || (isPendingPayment && checkBoxPendingPayment) || (isPaymentPlan && checkBoxPaymentPlan)) {
                    filteredInvoicesCheckBox.add(invoice)
                }
            }
            return filteredInvoicesCheckBox
        } else {
            return filteredInvoices
        }
    }

    private fun verificarBalanceBar(filteredList: List<FacturaModelRoom>, filters: Filtros): List<FacturaModelRoom> {
        val filteredInvoicesBalanceBar = ArrayList<FacturaModelRoom>()
        val maxValueSlider = filters.maxValueSlider

        Log.d("VerificarBalanceBar", "maxValueSlider=$maxValueSlider")

        if (maxValueSlider > 0) {  // Ignorar el filtro si maxValueSlider es 0
            for (factura in filteredList) {
                Log.d("VerificarBalanceBar", "factura.importeOrdenacion=${factura.importeOrdenacion}")
                if (factura.importeOrdenacion < maxValueSlider) {
                    filteredInvoicesBalanceBar.add(factura)
                }
            }
            return filteredInvoicesBalanceBar
        }
        return filteredList
    }

fun toggleDataSource(useAPI: Boolean) {
    this.useAPI = useAPI
    fetchInvoices() // Vuelve a obtener las facturas según el nuevo origen de datos
}
}

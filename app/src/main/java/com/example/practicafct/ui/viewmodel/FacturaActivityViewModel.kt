package com.example.practicafct.ui.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.infinum.retromock.Retromock
import com.example.practicafct.MyApplication
import com.example.practicafct.MyApplication.Companion.context
import com.example.practicafct.data.FacturasRepository
import com.example.practicafct.data.room.FacturaModelRoom
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.security.Provider


// ViewModel para la actividad de facturas
class FacturaActivityViewModel() : ViewModel() {

    private lateinit var facturaRepository: FacturasRepository

    // Lista de facturas filtradas
    private val _filteredFacturasLiveData = MutableLiveData<List<FacturaModelRoom>>()
    val filteredFacturasLiveData: LiveData<List<FacturaModelRoom>>
        get() = _filteredFacturasLiveData



    // Flag para indicar si se debe usar la API para obtener las facturas
    private var useAPI = false

    init {
        initRepository()
        fetchInvoices()
    }

    // Inicializa el repositorio de facturas
    fun initRepository() {
        facturaRepository = FacturasRepository()
    }

    // Función para obtener las facturas
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

    // Función para verificar la disponibilidad de conexión a Internet
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
}

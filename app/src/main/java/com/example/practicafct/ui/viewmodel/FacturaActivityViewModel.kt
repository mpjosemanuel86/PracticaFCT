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
import com.example.practicafct.data.FacturasRepository
import com.example.practicafct.data.room.FacturaModelRoom
import kotlinx.coroutines.launch

class FacturaActivityViewModel: ViewModel() {

    private lateinit var facturaRepository: FacturasRepository

    private var facturas: List<FacturaModelRoom> = emptyList()

    private val _filteredFacturasLiveData = MutableLiveData<List<FacturaModelRoom>>()
    val filteredFacturasLiveData: LiveData<List<FacturaModelRoom>>
        get() = _filteredFacturasLiveData


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
            _filteredFacturasLiveData.postValue(facturaRepository.getAllFacturasFromRoom())
            try {
                if (isInternetAvailable()) {
                    when (useAPI) {
                        true -> facturaRepository.fetchAndInsertFacturasFromAPI()
                        //Hasta que implemente retromock
                        false -> facturaRepository.fetchAndInsertFacturasFromAPI()
                    }
                    facturas = facturaRepository.getAllFacturasFromRoom()
                    _filteredFacturasLiveData.postValue(facturas)


                }
            } catch (e: Exception) {
                Log.d("Error", e.printStackTrace().toString())
            }

        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network=connectivityManager.activeNetwork
        val capabilities=connectivityManager.getNetworkCapabilities(network)

        return capabilities!=null&&(
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }



}

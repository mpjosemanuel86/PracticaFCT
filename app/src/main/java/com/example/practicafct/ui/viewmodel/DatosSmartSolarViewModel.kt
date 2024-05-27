package com.example.practicafct.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicafct.data.FacturasRepository
import com.example.practicafct.data.room.DatosSmartSolarRoom
import kotlinx.coroutines.launch

class DatosSmartSolarViewModel: ViewModel() {

    private lateinit var appRepository: FacturasRepository
    private val _energyDataLiveData = MutableLiveData<DatosSmartSolarRoom>()
    val energyDataLiveData: LiveData<DatosSmartSolarRoom>
        get() = _energyDataLiveData

    init {
        initRepository()
        fetchDatosSmartSolarData()
    }

    private fun initRepository(){
        appRepository = FacturasRepository()
    }

    fun fetchDatosSmartSolarData(){
        viewModelScope.launch {
            appRepository.fetchAndInsertDatosSmartSolarFromMock()
            _energyDataLiveData.postValue(appRepository.getDatosSmartSolarFromRoom())
        }
    }

}
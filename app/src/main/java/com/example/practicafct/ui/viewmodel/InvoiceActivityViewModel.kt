package com.example.practicafct.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicafct.Facturas
import com.example.practicafct.data.InvoiceRepository
import kotlinx.coroutines.launch

class InvoiceActivityViewModel: ViewModel() {
    private lateinit var invoiceRepository: InvoiceRepository
    private val _filteredInvoices = MutableLiveData<List<Facturas>>()
    val filteredInvoices: LiveData<List<Facturas>>
        get() = _filteredInvoices

    init {
        initRepository()
        fetchInvoices()
    }

    fun initRepository() {
        invoiceRepository = InvoiceRepository()
    }

    fun fetchInvoices() {
        viewModelScope.launch{
            _filteredInvoices.postValue(invoiceRepository.fetchDataFromAPI())
        }
    }
}
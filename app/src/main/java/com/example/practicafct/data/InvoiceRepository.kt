package com.example.practicafct.data

import android.util.Log
import android.widget.Toast
import com.example.practicafct.Facturas
import com.example.practicafct.data.retrofit.InvoiceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InvoiceRepository {
    val api = InvoiceService()
    suspend fun fetchDataFromAPI(): List<Facturas>? {
        val invoices = api.getDataFromAPI() ?: emptyList()
        return invoices
    }
}
package com.example.practicafct.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practicafct.ui.model.adapter.Filtros

class SharedViewModel : ViewModel() {
    private val _filters = MutableLiveData<Filtros>()
    val filters: LiveData<Filtros> get() = _filters

    private val _minDate = MutableLiveData<String>()
    val minDate: LiveData<String> get() = _minDate

    private val _maxDate = MutableLiveData<String>()
    val maxDate: LiveData<String> get() = _maxDate

    private val _maxValueSlider = MutableLiveData<Double>()
    val maxValueSlider: LiveData<Double> get() = _maxValueSlider

    private val _status = MutableLiveData<HashMap<String, Boolean>>()
    val status: LiveData<HashMap<String, Boolean>> get() = _status

    fun setFilters(filters: Filtros) {
        Log.d("SharedViewModel", "Set Filters: $filters")
        _filters.value = filters
        _minDate.value = filters.minDate
        _maxDate.value = filters.maxDate
        _maxValueSlider.value = filters.maxValueSlider
        _status.value = filters.status
    }
}
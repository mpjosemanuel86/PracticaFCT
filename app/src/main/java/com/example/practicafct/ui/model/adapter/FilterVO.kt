package com.example.practicafct.ui.model.adapter


data class FilterVO(
    var maxDate: String = "",
    var minDate: String = "",
    var maxValueSlider: Double = 0.0,
    var status: HashMap<String, Boolean> = HashMap()
)

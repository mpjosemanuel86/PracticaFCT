package com.example.practicafct.ui.model.adapter

data class Filtros(
    val minDate: String,
    val maxDate: String,
    val maxValueSlider: Double,
    val status: HashMap<String, Boolean>
) {
    override fun toString(): String {
        return "Filters(minDate='$minDate', maxDate='$maxDate', maxValueSlider=$maxValueSlider, status=$status)"
    }
}

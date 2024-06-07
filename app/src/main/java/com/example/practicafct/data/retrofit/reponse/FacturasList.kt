package com.example.practicafct.data.retrofit.reponse


import com.example.practicafct.data.room.FacturaModelRoom

data class FacturasList(
    var numFacturas: Int,
    var facturas: List<FacturaModelRoom>
) {
}
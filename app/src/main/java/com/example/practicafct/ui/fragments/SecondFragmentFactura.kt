package com.example.practicafct.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.example.practicafct.R

class SecondFragmentFactura : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        val view = inflater.inflate(R.layout.fragment_second_factura, container, false)

        // Obtener referencias a la SeekBar y al TextView
        val seekBar = view.findViewById<SeekBar>(R.id.seekBar)
        val tvSeekBarValue = view.findViewById<TextView>(R.id.tvSeekBarValue)

        // Configurar el Listener para la SeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = progress + 1
                // Actualizar el valor del TextView con el valor actual de la SeekBar
                tvSeekBarValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Método vacío requerido por la interfaz
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Método vacío requerido por la interfaz
            }
        })

        // Retornar la vista del fragmento
        return view
    }
}

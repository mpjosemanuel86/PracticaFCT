package com.example.practicafct.ui.model.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.practicafct.ui.fragments.PantallaDetallesFragment
import com.example.practicafct.ui.fragments.PantallaEnergiaFragment
import com.example.practicafct.ui.fragments.PantallaInstalacionFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3 // Número de pestañas

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PantallaInstalacionFragment()
            1 -> PantallaEnergiaFragment()
            else -> PantallaDetallesFragment()
        }
    }
}

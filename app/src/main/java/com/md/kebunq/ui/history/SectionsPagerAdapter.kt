package com.md.kebunq.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }

    private var arguments: Bundle? = null

    fun setArguments(bundle: Bundle) {
        arguments = bundle
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 ->{
                fragment = HasilAnalisisFragment()
                fragment.arguments = arguments
            }
            1 -> {
                fragment = SaranPengobatanFragment()
                fragment.arguments = arguments
            }
        }
        return fragment as Fragment
    }
}
package com.example.book

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.book.ui.home.HistoryFragment
import com.example.book.ui.home.SearchFragment

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchFragment()
            1 -> HistoryFragment()
            else -> throw IllegalArgumentException("ポジションエラー")
        }
    }
}
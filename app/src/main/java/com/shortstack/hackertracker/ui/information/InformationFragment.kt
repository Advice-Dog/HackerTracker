package com.shortstack.hackertracker.ui.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.shortstack.hackertracker.R
import com.shortstack.hackertracker.Status
import com.shortstack.hackertracker.ui.HackerTrackerViewModel
import com.shortstack.hackertracker.ui.activities.MainActivity
import com.shortstack.hackertracker.ui.information.faq.FAQFragment
import com.shortstack.hackertracker.ui.information.info.InfoFragment
import com.shortstack.hackertracker.ui.information.speakers.SpeakersFragment
import com.shortstack.hackertracker.ui.information.vendors.VendorsFragment
import com.shortstack.hackertracker.ui.information.categories.CategoriesFragment
import kotlinx.android.synthetic.main.fragment_information.*

class InformationFragment : Fragment() {

    companion object {
        private const val INFO = 0
        private const val CATEGORIES = 1
        private const val FAQ = 2
        private const val SPEAKERS = 3
        private const val VENDORS = 4


        fun newInstance(): InformationFragment {
            return InformationFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener {
            (context as MainActivity).openNavDrawer()
        }

        tabs.apply {
            tabGravity = TabLayout.GRAVITY_START
            setupWithViewPager(pager)
        }

        pager.offscreenPageLimit = 4

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {}

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }
        })

        val viewModel = ViewModelProvider(this)[HackerTrackerViewModel::class.java]
        viewModel.conference.observe(viewLifecycleOwner, Observer {
            val fm = activity?.supportFragmentManager ?: return@Observer
            if (it.status == Status.SUCCESS) {
                val adapter = PagerAdapter(fm, it.data!!.code)
                pager.adapter = adapter
            }
        })
    }

    class PagerAdapter(fm: FragmentManager, private val conference: String) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            val index = if (conference.contains("DEFCON")) {
                position
            } else {
                position + 2
            }

            return when (index) {
                INFO -> InfoFragment.newInstance()
                CATEGORIES -> CategoriesFragment.newInstance()
                SPEAKERS -> SpeakersFragment.newInstance()
                FAQ -> FAQFragment.newInstance()
                VENDORS -> VendorsFragment.newInstance()
                else -> throw IndexOutOfBoundsException("Position out of bounds: $index")
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            val index = if (conference.contains("DEFCON")) {
                position
            } else {
                position + 2
            }

            return when (index) {
                INFO -> "Event"
                CATEGORIES -> "Categories"
                FAQ -> "FAQ"
                SPEAKERS -> "Speakers"
                VENDORS -> "Vendors"
                else -> throw IndexOutOfBoundsException("Position out of bounds: $index")
            }
        }

        override fun getCount(): Int {
            if (conference.contains("DEFCON"))
                return 5
            return 3
        }
    }
}
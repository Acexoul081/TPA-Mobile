package edu.bluejack20_1.gogames.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import edu.bluejack20_1.gogames.R
import kotlinx.android.synthetic.main.activity_update_profile.*

class UpdateProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        initViewPager()
    }

    private fun initViewPager(){
        var adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Update1Fragment(), "Update profile")
        adapter.addFragment(Update2Fragment(), "Add Social Media")
        view_pager_update_profile.adapter = adapter

    }

//    private fun initViewPagerListener(){
//        view_pager_update_profile.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onPageSelected(position: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }
}
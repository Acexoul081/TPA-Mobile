package edu.bluejack20_1.gogames.allCommunity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import edu.bluejack20_1.gogames.R
import kotlinx.android.synthetic.main.fragment_thread_category.*


class threadCategory : Fragment(R.layout.fragment_thread_category) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnListener()
    }
    private fun btnListener(){
        DeveloperCategory.setOnClickListener {
            val fragment = DeveloperForum("Developer")
            val act: FragmentActivity? = activity
            act?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }
        GameCategory.setOnClickListener {
            val fragment = DeveloperForum("Game")
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.mainFragment, fragment)
                    addToBackStack(null)
                    commit()
                }
            }
        OtherCategory.setOnClickListener {
            val fragment = DeveloperForum("Other")
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }
    }
}

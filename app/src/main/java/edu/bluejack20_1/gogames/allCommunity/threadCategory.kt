package edu.bluejack20_1.gogames.allCommunity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
        developerLayout.setOnClickListener {
            val fragment = DeveloperForum("Developer")
            val act: FragmentActivity? = activity
            act?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }
        gamesLayout.setOnClickListener {
            val fragment = DeveloperForum("Game")
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.mainFragment, fragment)
                    addToBackStack(null)
                    commit()
                }
            }
        otherLayout.setOnClickListener {
            val fragment = DeveloperForum("Other")
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }

        btnSearch.setOnClickListener {
            var keyword: String = searchText.editableText.toString()
            if(keyword != ""){
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    val fragment = SearchFragment(keyword.toUpperCase())
                    replace(R.id.mainFragment, fragment)
                    addToBackStack(null)
                    commit()
                }
            }else{
                Log.d("test", "test")
                Toast.makeText(activity, "search must be filled !", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

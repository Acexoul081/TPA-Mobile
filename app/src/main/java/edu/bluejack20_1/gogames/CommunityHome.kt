package edu.bluejack20_1.gogames

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import edu.bluejack20_1.gogames.allCommunity.createThread.CreateThread
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import kotlinx.android.synthetic.main.home_community.*

class CommunityHome : Fragment(R.layout.home_community) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharePref = PreferencesConfig(context as Context)
        if(sharePref.getUserID() == null){
            addThread.visibility = View.GONE
        }
        addThread.setOnClickListener {
            val fragment = CreateThread()
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }

    }
}
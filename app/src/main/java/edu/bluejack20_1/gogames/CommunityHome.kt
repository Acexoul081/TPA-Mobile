package edu.bluejack20_1.gogames

import android.os.Bundle
import androidx.fragment.app.Fragment
import edu.bluejack20_1.gogames.allCommunity.createThread.CreateThread
import kotlinx.android.synthetic.main.home_community.*

class CommunityHome : Fragment(R.layout.home_community) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
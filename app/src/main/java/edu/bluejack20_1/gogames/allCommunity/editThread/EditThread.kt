package edu.bluejack20_1.gogames.allCommunity.editThread

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import edu.bluejack20_1.gogames.R
import kotlinx.android.synthetic.main.fragment_edit_thread.*


class EditThread : Fragment(R.layout.fragment_edit_thread) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BtnCancel.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }
    }
}
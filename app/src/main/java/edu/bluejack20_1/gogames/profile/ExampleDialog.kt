package edu.bluejack20_1.gogames.profile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import edu.bluejack20_1.gogames.R
import kotlinx.android.synthetic.main.layout_dialog.*

class ExampleDialog : AppCompatDialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater = activity!!.layoutInflater
        val view : View = inflater.inflate(R.layout.layout_dialog, null)
        builder.setView(view)
//            .setTitle("Update Profile")
//        view.okUpdate_btn.setOnClickListener{
//            Log.d("test","masuk")
//        }

        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(activity!!.supportFragmentManager)
        adapter.addFragment(Update1Fragment(), "One")
        adapter.addFragment(Update2Fragment(),"Two")
        view_pager2.adapter = adapter
        pager_layout.setupWithViewPager(view_pager2)
    }



}
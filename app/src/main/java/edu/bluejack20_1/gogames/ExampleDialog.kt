package edu.bluejack20_1.gogames

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.layout_dialog.view.*

class ExampleDialog : AppCompatDialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater = activity!!.layoutInflater
        val view : View = inflater.inflate(R.layout.layout_dialog, null)
        builder.setView(view)
            .setTitle("Update Profile")
        view.okUpdate_btn.setOnClickListener{
            Log.d("test","masuk")
        }

        return builder.create()
    }
}
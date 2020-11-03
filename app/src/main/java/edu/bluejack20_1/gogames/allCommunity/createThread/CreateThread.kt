package edu.bluejack20_1.gogames.allCommunity.createThread

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import edu.bluejack20_1.gogames.CommunityHome
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_thread.*
import java.text.DateFormat
import java.util.*


class CreateThread : Fragment(R.layout.fragment_create_thread) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancel.setOnClickListener {
            val fragment = CommunityHome()
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }

        submit.setOnClickListener {
            insertData()
            Toast.makeText(context, "Create Successful", Toast.LENGTH_LONG).show()
        }
    }

    private fun insertData() {
        val title = createTitle.text.toString()
        val description = createDescription.text.toString()
        val category = createCategory.selectedItem.toString()
        val pref = PreferencesConfig(context as Context)
        val uuid: UUID = UUID.randomUUID()
        if(title != "" && description != ""){
            val reff: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Thread").child(category)
            val currDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
            var thread = DataThread(uuid.toString(), pref.getUserID()!!, pref.getUsername()!!, title, description, category, 0, 0, 0, currDate, 0)
            reff.child(uuid.toString()).setValue(thread)
        }else{
            Toast.makeText(context as Context, "title or description must be filled !", Toast.LENGTH_LONG).show()
        }

    }

}
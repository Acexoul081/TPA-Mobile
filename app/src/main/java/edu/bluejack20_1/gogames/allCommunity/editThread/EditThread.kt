package edu.bluejack20_1.gogames.allCommunity.editThread

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.allCommunity.DeveloperThread.DeveloperThread
import kotlinx.android.synthetic.main.fragment_edit_thread.*


class EditThread(var id: String, var title: String, var desc: String, var category: String, var like: Number, var dislike: Number, var userID: String) : Fragment(R.layout.fragment_edit_thread) {

    lateinit var reff: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BtnCancel.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                val fragment = DeveloperThread(id, title, desc, category, like, dislike, userID)
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit();
            }
        }

        BtnSubmit.setOnClickListener {
            var title: String? = editTitle.text?.toString()
            var description: String? = editDescription.text?.toString()

            if(title == "" && description == ""){
                Toast.makeText(activity, "title / description must be filled !", Toast.LENGTH_SHORT).show();
            }else{
                reff = FirebaseDatabase.getInstance().reference.child("Thread").child(category).child(id)
                reff.child("title").setValue(title)
                reff.child("description").setValue(description)
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    val fragment = DeveloperThread(id, title!!, description!!, category, like, dislike, userID)
                    replace(R.id.mainFragment, fragment)
                    addToBackStack(null)
                    commit();
                }
            }

        }
    }
}
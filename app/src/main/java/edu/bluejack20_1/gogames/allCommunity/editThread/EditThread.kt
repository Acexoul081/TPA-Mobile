package edu.bluejack20_1.gogames.allCommunity.editThread

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import edu.bluejack20_1.gogames.CommunityHome
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.allCommunity.DeveloperForum
import edu.bluejack20_1.gogames.allCommunity.DeveloperThread.DeveloperThread
import edu.bluejack20_1.gogames.allCommunity.threadCategory
import kotlinx.android.synthetic.main.fragment_edit_thread.*


class EditThread(var id: String, var title: String, var desc: String, var category: String, var like: Number, var dislike: Number, var userID: String) : Fragment(R.layout.fragment_edit_thread) {

    lateinit var reff: DatabaseReference
    lateinit var titleE: String
    lateinit var descriptionE: String

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
            titleE = editTitle.text?.toString().toString()
            descriptionE = editDescription.text?.toString().toString()

            if(titleE == "" && descriptionE == ""){
                Toast.makeText(activity, "title / description must be filled !", Toast.LENGTH_SHORT).show();
            }else{
                reff = FirebaseDatabase.getInstance().reference.child("Thread").child(category).child(id)
                if(editCategory.selectedItem.toString() == category){
                    reff.child("title").setValue(titleE)
                    reff.child("description").setValue(descriptionE)
                    redirect(false)
                }else{
                    reff.child("title").setValue(titleE)
                    reff.child("description").setValue(descriptionE)
                    reff.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                var obj = snapshot.value
                                FirebaseDatabase.getInstance().reference.child("Thread").child(editCategory.selectedItem.toString()).child(id).setValue(obj)
                                reff.removeValue()
                                redirect(true)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

        }

        btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(activity as Context)
                .setTitle("Delete")
                .setMessage("Are you sure want to delete this thread ?")
                .setNeutralButton("Cancel") { dialog, which ->

                }
                .setNegativeButton("Decline") { dialog, which ->
                    // Respond to negative button press

                }
                .setPositiveButton("Accept") { dialog, which ->
                    reff = FirebaseDatabase.getInstance().reference.child("Thread").child(category).child(id)
                    reff.removeValue()
                    activity?.supportFragmentManager?.beginTransaction()?.apply {
                        var fragment = CommunityHome()
                        replace(R.id.mainFragment, fragment)
                        commit()
                    }
                }
                .show()
        }
    }

    fun redirect(boolean: Boolean){
        if(!boolean){
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                val fragment = DeveloperThread(id, titleE!!, descriptionE!!, category, like, dislike, userID)
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit();
            }
        }else{
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                val fragment = DeveloperThread(id, titleE!!, descriptionE!!, editCategory.selectedItem.toString(), like, dislike, userID)
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit();
            }
        }
    }

}
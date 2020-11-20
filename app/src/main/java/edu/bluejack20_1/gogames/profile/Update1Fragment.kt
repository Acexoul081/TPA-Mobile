package edu.bluejack20_1.gogames.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack20_1.gogames.ProfileFragment
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_update1.*

class Update1Fragment : Fragment() {
    private lateinit var database : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
            //.child("Users").child(user?.uid.toString())
        var user = User.getInstance()
        ET_update_username.setText(user.getUsername())
        ET_update_description.setText(user.getDescription())

        btn_update_profile.setOnClickListener{
            val newUsername = ET_update_username.text.toString()
            val newDescription = ET_update_description.text.toString()
            val newPassword = ET_update_password.text.toString()
            database.child("Users").child(user.getUid()).child("username").setValue(newUsername)
            database.child("Users").child(user.getUid()).child("description").setValue(newDescription)
            database.child("Users").child(user.getUid()).child("password").setValue(newPassword).addOnSuccessListener {

            }
            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(newUsername)
                .build()
            progressbar_update.visibility = View.VISIBLE
            if(newPassword != ""){
                FirebaseAuth.getInstance().currentUser?.updatePassword(newPassword)
            }

            FirebaseAuth.getInstance().currentUser?.updateProfile(updates)?.addOnCompleteListener{
                progressbar_update.visibility = View.INVISIBLE
                val pref = PreferencesConfig(activity as Context)
                pref.putUser(user.getUid(), User.getInstance().getUsername()
                    , User.getInstance().getImagePath()
                    , User.getInstance().getDescription()
                    , User.getInstance().getGenre()
                    , User.getInstance().getSocmed()
                    , User.getInstance().getEmail())
            }
        }
    }


}
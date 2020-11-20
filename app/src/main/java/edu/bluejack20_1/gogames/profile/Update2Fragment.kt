package edu.bluejack20_1.gogames.profile

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack20_1.gogames.NewsActivity
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import kotlinx.android.synthetic.main.fragment_update2.*


class Update2Fragment : Fragment() {
    private lateinit var user: User
    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        var counter : Int = 0
        val editTextList : MutableList<EditText> = mutableListOf<EditText>()
        user = User.getInstance()
        if(user.getSocmed().isNotEmpty()){
            user.getSocmed().forEach{
                val editText : EditText = EditText(context)
                editText.id = counter
                editText.hint = "Enter social media account"
                editText.setText(it.getLink())
                editText.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
                layout_social_list.addView(editText)
                editTextList.add(editText)
                counter++
            }
        }
        val button : Button = Button(context)
        button.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        button.text = "Add new social media"
        button.setOnClickListener{
            val editText : EditText = EditText(context)
            editText.id = counter
            editText.hint = "Enter social media account"
            editText.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            layout_social_list.addView(editText)
            editTextList.add(editText)
            counter++
        }
        layout_social_list.addView(button)

        button_update_social_media.setOnClickListener{
            var listOfLink : MutableList<Sosmed> = mutableListOf<Sosmed>()
            editTextList.forEach{
                if(it.text.toString() != ""){
                    val sosmed : Sosmed = Sosmed()
                    sosmed.setLink(it.text.toString())
                    sosmed.setType(determineType(it.text.toString()))
                    listOfLink.add(sosmed)
                }
            }
            user.setSocmed(listOfLink.toList())
            database.child("Users").child(user.getUid()).setValue(user)
            val pref = PreferencesConfig(activity as Context)
            pref.putUser(user.getUid(), User.getInstance().getUsername()
                , User.getInstance().getImagePath()
                , User.getInstance().getDescription()
                , User.getInstance().getGenre()
                , User.getInstance().getSocmed()
                , User.getInstance().getEmail())
            requireActivity().startActivity(Intent(activity as Context, NewsActivity::class.java))
        }
    }

    private fun determineType(link : String) : String{
        if(link.contains("youtube")){
            return "Youtube"
        }
        else if (link.contains("steam")){
            return "Steam"
        }
        else if (link.contains("facebook")){
            return "Facebook"
        }
        else{
            return "Other"
        }
    }

}
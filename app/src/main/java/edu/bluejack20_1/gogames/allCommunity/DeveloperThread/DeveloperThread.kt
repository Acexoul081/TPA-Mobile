package edu.bluejack20_1.gogames.allCommunity.DeveloperThread

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.allCommunity.Reply.Reply
import edu.bluejack20_1.gogames.allCommunity.Reply.ReplyAdapter
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_developer_thread.*
import java.util.*

class DeveloperThread(var id: String, var title: String, var desc: String, var category: String) : Fragment(R.layout.fragment_developer_thread) {

    lateinit var replies: MutableList<Reply>
    private lateinit var reff: DatabaseReference

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        threadTitle.text = title
        textDescription.text = desc

        replies = mutableListOf()

        reff = FirebaseDatabase.getInstance().reference.child("Thread").child(category).child(id).child("Replies")

        readReplies()
        val pref = PreferencesConfig(context as Context)
        addReply.setOnClickListener {
            if(editText.text.toString() == ""){
                Toast.makeText(context as Context, "Text Field must be filled !", Toast.LENGTH_SHORT).show()
            }else{
                val uuid = UUID.randomUUID()
                val reply = Reply(uuid.toString(), pref.getUserID()!!, pref.getUsername()!!, editText.text.toString(), 0, 0)
                reff.child(uuid.toString()).setValue(reply)
            }
        }

    }

    private fun readReplies(){
        reff.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if(snapshot.exists()){
                        replies.clear()
                        for(i in snapshot.children){
                            val reply = i.getValue(Reply::class.java)
                            replies.add(reply!!)
                        }

                        val adapter = ReplyAdapter(replies, category, id, context as Context)
                        rvThreadReply.adapter = adapter
                        rvThreadReply.layoutManager = LinearLayoutManager(context)
                    }
                } catch (e: Exception) {
                    Log.getStackTraceString(e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}
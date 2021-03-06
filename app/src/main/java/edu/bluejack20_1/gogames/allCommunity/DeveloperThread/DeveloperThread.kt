package edu.bluejack20_1.gogames.allCommunity.DeveloperThread

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.allCommunity.Reply.LikeAndDislike
import edu.bluejack20_1.gogames.allCommunity.Reply.Reply
import edu.bluejack20_1.gogames.allCommunity.Reply.ReplyAdapter
import edu.bluejack20_1.gogames.allCommunity.UserProfile
import edu.bluejack20_1.gogames.allCommunity.createThread.DataThread
import edu.bluejack20_1.gogames.allCommunity.editThread.EditThread
import edu.bluejack20_1.gogames.allCommunity.preferThread.PreferThreadAdapter
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import kotlinx.android.synthetic.main.forum_thread.*
import kotlinx.android.synthetic.main.thread_fix.*
import kotlinx.android.synthetic.main.thread_fix.BtnDislike
import kotlinx.android.synthetic.main.thread_fix.BtnLike
import kotlinx.android.synthetic.main.thread_fix.DislikeCount
import kotlinx.android.synthetic.main.thread_fix.LikeCount
import java.util.*


class DeveloperThread(
    var id: String,
    var title: String,
    var desc: String,
    var category: String,
    var like: Number,
    var dislike: Number,
    var userID: String
) : Fragment(R.layout.thread_fix) {

    lateinit var replies: MutableList<Reply>
    lateinit var preferThreads: MutableList<DataThread>
    private lateinit var reff: DatabaseReference

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        threadTitlex.text = title
        threadDescription.text = desc

        replies = mutableListOf()



        reff = FirebaseDatabase.getInstance().reference.child("Thread").child(category).child(id).child(
            "Replies"
        )

        readReplies()

        likeDislike()

        getPreferThread()

        getUser()

        val pref = PreferencesConfig(context as Context)

//        pref.putUser("", "")

        headerText.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                val fragment = UserProfile(userID)
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }

        if(pref.getUserID().isNullOrBlank()){
            BtnLike.visibility = View.INVISIBLE
            BtnDislike.visibility = View.INVISIBLE
            replyText.visibility = View.INVISIBLE
            BtnReply.visibility = View.INVISIBLE
            DislikeCount.visibility = View.GONE
            LikeCount.visibility = View.GONE

        }

        if(pref.getUserID() == userID){
            buttonEdit.visibility = View.VISIBLE

            buttonEdit.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    val fragment = EditThread(id, title, desc, category, like, dislike, userID)
                    replace(R.id.mainFragment, fragment)
                    addToBackStack(null)
                    commit()
                }
            }
        }

        BtnReply.setOnClickListener {
            if(replyText.text.toString() == ""){
                Toast.makeText(
                    context as Context,
                    "Text Field must be filled !",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                val uuid = UUID.randomUUID()
                val reply = Reply(
                    uuid.toString(),
                    pref.getUserID()!!,
                    pref.getUsername()!!,
                    replyText.text.toString(),
                    0,
                    0
                )

                val ref = FirebaseDatabase.getInstance().reference.child("Thread").child(category).child(
                    id
                )

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.child("threadID").value.toString() == id) {
                                var totalReplied: Long =
                                    snapshot.child("totalReplied").value as Long
                                totalReplied += 1
                                ref.child("totalReplied").setValue(totalReplied)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
                reff.child(uuid.toString()).setValue(reply)
            }
        }
    }

    private fun readReplies(){
        reff.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
                        replies.clear()
                        for (i in snapshot.children) {
                            val reply = i.getValue(Reply::class.java)
                            replies.add(reply!!)
                        }
                        val adapter = ReplyAdapter(replies, category, id, context as Context)
                        rvThreadReplies.adapter = adapter
                        rvThreadReplies.layoutManager = LinearLayoutManager(context)
                    }
                } catch (e: Exception) {
                }
            }

            override fun onCancelled(error: DatabaseError) {
                replies.clear()
                val adapter = ReplyAdapter(replies, category, id, context as Context)
                rvThreadReplies.adapter = adapter
                rvThreadReplies.layoutManager = LinearLayoutManager(context)
            }

        })
    }

    private fun likeDislike(){
        val refLikeDislike = FirebaseDatabase.getInstance().reference.child("Thread").child(category).child(
            id
        ).child("LikeAndDislike")

        val pref = PreferencesConfig(activity as Context)

        val userID = pref.getUserID()

        var like = like as Int
        var dislike = dislike as Int

        LikeCount.text = like.toString()
        DislikeCount.text = dislike.toString()

        BtnLike.setOnClickListener{
            refLikeDislike.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (userID != null) {
                        if (snapshot.exists()) {
                            if (snapshot.hasChild(userID!!)) {
                                if (snapshot.child(userID)
                                        .child("type").value.toString() == "Like"
                                ) {
                                    like -= 1
                                    refLikeDislike.parent?.child("like")?.setValue(like)
                                    LikeCount.text = like.toString()
                                    refLikeDislike.child(userID).removeValue()
                                } else {
                                    dislike -= 1
                                    like += 1
                                    refLikeDislike.child(userID).child("type").setValue("Like")
                                    refLikeDislike.parent?.child("like")?.setValue(like)
                                    refLikeDislike.parent?.child("dislike")?.setValue(dislike)
                                    LikeCount.text = like.toString()
                                    DislikeCount.text = dislike.toString()
                                }
                            } else {
                                val likeAndDislike = LikeAndDislike(userID, "Like")
                                refLikeDislike.child(userID).setValue(likeAndDislike)
                                like += 1
                                refLikeDislike.parent?.child("like")?.setValue(like)
                                LikeCount.text = like.toString()
                            }
                        } else {
                            val likeAndDislike = LikeAndDislike(userID, "Like")
                            refLikeDislike.child(userID).setValue(likeAndDislike)
                            like += 1
                            refLikeDislike.parent?.child("like")?.setValue(like)
                            LikeCount.text = like.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        BtnDislike.setOnClickListener{
            refLikeDislike.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (userID != null) {
                        if (snapshot.exists()) {
                            if (snapshot.hasChild(userID!!)) {
                                if (snapshot.child(userID)
                                        .child("type").value.toString() == "Dislike"
                                ) {
                                    dislike -= 1
                                    refLikeDislike.parent?.child("dislike")?.setValue(dislike)
                                    DislikeCount.text = dislike.toString()
                                    refLikeDislike.child(userID).removeValue()
                                } else {
                                    dislike += 1
                                    like -= 1
                                    refLikeDislike.child(userID).child("type").setValue("Dislike")
                                    refLikeDislike.parent?.child("like")?.setValue(like)
                                    refLikeDislike.parent?.child("dislike")?.setValue(dislike)
                                    LikeCount.text = like.toString()
                                    DislikeCount.text = dislike.toString()
                                }
                            } else {
                                val likeAndDislike = LikeAndDislike(userID, "Dislike")
                                refLikeDislike.child(userID).setValue(likeAndDislike)
                                dislike += 1
                                refLikeDislike.parent?.child("dislike")?.setValue(dislike)
                                DislikeCount.text = dislike.toString()
                            }
                        } else {
                            val likeAndDislike = LikeAndDislike(userID, "Dislike")
                            refLikeDislike.child(userID).setValue(likeAndDislike)
                            dislike += 1
                            refLikeDislike.parent?.child("dislike")?.setValue(dislike)
                            DislikeCount.text = dislike.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun getPreferThread() {
        preferThreads = mutableListOf()

        var suggestThreads = mutableListOf<DataThread>()

        var ref = FirebaseDatabase.getInstance().reference.child("Thread").child(category)

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    preferThreads.clear()
                    for(i in snapshot.children){
                        val thread = i.getValue(DataThread::class.java)
                        preferThreads.add(thread!!)
                    }
                    var x = 0
                    for(i in preferThreads){
                        var thread = preferThreads.random()
                        suggestThreads.add(thread)
                        if(thread.threadID == id){
                            suggestThreads.removeLast()
                            x--;
                        }
                        x++
                        if(x == 5) break;
                    }
                    var adapter = PreferThreadAdapter(suggestThreads, context as Context)
                    rvPreferThread.adapter = adapter
                    rvPreferThread.layoutManager = LinearLayoutManager(context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getUser(){
        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(userID)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var imagePath = snapshot.child("imagePath").value.toString()
                    var username = snapshot.child("username").value.toString()

                    if(!imagePath.isNullOrBlank()){
                        Glide.with(activity as Context).load(imagePath).circleCrop().into(profileImage)
                    }
                    if(username.isNullOrBlank()){
                        usernameUpload.text = "Anonymous"
                    }else{
                        usernameUpload.text = username
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
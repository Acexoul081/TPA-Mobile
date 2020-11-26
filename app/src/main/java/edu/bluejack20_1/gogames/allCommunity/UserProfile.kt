package edu.bluejack20_1.gogames.allCommunity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.allCommunity.createThread.DataThread
import edu.bluejack20_1.gogames.allCommunity.preferThread.PreferThreadAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.thread_fix.*
import kotlinx.android.synthetic.main.user_profile.*

class UserProfile(var userID: String) : Fragment(R.layout.user_profile) {

    lateinit var developerThreads: MutableList<DataThread>
    lateinit var gameThreads: MutableList<DataThread>
    lateinit var otherThreads: MutableList<DataThread>
    lateinit var mainThreadList: MutableList<DataThread>
    lateinit var userThread: MutableList<DataThread>
    var allDevelopers = false
    var allGames = false
    var allOthers = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        developerThreads = mutableListOf()
        gameThreads = mutableListOf()
        otherThreads = mutableListOf()
        mainThreadList = mutableListOf()
        userThread = mutableListOf()

        getUser()

        getTopPost()
    }

    fun getUser(){
        var ref = FirebaseDatabase.getInstance().reference.child("Users").child(userID)

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var imagePath = snapshot.child("imagePath").value.toString()
                    var username = snapshot.child("username").value.toString()
                    var description = snapshot.child("description").value.toString()

                    if(!imagePath.isNullOrBlank()){
                        Glide.with(activity as Context).load(imagePath).circleCrop().into(profileImagex)
                    }
                    if(username.isNullOrBlank()){
                        profileName.text = "Anonymous"
                    }else{
                        profileName.text = username
                    }

                    if(!description.isNullOrBlank()){
                        userDescription.text = description
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getTopPost(){
        var ref = FirebaseDatabase.getInstance().reference.child("Thread")

        ref.child("Developer").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    developerThreads.clear()
                    for(i in snapshot.children){
                        val thread = i.getValue(DataThread::class.java)
                        developerThreads.add(thread!!)
                    }
                    if(developerThreads.size.toLong() == snapshot.childrenCount){
                        mainThreadList.addAll(developerThreads)
                        allDevelopers = true
                        show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        ref.child("Game").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    gameThreads.clear()
                    for(i in snapshot.children){
                        val thread = i.getValue(DataThread::class.java)
                        gameThreads.add(thread!!)
                    }
                    if(gameThreads.size.toLong() == snapshot.childrenCount){
                        mainThreadList.addAll(gameThreads)
                        allGames = true
                        show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        ref.child("Other").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    otherThreads.clear()
                    for(i in snapshot.children){
                        val thread = i.getValue(DataThread::class.java)
                        otherThreads.add(thread!!)
                    }
                    if(otherThreads.size.toLong() == snapshot.childrenCount){
                        mainThreadList.addAll(otherThreads)
                        allOthers = true
                        show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun show(){
        if(allOthers && allGames && allDevelopers){
            mainThreadList.iterator().forEach {
                if(it.userID == userID){
                    userThread.add(it)
                }
            }
            var adapter = PreferThreadAdapter(userThread, context as Context)
            thisUserThread.adapter = adapter
            thisUserThread.layoutManager = LinearLayoutManager(context)
        }
    }

}
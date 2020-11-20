package edu.bluejack20_1.gogames

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.bluejack20_1.gogames.allCommunity.createThread.DataThread
import edu.bluejack20_1.gogames.allCommunity.preferThread.PreferThreadAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_top_thread.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var developerThreads: MutableList<DataThread>
    lateinit var gameThreads: MutableList<DataThread>
    lateinit var otherThreads: MutableList<DataThread>
    lateinit var mainThreadList: MutableList<DataThread>
    var allDevelopers = false
    var allGames = false
    var allOthers = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buttonListener()

        developerThreads = mutableListOf()
        gameThreads = mutableListOf()
        otherThreads = mutableListOf()
        mainThreadList = mutableListOf()

        getTopPost()

    }

    private fun buttonListener() {
        homeCommunityButton.setOnClickListener {
            val fragment = CommunityHome()
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }
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
            var adapter = PreferThreadAdapter(mainThreadList, context as Context)
            rvListOfThreads.adapter = adapter
            rvListOfThreads.layoutManager = LinearLayoutManager(context)
        }
    }



}
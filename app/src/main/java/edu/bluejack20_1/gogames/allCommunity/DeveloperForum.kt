package edu.bluejack20_1.gogames.allCommunity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.allCommunity.createThread.DataThread
import edu.bluejack20_1.gogames.allCommunity.thread.DeveloperThreadAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_developer_forum.*

class DeveloperForum(var category: String) : Fragment(R.layout.fragment_developer_forum) {

    lateinit var threadList: MutableList<DataThread>
    lateinit var  reff: DatabaseReference

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        threadList = mutableListOf()
        reff = FirebaseDatabase.getInstance().reference.child("Thread").child(category)

        readThread()
    }

    private fun readThread(){
        reff.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    threadList.clear()
                    for(i in snapshot.children){
                        val thread = i.getValue(DataThread::class.java)
                        threadList.add(thread!!)
                    }

                    val adapter = DeveloperThreadAdapter(threadList, context as Context)
                    RvDeveloperThread.adapter = adapter
                    RvDeveloperThread.layoutManager = LinearLayoutManager(context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}
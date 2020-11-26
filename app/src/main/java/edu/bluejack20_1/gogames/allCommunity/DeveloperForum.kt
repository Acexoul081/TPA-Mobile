package edu.bluejack20_1.gogames.allCommunity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.allCommunity.createThread.DataThread
import edu.bluejack20_1.gogames.allCommunity.thread.DeveloperThreadAdapter
import com.google.firebase.database.*
import edu.bluejack20_1.gogames.allCommunity.preferThread.PreferThreadAdapter
import kotlinx.android.synthetic.main.fragment_developer_forum.*

class DeveloperForum(var category: String) : Fragment(R.layout.fragment_developer_forum) {

    lateinit var threadList: MutableList<DataThread>
    lateinit var  reff: DatabaseReference
    lateinit var adapter: PreferThreadAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        threadList = mutableListOf()
        reff = FirebaseDatabase.getInstance().reference.child("Thread").child(category)

        readThread()
        sort()
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

                    adapter = PreferThreadAdapter(threadList, context as Context)
                    RvDeveloperThread.adapter = adapter
                    RvDeveloperThread.layoutManager = LinearLayoutManager(context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun sort(){
        sort_by?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2 == 0){
                    threadList.sortByDescending { it.date }
                    adapter = PreferThreadAdapter(threadList, context as Context)
                    RvDeveloperThread.adapter = adapter
                    RvDeveloperThread.layoutManager = LinearLayoutManager(context)
                }else{
                    threadList.sortByDescending { it.totalReplied }
                    adapter = PreferThreadAdapter(threadList, context as Context)
                    RvDeveloperThread.adapter = adapter
                    RvDeveloperThread.layoutManager = LinearLayoutManager(context)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }

}
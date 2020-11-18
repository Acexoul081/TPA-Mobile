package edu.bluejack20_1.gogames.allCommunity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.allCommunity.createThread.DataThread
import edu.bluejack20_1.gogames.allCommunity.preferThread.PreferThreadAdapter
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment(var keyword: String) : Fragment(R.layout.fragment_search) {

    lateinit var ref: DatabaseReference

    var developerThreads: MutableList<DataThread> = mutableListOf()
    var gameThreads: MutableList<DataThread> = mutableListOf()
    var otherThreads: MutableList<DataThread> = mutableListOf()

    var mainThreadList: MutableList<DataThread> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(keyword, keyword)

        ref = FirebaseDatabase.getInstance().reference.child("Thread")
        search("Developer")
        search("Game")
        search("Other")
    }

    fun search(category: String){
        var Dev: Int = 0
        ref.child(category).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        Dev++
                        val thread = i.getValue(DataThread::class.java)
                        var title: String? = thread?.title?.toUpperCase()
                        if (title != null) {
                            if(title.contains(keyword)){
                                if(category == "Developer"){
                                    developerThreads.add(thread!!)
                                }else if(category == "Game"){
                                    gameThreads.add(thread!!)
                                }else{
                                    otherThreads.add(thread!!)
                                }
                            }
                        }
                    }
                    if(category == "Developer"){
                        if(Dev.toLong() == snapshot.childrenCount){
                            mainThreadList.addAll(developerThreads)
                            addToAdapter()
                        }
                    }else if(category == "Game"){
                        if(gameThreads.size.toLong() == snapshot.childrenCount){
                            mainThreadList.addAll(gameThreads)
                            addToAdapter()
                        }
                    }else{
                        if(otherThreads.size.toLong() == snapshot.childrenCount){
                            mainThreadList.addAll(otherThreads)
                            addToAdapter()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun addToAdapter(){
            var adapter = PreferThreadAdapter(mainThreadList, context as Context)
            rvSearchThread.adapter = adapter
            rvSearchThread.layoutManager = LinearLayoutManager(context)
    }
}
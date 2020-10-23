package edu.bluejack20_1.gogames.allCommunity.Reply

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_1.gogames.MainActivity
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.thread_reply.view.*

class ReplyAdapter(private val replies: List<Reply>, private val category: String, private val threadId: String, var parentContext: Context) : RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    inner class ReplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.thread_reply, parent, false)
        return ReplyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.itemView.apply {
            userReply.text = replies[position].userName
            description.text = replies[position].description
            LikeCount.text = replies[position].like.toString()
            DislikeCount.text = replies[position].dislike.toString()

            val reff = FirebaseDatabase.getInstance().reference.child("Thread").child(category).child(threadId).child("Replies").child(replies[position].id).child("LikeAndDislike")
            val activity: MainActivity = parentContext as MainActivity
            val pref = PreferencesConfig(activity)
            val userId = pref.getUserID()

            var like = replies[position].like
            var dislike = replies[position].dislike

            BtnLike.setOnClickListener{
                reff.addListenerForSingleValueEvent( object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(userId != null){
                            if(snapshot.exists()){
                                if(snapshot.hasChild(userId!!)){
                                    if(snapshot.child(userId).child("type").value.toString() == "Like"){
                                        like -= 1
                                        reff.parent?.child("like")?.setValue(like)
                                        LikeCount.text = (like).toString()
                                        reff.child(userId).removeValue()
                                    }else{
                                        dislike -= 1
                                        reff.parent?.child("dislike")?.setValue(dislike)
                                        DislikeCount.text = (dislike).toString()
                                        reff.child(userId).child("type").setValue("Like")
                                        like += 1
                                        reff.parent?.child("like")?.setValue(like)
                                        LikeCount.text = (like).toString()
                                    }

                                }else{
                                    val likeAndDislike = LikeAndDislike(userId, "Like")
                                    reff.child(userId).setValue(likeAndDislike)
                                    like += 1
                                    reff.parent?.child("like")?.setValue(like)
                                    LikeCount.text = (like).toString()
                                }
                            }else{
                                val likeAndDislike = LikeAndDislike(userId, "Like")
                                reff.child(userId).setValue(likeAndDislike)
                                like += 1
                                reff.parent?.child("like")?.setValue(like)
                                LikeCount.text = (like).toString()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }

            BtnDislike.setOnClickListener {
                reff.addListenerForSingleValueEvent(object :  ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(userId != null){
                            if(snapshot.exists()){
                                if(snapshot.hasChild(userId!!)){
                                    if(snapshot.child(userId).child("type").value.toString() == "Dislike"){
                                        dislike -= 1
                                        reff.parent?.child("dislike")?.setValue(like)
                                        DislikeCount.text = (dislike).toString()
                                        reff.child(userId).removeValue()
                                    }else{
                                        like -= 1
                                        reff.parent?.child("like")?.setValue(like)
                                        LikeCount.text = (like).toString()
                                        reff.child(userId).child("type").setValue("Dislike")
                                        dislike += 1
                                        reff.parent?.child("dislike")?.setValue(like)
                                        DislikeCount.text = (dislike).toString()
                                    }

                                }else{
                                    val likeAndDislike = LikeAndDislike(userId, "Dislike")
                                    reff.child(userId).setValue(likeAndDislike)
                                    dislike += 1
                                    reff.parent?.child("dislike")?.setValue(like)
                                    DislikeCount.text = (dislike).toString()
                                }
                            }else{
                                val likeAndDislike = LikeAndDislike(userId, "Dislike")
                                reff.child(userId).setValue(likeAndDislike)
                                dislike += 1
                                reff.parent?.child("dislike")?.setValue(like)
                                DislikeCount.text = (dislike).toString()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }


        }
    }

    override fun getItemCount(): Int {
        return replies.size
    }
}
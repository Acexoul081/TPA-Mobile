package edu.bluejack20_1.gogames.allCommunity.thread

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack20_1.gogames.MainActivity
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.allCommunity.createThread.DataThread
import kotlinx.android.synthetic.main.forum_thread.view.*

class DeveloperThreadAdapter(private var DeveloperThreads: List<DataThread>, private var parentContext: Context) : RecyclerView.Adapter<DeveloperThreadAdapter.DeveloperThreadViewHolder>() {

    inner class DeveloperThreadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeveloperThreadViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.forum_thread, parent, false)
        return DeveloperThreadViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeveloperThreadViewHolder, position: Int) {
        holder.itemView.apply {
            threadTitle.text = DeveloperThreads[position].title
            var tempDesc: String = "test"
            if(DeveloperThreads[position].description.length <= 31){
                tempDesc = DeveloperThreads[position].description
            }else{
                tempDesc = DeveloperThreads[position].description.take(31) + "..."
            }
            threadText.text = tempDesc

            threadParent.setOnClickListener {
                val activity: MainActivity = parentContext as MainActivity


                val ref = FirebaseDatabase.getInstance().reference.child("Thread").child(DeveloperThreads[position].category).child(DeveloperThreads[position].threadID).child("view")
                var view = DeveloperThreads[position].view
                view += 1
                ref.setValue(view)

                    activity.redirectToMainThread(DeveloperThreads[position].threadID, DeveloperThreads[position].title, DeveloperThreads[position].description, DeveloperThreads[position].category, DeveloperThreads[position].like, DeveloperThreads[position].dislike, DeveloperThreads[position].userID)
            }
        }
    }

    override fun getItemCount(): Int {
        return DeveloperThreads.size
    }
}
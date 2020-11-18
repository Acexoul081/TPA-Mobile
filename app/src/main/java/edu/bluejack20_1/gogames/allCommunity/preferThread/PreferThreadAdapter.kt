package edu.bluejack20_1.gogames.allCommunity.preferThread

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack20_1.gogames.MainActivity
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.allCommunity.createThread.DataThread
import kotlinx.android.synthetic.main.prefered_thread.view.*

class PreferThreadAdapter(private var threads: List<DataThread>, private var parentContext: Context) : RecyclerView.Adapter<PreferThreadAdapter.PreferThreadViewHolder>() {

    inner class PreferThreadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferThreadViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.prefered_thread, parent, false)
        return  PreferThreadViewHolder(view)
    }

    override fun onBindViewHolder(holder: PreferThreadViewHolder, position: Int) {
        holder.itemView.apply {
            username.text = threads[position].title
            if(threads[position].description.length <= 500) description.text = threads[position].description
            else description.text = threads[position].description.take(500) + "..."
            preferClick.setOnClickListener {
                val ref = FirebaseDatabase.getInstance().reference.child("Thread").child(threads[position].category).child(threads[position].threadID)
                var view = threads[position].view
                view += 1
                ref.child("view").setValue(view)
                val activity: MainActivity = parentContext as MainActivity
                activity.redirectToMainThread(threads[position].threadID, threads[position].title, threads[position].description, threads[position].category, threads[position].like, threads[position].dislike, threads[position].userID)
            }
        }
    }

    override fun getItemCount(): Int {
        return threads.size
    }

}
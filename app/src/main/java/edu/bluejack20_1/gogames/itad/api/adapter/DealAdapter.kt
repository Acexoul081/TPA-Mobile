package edu.bluejack20_1.gogames.itad.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.itad.api.entity.Deal
import kotlinx.android.synthetic.main.itemview_promos_rv.view.*

class DealAdapter(private val list: ArrayList<Deal>):RecyclerView.Adapter<DealAdapter.DealViewHolder>(){
    inner class DealViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(deal: Deal){
            with(itemView){
                val text = deal.title + deal.price_new + deal.added + deal.urls
                tvPromoInfo.text = text
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemview_promos_rv, parent, false)
        return DealViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DealAdapter.DealViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
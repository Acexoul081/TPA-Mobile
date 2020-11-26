package edu.bluejack20_1.gogames.rawg.ui.games

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.bluejack20_1.gogames.rawg.entity.Game
import edu.bluejack20_1.gogames.R
import kotlinx.android.synthetic.main.itemview_games_rv.view.*

class GamesAdapter (val listener: RecyclerViewItemClickLister) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface  RecyclerViewItemClickLister{
        fun onRecycleViewItemSelected(item: Game)
    }

    private var games: List<Game> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GamesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.itemview_games_rv, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is GamesViewHolder -> {
                holder.bind(games[position])
                    .setOnClickListener { listener.onRecycleViewItemSelected(games[position])}
            }
        }
    }

    fun updateList(list: List<Game>){
        games = list
        notifyDataSetChanged()
    }

    class GamesViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){
        val image = itemView.game_title_img
        val title = itemView.game_title_txt

        fun bind(game: Game): View{
            title.text = game.name

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)


            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(game.background_image)
                .into(image)
            return itemView
        }
    }

}